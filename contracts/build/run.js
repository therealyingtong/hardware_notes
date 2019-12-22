"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
exports.__esModule = true;
var su_config_1 = require("su-config");
var argparse_1 = require("argparse");
var shell = require("shelljs");
var path = require("path");
var fs = require("fs");
var ethers = require("ethers");
var genAccounts = function (num, mnemonic, provider) {
    var accounts = [];
    for (var i = 0; i < num; i++) {
        var path_1 = "m/44'/60'/" + i + "'/0/0";
        var wallet = ethers.Wallet.fromMnemonic(mnemonic, path_1);
        wallet = wallet.connect(provider);
        accounts.push(wallet);
    }
    return accounts;
};
var manufacturer;
var note;
var merchant;
var buyer;
var tokenName = "hardwareToken";
var tokenSymbol = "HWT";
var tokenDecimal = 10;
var execute = function (cmd) {
    var result = shell.exec(cmd, { silent: false });
    if (result.code !== 0) {
        throw 'Error executing ' + cmd;
    }
    return result;
};
var readFile = function (abiDir, filename) {
    return fs.readFileSync(path.join(abiDir, filename)).toString();
};
var compileAndDeploy = function (abiDir, solDir, solcBinaryPath, provider, deployerWallet, manufacturer, 
// note: ethers.Wallet,
merchant, buyer) {
    if (solcBinaryPath === void 0) { solcBinaryPath = 'solc'; }
    return __awaiter(void 0, void 0, void 0, function () {
        var readAbiAndBin, solcCmd, result, hardwareNotesAB, hardwareNotesContractFactory, hardwareNotesContract, tokenAB, tokenContractFactory, tokenContract, numEth, addressesToFund, _i, addressesToFund_1, address, tx, _a, _b, _c, _d, _e, receipt;
        return __generator(this, function (_f) {
            switch (_f.label) {
                case 0:
                    readAbiAndBin = function (name) {
                        var abi = readFile(abiDir, name + '.abi');
                        var bin = readFile(abiDir, name + '.bin');
                        return { abi: abi, bin: bin };
                    };
                    // compile contracts
                    shell.mkdir('-p', abiDir);
                    solcCmd = solcBinaryPath + " -o " + abiDir + " " + solDir + "/*.sol --overwrite --optimize --abi --bin";
                    result = execute(solcCmd);
                    hardwareNotesAB = readAbiAndBin('HardwareNotes');
                    hardwareNotesContractFactory = new ethers.ContractFactory(hardwareNotesAB.abi, hardwareNotesAB.bin, deployerWallet);
                    return [4 /*yield*/, hardwareNotesContractFactory.deploy({ gasPrice: ethers.utils.parseUnits('10', 'gwei') })];
                case 1:
                    hardwareNotesContract = _f.sent();
                    return [4 /*yield*/, hardwareNotesContract.deployed()];
                case 2:
                    _f.sent();
                    console.log('Deployed HardwareNotes at', hardwareNotesContract.address);
                    tokenAB = readAbiAndBin('Token');
                    tokenContractFactory = new ethers.ContractFactory(tokenAB.abi, tokenAB.bin, deployerWallet);
                    return [4 /*yield*/, tokenContractFactory.deploy(tokenName, tokenSymbol, tokenDecimal, { gasPrice: ethers.utils.parseUnits('10', 'gwei') })];
                case 3:
                    tokenContract = _f.sent();
                    return [4 /*yield*/, tokenContract.deployed()];
                case 4:
                    _f.sent();
                    console.log('Deployed HardwareToken at', tokenContract.address);
                    numEth = 2;
                    addressesToFund = [
                        manufacturer.address,
                        merchant.address,
                        buyer.address,
                    ];
                    _i = 0, addressesToFund_1 = addressesToFund;
                    _f.label = 5;
                case 5:
                    if (!(_i < addressesToFund_1.length)) return [3 /*break*/, 10];
                    address = addressesToFund_1[_i];
                    tx = void 0;
                    _b = (_a = provider).sendTransaction;
                    _d = (_c = deployerWallet).sign;
                    _e = {};
                    return [4 /*yield*/, provider.getTransactionCount(deployerWallet.address)];
                case 6: return [4 /*yield*/, _b.apply(_a, [_d.apply(_c, [(_e.nonce = _f.sent(),
                                _e.gasPrice = ethers.utils.parseUnits('10', 'gwei'),
                                _e.gasLimit = 21000,
                                _e.to = address,
                                _e.value = ethers.utils.parseUnits(numEth.toString(), 'ether'),
                                _e.data = '0x',
                                _e)])])];
                case 7:
                    tx = _f.sent();
                    return [4 /*yield*/, tx.wait()];
                case 8:
                    receipt = _f.sent();
                    console.log("Gave away " + numEth + " ETH to", address);
                    _f.label = 9;
                case 9:
                    _i++;
                    return [3 /*break*/, 5];
                case 10: return [2 /*return*/, {
                        HardwareNotes: hardwareNotesContract
                    }];
            }
        });
    });
};
var main = function () { return __awaiter(void 0, void 0, void 0, function () {
    var parser, args, abiDir, solDir, solcBinaryPath, rpcUrl, provider, wallets, deployerWallet, manufacturer, note, merchant, buyer, contracts, hardwareNotesContract, hardwareNotesContractWithManufacturer, registerTx, hardwareNotesContractWithBuyer, depositTx, blockNumber1, flatSig1, sig1, hardwareNotesContractWithMerchant, signalWithdrawTx, blockNumberBefore, time, timeTravel, timeTravelTx, blockNumberAfter, blockNumber2, flatSig2, sig2, withdrawTx;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                parser = new argparse_1.ArgumentParser({
                    description: 'Build and deploy contracts'
                });
                parser.addArgument(['-s', '--solc'], {
                    help: 'The path to the solc binary',
                    required: false
                });
                parser.addArgument(['-r', '--rpcUrl'], {
                    help: 'The JSON-RPC URL of the Ethereum node',
                    required: false
                });
                parser.addArgument(['-o', '--out'], {
                    help: 'The output directory for compiled files',
                    required: true
                });
                parser.addArgument(['-i', '--input'], {
                    help: 'The input directory with .sol files',
                    required: true
                });
                args = parser.parseArgs();
                abiDir = path.resolve(args.out);
                solDir = path.resolve(args.input);
                solcBinaryPath = args.solc ? args.solc : 'solc';
                rpcUrl = args.rpcUrl ? args.rpcUrl : su_config_1.config.chain.url;
                provider = new ethers.providers.JsonRpcProvider(rpcUrl);
                wallets = genAccounts(5, su_config_1.config.chain.mnemonic, provider);
                deployerWallet = wallets[0];
                manufacturer = wallets[1];
                note = wallets[2];
                merchant = wallets[3];
                buyer = wallets[4];
                return [4 /*yield*/, compileAndDeploy(abiDir, solDir, solcBinaryPath, provider, deployerWallet, manufacturer, 
                    // note,
                    merchant, buyer)];
            case 1:
                contracts = _a.sent();
                hardwareNotesContract = contracts.HardwareNotes;
                console.log();
                console.log('========================================');
                // manufacturer registers a batch of notes
                console.log('Registering batch 0');
                hardwareNotesContractWithManufacturer = hardwareNotesContract.connect(manufacturer);
                return [4 /*yield*/, hardwareNotesContractWithManufacturer.registerBatch(0, [note.address])];
            case 2:
                registerTx = _a.sent();
                return [4 /*yield*/, registerTx.wait()];
            case 3:
                _a.sent();
                console.log(" Manufacturer " + manufacturer.address + " registered batch [" + note.address + "] via transaction " + registerTx.hash + " ");
                console.log();
                console.log('========================================');
                // buyer deposits into note
                console.log("Depositing into note " + note.address);
                hardwareNotesContractWithBuyer = hardwareNotesContract.connect(buyer);
                return [4 /*yield*/, hardwareNotesContractWithBuyer.deposit(manufacturer.address, 0, 0, 0, deployerWallet.address, 1, 0, 1)];
            case 4:
                depositTx = _a.sent();
                return [4 /*yield*/, depositTx.wait()];
            case 5:
                _a.sent();
                console.log(" Buyer " + buyer.address + " deposited into note " + note.address + " via transaction " + depositTx.hash + " ");
                console.log();
                console.log('========================================');
                return [4 /*yield*/, provider.getBlockNumber()];
            case 6:
                blockNumber1 = _a.sent();
                return [4 /*yield*/, note.signMessage(blockNumber1.toString())];
            case 7:
                flatSig1 = _a.sent();
                sig1 = ethers.utils.splitSignature(flatSig1);
                hardwareNotesContractWithMerchant = hardwareNotesContract.connect(merchant);
                return [4 /*yield*/, hardwareNotesContractWithMerchant.signalWithdraw(0, 0, 0, blockNumber1.toString(), sig1.v, sig1.r, sig1.s)];
            case 8:
                signalWithdrawTx = _a.sent();
                return [4 /*yield*/, signalWithdrawTx.wait()];
            case 9:
                _a.sent();
                console.log(" Merchant signalled withdraw for note " + note.address + " via transaction " + signalWithdrawTx.hash);
                console.log();
                console.log('========================================');
                return [4 /*yield*/, provider.getBlockNumber()];
            case 10:
                blockNumberBefore = _a.sent();
                console.log(blockNumberBefore);
                time = 1000;
                timeTravel = function (time) { return __awaiter(void 0, void 0, void 0, function () {
                    return __generator(this, function (_a) {
                        switch (_a.label) {
                            case 0: return [4 /*yield*/, provider.send('evm_increaseTime', time)];
                            case 1:
                                _a.sent();
                                return [4 /*yield*/, provider.send('evm_mine', [])];
                            case 2:
                                _a.sent();
                                return [2 /*return*/];
                        }
                    });
                }); };
                return [4 /*yield*/, timeTravel(time)];
            case 11:
                timeTravelTx = _a.sent();
                return [4 /*yield*/, provider.getBlockNumber()];
            case 12:
                blockNumberAfter = _a.sent();
                console.log(blockNumberAfter);
                console.log();
                console.log('========================================');
                return [4 /*yield*/, provider.getBlockNumber()];
            case 13:
                blockNumber2 = _a.sent();
                return [4 /*yield*/, note.signMessage(blockNumber2.toString())];
            case 14:
                flatSig2 = _a.sent();
                sig2 = ethers.utils.splitSignature(flatSig2);
                return [4 /*yield*/, hardwareNotesContractWithMerchant.withdraw(0, 0, 0, blockNumber2.toString(), sig2.v, sig2.r, sig2.s, merchant.address)];
            case 15:
                withdrawTx = _a.sent();
                return [4 /*yield*/, withdrawTx.wait()];
            case 16:
                _a.sent();
                console.log(" Merchant called withdraw for note " + note.address + " to recipient " + merchant.address + " via transaction " + withdrawTx.hash);
                return [2 /*return*/];
        }
    });
}); };
if (require.main === module) {
    main();
}
//# sourceMappingURL=run.js.map