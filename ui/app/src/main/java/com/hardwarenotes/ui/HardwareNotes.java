package com.hardwarenotes.ui;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.14.
 */
@SuppressWarnings("rawtypes")
public class HardwareNotes extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610ed8806100206000396000f3fe6080604052600436106100705760003560e01c806355a373d61161004e57806355a373d6146101d4578063ab80181614610205578063d558b11e1461026d578063ecba86ad146102c557610070565b80630991468f146100755780631f98829e1461012e5780634be18f8e1461017c575b600080fd5b34801561008157600080fd5b5061012c6004803603604081101561009857600080fd5b813591908101906040810160208201356401000000008111156100ba57600080fd5b8201836020820111156100cc57600080fd5b803590602001918460208302840111640100000000831117156100ee57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610304945050505050565b005b61012c600480360360e081101561014457600080fd5b506001600160a01b03813581169160208101359160408201359160608101359091169060808101359060a08101359060c00135610382565b34801561018857600080fd5b5061012c60048036036101008110156101a057600080fd5b5080359060208101359060408101359060608101359060808101359060ff60a0820135169060c08101359060e00135610756565b3480156101e057600080fd5b506101e96107ca565b604080516001600160a01b039092168252519081900360200190f35b34801561021157600080fd5b5061012c600480360361012081101561022957600080fd5b5080359060208101359060408101359060608101359060808101359060a081013560ff169060c08101359060e08101359061010001356001600160a01b03166107d9565b34801561027957600080fd5b5061012c600480360361010081101561029157600080fd5b5080359060208101359060408101359060608101359060808101359060ff60a0820135169060c08101359060e00135610b0b565b3480156102d157600080fd5b506101e9600480360360608110156102e857600080fd5b506001600160a01b038135169060208101359060400135610d2f565b60005b815181101561037d573360009081526001602090815260408083208684529091529020825183908390811061033857fe5b60209081029190910181015182546001808201855560009485529290932090920180546001600160a01b0319166001600160a01b039093169290921790915501610307565b505050565b8181116103c05760405162461bcd60e51b8152600401808060200182810382526031815260200180610dfb6031913960400191505060405180910390fd5b6003546001600160a01b03851661043d5783670de0b6b3a764000002341015610427576040805162461bcd60e51b815260206004820152601460248201527319195c1bdcda5d081a5b9cdd59999a58da595b9d60621b604482015290519081900360640190fd5b600081815260026020526040902084905561052b565b600080546001600160a01b0319166001600160a01b0387811691909117808355604080516323b872dd60e01b815233600482015230602482015260448101899052905191909216926323b872dd92606480820193602093909283900390910190829087803b1580156104ae57600080fd5b505af11580156104c2573d6000803e3d6000fd5b505050506040513d60208110156104d857600080fd5b505161052b576040805162461bcd60e51b815260206004820152601b60248201527f746f6b656e207472616e73666572206e6f7420617070726f7665640000000000604482015290519081900360640190fd5b610533610d71565b6040518061014001604052808a6001600160a01b03168152602001898152602001888152602001838152602001876001600160a01b03168152602001868152602001858152602001848152602001600081526020016000151581525090506003819080600181540180825580915050906001820390600052602060002090600a02016000909192909190915060008201518160000160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060208201518160010155604082015181600201556060820151816003015560808201518160040160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060a0820151816005015560c0820151816006015560e0820151816007015561010082015181600801556101208201518160090160006101000a81548160ff0219169083151502179055505050506000600160008b6001600160a01b03166001600160a01b0316815260200190815260200160002060008a815260200190815260200160002088815481106106c757fe5b60009182526020918290200154604080516001600160a01b038e811682529381018d90528082018c9052606081018790528a8416608082015260a081018a905260c0810189905260e08101889052905192909116925082917f4c88b1df56e0b25cb1f70f0364488365ae0468e43296863dfcef4bd508193418918190036101000190a250505050505050505050565b6107668888888888888888610b0b565b60016003878154811061077557fe5b90600052602060002090600a020160090160006101000a81548160ff02191690831515021790555042600387815481106107ab57fe5b90600052602060002090600a0201600801819055505050505050505050565b6000546001600160a01b031681565b6107e1610d71565b600388815481106107ee57fe5b60009182526020918290206040805161014081018252600a90930290910180546001600160a01b039081168452600182015494840194909452600281015491830191909152600380820154606084015260048201549093166080830152600581015460a0830152600681015460c0830152600781015460e083015260088101546101008301526009015460ff16151561012082015281549092508990811061089257fe5b600091825260209091206009600a90920201015460ff1615156001146108e95760405162461bcd60e51b8152600401808060200182810382526022815260200180610dd96022913960400191505060405180910390fd5b8060e00151816101000151014211156109335760405162461bcd60e51b815260040180806020018281038252602b815260200180610e2c602b913960400191505060405180910390fd5b8060c0015181610100015101421015610993576040805162461bcd60e51b815260206004820152601b60248201527f776974686472617744656c617920686173206e6f7420656e6465640000000000604482015290519081900360640190fd5b6109a38a8a8a8a8a8a8a8a610b0b565b60808101516001600160a01b0316610a0d5760a08101516040516001600160a01b03841691670de0b6b3a76400000280156108fc02916000818181858888f193505050501580156109f8573d6000803e3d6000fd5b50600088815260026020526040812055610aff565b6080810151600080546001600160a01b0319166001600160a01b039283161780825560a08401516040805163a9059cbb60e01b81528786166004820152602481019290925251919093169263a9059cbb9260448083019360209390929083900390910190829087803b158015610a8257600080fd5b505af1158015610a96573d6000803e3d6000fd5b505050506040513d6020811015610aac57600080fd5b5051610aff576040805162461bcd60e51b815260206004820152601b60248201527f746f6b656e207472616e73666572206e6f7420617070726f7665640000000000604482015290519081900360640190fd5b50505050505050505050565b83854014610b4a5760405162461bcd60e51b8152600401808060200182810382526026815260200180610e7e6026913960400191505060405180910390fd5b610b52610d71565b60038781548110610b5f57fe5b600091825260208083206040805161014081018252600a90940290910180546001600160a01b03908116808652600180840154878701526002840154878601526003840154606088015260048401549092166080870152600583015460a0870152600683015460c0870152600783015460e0870152600883015461010087015260099092015460ff16151561012086015290855282528084208d855290915282208054919350908a908110610c1057fe5b6000918252602080832090910154604080517f19457468657265756d205369676e6564204d6573736167653a0a33320000000081850152603c8082018c905282518083039091018152605c8201808452815191860191909120959052607c810180835285905260ff8a16609c82015260bc810189905260dc810188905290516001600160a01b03909216945060019260fc808301939192601f198301929081900390910190855afa158015610cc9573d6000803e3d6000fd5b505050602060405103516001600160a01b0316826001600160a01b031614610d225760405162461bcd60e51b8152600401808060200182810382526027815260200180610e576027913960400191505060405180910390fd5b5050505050505050505050565b60016020528260005260406000206020528160005260406000208181548110610d5457fe5b6000918252602090912001546001600160a01b0316925083915050565b60405180610140016040528060006001600160a01b0316815260200160008152602001600081526020016000815260200160006001600160a01b0316815260200160008152602001600081526020016000815260200160008152602001600015158152509056fe7369676e616c576974686472617720686173206e6f74206265656e2063616c6c6564776974686472617754696d656f7574206d757374206265206c6172676572207468616e20776974686472617744656c61797769746864726177206861732074696d6564206f75742073696e6365207369676e616c57697468647261776f6e6c7920747275737465642068617264776172652063616e207369676e207769746864726177696e76616c696420626c6f636b20286e6f7420696e206c6173742032353620626c6f636b7329a265627a7a72315820f91eb30949eed60401c922cabba34282d9447b626b8300916aa169cc961e188464736f6c634300050d0032";

    public static final String FUNC_CHECKSIG = "checkSig";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_HARDWARE = "hardware";

    public static final String FUNC_REGISTERBATCH = "registerBatch";

    public static final String FUNC_SIGNALWITHDRAW = "signalWithdraw";

    public static final String FUNC_TOKENCONTRACT = "tokenContract";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final Event DEPOSIT_EVENT = new Event("Deposit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected HardwareNotes(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected HardwareNotes(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected HardwareNotes(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected HardwareNotes(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<DepositEventResponse> getDepositEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DEPOSIT_EVENT, transactionReceipt);
        ArrayList<DepositEventResponse> responses = new ArrayList<DepositEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DepositEventResponse typedResponse = new DepositEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._noteAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.manufacturer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.batchId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.hardwareId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.noteId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.withdrawDelay = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.withdrawTimeout = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DepositEventResponse> depositEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DepositEventResponse>() {
            @Override
            public DepositEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DEPOSIT_EVENT, log);
                DepositEventResponse typedResponse = new DepositEventResponse();
                typedResponse.log = log;
                typedResponse._noteAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.manufacturer = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.batchId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.hardwareId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.noteId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.token = (String) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse.withdrawDelay = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
                typedResponse.withdrawTimeout = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DepositEventResponse> depositEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DEPOSIT_EVENT));
        return depositEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> deposit(String manufacturer, BigInteger batchId, BigInteger hardwareId, String token, BigInteger amount, BigInteger withdrawDelay, BigInteger withdrawTimeout, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, manufacturer), 
                new org.web3j.abi.datatypes.generated.Uint256(batchId), 
                new org.web3j.abi.datatypes.generated.Uint256(hardwareId), 
                new org.web3j.abi.datatypes.Address(160, token), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.generated.Uint256(withdrawDelay), 
                new org.web3j.abi.datatypes.generated.Uint256(withdrawTimeout)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<String> hardware(String param0, BigInteger param1, BigInteger param2) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HARDWARE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1), 
                new org.web3j.abi.datatypes.generated.Uint256(param2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> registerBatch(BigInteger batch, List<String> pubKeys) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REGISTERBATCH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(batch), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(pubKeys, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> signalWithdraw(BigInteger batchId, BigInteger hardwareId, BigInteger noteId, BigInteger blockNum, byte[] blockHash, BigInteger v, byte[] r, byte[] s) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SIGNALWITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(batchId), 
                new org.web3j.abi.datatypes.generated.Uint256(hardwareId), 
                new org.web3j.abi.datatypes.generated.Uint256(noteId), 
                new org.web3j.abi.datatypes.generated.Uint256(blockNum), 
                new org.web3j.abi.datatypes.generated.Bytes32(blockHash), 
                new org.web3j.abi.datatypes.generated.Uint8(v), 
                new org.web3j.abi.datatypes.generated.Bytes32(r), 
                new org.web3j.abi.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> tokenContract() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKENCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw(BigInteger batchId, BigInteger hardwareId, BigInteger noteId, BigInteger blockNum, byte[] blockHash, BigInteger v, byte[] r, byte[] s, String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(batchId), 
                new org.web3j.abi.datatypes.generated.Uint256(hardwareId), 
                new org.web3j.abi.datatypes.generated.Uint256(noteId), 
                new org.web3j.abi.datatypes.generated.Uint256(blockNum), 
                new org.web3j.abi.datatypes.generated.Bytes32(blockHash), 
                new org.web3j.abi.datatypes.generated.Uint8(v), 
                new org.web3j.abi.datatypes.generated.Bytes32(r), 
                new org.web3j.abi.datatypes.generated.Bytes32(s), 
                new org.web3j.abi.datatypes.Address(160, recipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static HardwareNotes load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new HardwareNotes(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static HardwareNotes load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HardwareNotes(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static HardwareNotes load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new HardwareNotes(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static HardwareNotes load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new HardwareNotes(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<HardwareNotes> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(HardwareNotes.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<HardwareNotes> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HardwareNotes.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<HardwareNotes> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(HardwareNotes.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<HardwareNotes> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HardwareNotes.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class DepositEventResponse extends BaseEventResponse {
        public String _noteAddress;

        public String manufacturer;

        public BigInteger batchId;

        public BigInteger hardwareId;

        public BigInteger noteId;

        public String token;

        public BigInteger amount;

        public BigInteger withdrawDelay;

        public BigInteger withdrawTimeout;
    }
}
