import * as crypto from 'crypto'
import { config } from 'su-config'
import { ArgumentParser } from 'argparse'
import * as shell from 'shelljs'
import * as path from 'path'
import * as fs from 'fs'
import * as ethers from 'ethers'
const ganache = require("ganache-cli")
var Web3 = require('web3');
var web3 = new Web3('http://localhost:8545');

const genAccounts = (
    num: number,
    mnemonic: string,
	provider: ethers.providers.JsonRpcProvider,
) => {
    let accounts: ethers.Wallet[] = []

    for (let i=0; i<num; i++) {
        const path = `m/44'/60'/${i}'/0/0`
        let wallet = ethers.Wallet.fromMnemonic(mnemonic, path)
        wallet = wallet.connect(provider)
        accounts.push(wallet)
    }

    return accounts
}

let manufacturer
let note
let merchant
let buyer

const tokenName = "token"
const tokenSymbol = "TKN"
const tokenDecimal = 30
const amount = 1

const execute = (cmd: string) => {
    const result = shell.exec(cmd, { silent: false })
    if (result.code !== 0) {
        throw 'Error executing ' + cmd
    }

    return result
}

const readFile = (abiDir: string, filename: string) => {
    return fs.readFileSync(path.join(abiDir, filename)).toString()
}

const compileAndDeploy = async (
    abiDir: string,
    solDir: string,
    solcBinaryPath: string = 'solc',
	provider: ethers.providers.JsonRpcProvider,
	deployerWallet: ethers.Wallet,
	manufacturer: ethers.Wallet,
	// note: ethers.Wallet,
    merchant: ethers.Wallet,
    buyer: ethers.Wallet,
) => {

    const readAbiAndBin = (name: string) => {
        const abi = readFile(abiDir, name + '.abi')
        const bin = readFile(abiDir, name + '.bin')
        return { abi, bin }
    }

	// compile contracts
	shell.mkdir('-p', abiDir)
	const solcCmd = `${solcBinaryPath} -o ${abiDir} ${solDir}/*.sol --overwrite --optimize --abi --bin`
	const result = execute(solcCmd)

	// fund accounts
    const numEth = 2
    const addressesToFund = [
		manufacturer.address,
        merchant.address,
        buyer.address,
    ]
    for (let address of addressesToFund) {
        let tx

        tx = await provider.sendTransaction(
            deployerWallet.sign({
                nonce: await provider.getTransactionCount(deployerWallet.address),
                gasPrice: ethers.utils.parseUnits('10', 'gwei'),
                gasLimit: 21000,
                to: address,
                value: ethers.utils.parseUnits(numEth.toString(), 'ether'),
                data: '0x'
            })
        )
        let receipt = await tx.wait()
        console.log(`Gave away ${numEth} ETH to`, address)
    }


    // deploy HardwareNotes
    const hardwareNotesAB = readAbiAndBin('HardwareNotes')
    const hardwareNotesContractFactory = new ethers.ContractFactory(hardwareNotesAB.abi, hardwareNotesAB.bin, deployerWallet)
    const hardwareNotesContract = await hardwareNotesContractFactory.deploy(
        {gasPrice: ethers.utils.parseUnits('10', 'gwei')},
    )
    await hardwareNotesContract.deployed()

    console.log('Deployed HardwareNotes at', hardwareNotesContract.address)

    // deploy Token
    const tokenAB = readAbiAndBin('Token')
    const tokenContractFactory = new ethers.ContractFactory(tokenAB.abi, tokenAB.bin, buyer)
    const tokenContract = await tokenContractFactory.deploy(
		tokenName, tokenSymbol, tokenDecimal,
        {gasPrice: ethers.utils.parseUnits('10', 'gwei')},
    )
    await tokenContract.deployed()

    console.log('Deployed HardwareToken at', tokenContract.address)

	return {
		HardwareNotes: hardwareNotesContract,
		Token: tokenContract
	}
}

const main = async () => {
    const parser = new ArgumentParser({
        description: 'Build and deploy contracts'
    })

    parser.addArgument(
        ['-s', '--solc'],
        {
            help: 'The path to the solc binary',
            required: false,
        }
    )

    parser.addArgument(
        ['-r', '--rpcUrl'],
        {
            help: 'The JSON-RPC URL of the Ethereum node',
            required: false,
        }
    )

    parser.addArgument(
        ['-o', '--out'],
        {
            help: 'The output directory for compiled files',
            required: true,
        }
    )

    parser.addArgument(
        ['-i', '--input'],
        {
            help: 'The input directory with .sol files',
            required: true,
        }
    )

    // parse command-line options
    const args = parser.parseArgs()

    const abiDir = path.resolve(args.out)
    const solDir = path.resolve(args.input)
    const solcBinaryPath = args.solc ? args.solc : 'solc'

    const rpcUrl = args.rpcUrl ? args.rpcUrl : config.chain.url

    // generate provider and walllets
    const provider = new ethers.providers.JsonRpcProvider(rpcUrl)
	const wallets = genAccounts(5, config.chain.mnemonic, provider)

    const deployerWallet: ethers.Wallet = wallets[0]
	const manufacturer: ethers.Wallet = wallets[1]
	const note: ethers.Wallet = wallets[2]
	const merchant: ethers.Wallet = wallets[3]
	const buyer: ethers.Wallet = wallets[4]

    // deploy contracts
    const contracts = await compileAndDeploy(
        abiDir,
        solDir,
        solcBinaryPath,
		provider,
        deployerWallet,
		manufacturer,
		// note,
		merchant,
		buyer
    )
	const hardwareNotesContract = contracts.HardwareNotes
	const tokenContract = contracts.Token

    console.log()
	console.log('========================================')
	
	// manufacturer registers a batch of notes
	console.log('Registering batch 0')
	const hardwareNotesContractWithManufacturer = hardwareNotesContract.connect(manufacturer)
	const registerTx = await hardwareNotesContractWithManufacturer.registerBatch(0, [note.address])
	await registerTx.wait()
	console.log(` Manufacturer ${manufacturer.address} registered batch [${note.address}] via transaction ${registerTx.hash} `)


    console.log()
	console.log('========================================')
	
	// buyer approves HardwareNotes.sol to call transferFrom
	console.log(`Approving HardwareNotes contract to  call transferFrom`)
	const tokenContractWithBuyer = tokenContract.connect(buyer)
	const approveTx = await tokenContractWithBuyer.approve(hardwareNotesContract.address, amount)
	await approveTx.wait()
	console.log(`Buyer ${buyer.address} approved HardwareNotes contract ${hardwareNotesContract.address} to transfer ${amount} on Token contract ${tokenContract.address}` )

	// buyer deposits into note
	console.log(`Depositing into note ${note.address}`)
	const hardwareNotesContractWithBuyer = hardwareNotesContract.connect(buyer)
	const depositTx = await hardwareNotesContractWithBuyer.deposit(
		manufacturer.address, 0, 0, 0, tokenContract.address, amount, 10000, 100000000 
	)
	await depositTx.wait()
	console.log(` Buyer ${buyer.address} deposited into note ${note.address} via transaction ${depositTx.hash} `)


    console.log()
	console.log('========================================')

	// merchant calls signalWithdraw with a signed message from the note

	let blockNumber1 = await provider.getBlockNumber()
	let flatSig1 = await note.signMessage(blockNumber1.toString())
	let sig1 = ethers.utils.splitSignature(flatSig1)
	const hardwareNotesContractWithMerchant = hardwareNotesContract.connect(merchant)
	const signalWithdrawTx = await hardwareNotesContractWithMerchant.signalWithdraw(
		0, 0, 0, blockNumber1.toString(), sig1.v, sig1.r, sig1.s
	)
	await signalWithdrawTx.wait()
	console.log(` Merchant signalled withdraw for note ${note.address} via transaction ${signalWithdrawTx.hash}`)

    console.log()
	console.log('========================================')

	// fast forward in time
	let blockBefore = await provider.getBlock(provider.getBlockNumber())
	console.log('timestampBefore', blockBefore.timestamp)

	let time = 1000000
	async function increaseTime(time) {
		await web3.currentProvider.send({
			jsonrpc: '2.0',
			method: 'evm_increaseTime',
			params: [time],
			id: new Date().getSeconds(),
		}, ()=>{})
		
		await web3.currentProvider.send({
			jsonrpc: '2.0',
			method: 'evm_mine',
			params: [],
			id: new Date().getSeconds(),
		}, ()=>{})

		let blockAfter = await provider.getBlock(provider.getBlockNumber())
		console.log('timestamp after', blockAfter.timestamp)
		console.log()
		console.log('========================================')

	}
	
	let timeTravel = await increaseTime(time)
	
	// merchant calls withdraw with another signed message from the note
	let blockNumber2 = await provider.getBlockNumber()
	let flatSig2 = await note.signMessage(blockNumber2.toString())
	let sig2 = ethers.utils.splitSignature(flatSig2)
	const withdrawTx = await hardwareNotesContractWithMerchant.withdraw(
		0, 0, 0, blockNumber2.toString(), sig2.v, sig2.r, sig2.s, merchant.address
	)
	await withdrawTx.wait()
	console.log(` Merchant called withdraw for note ${note.address} to recipient ${merchant.address} via transaction ${withdrawTx.hash}`)

}

if (require.main === module) {
    main()
}
