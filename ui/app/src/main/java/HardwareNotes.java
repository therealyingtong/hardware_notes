import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
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
import org.web3j.tuples.generated.Tuple10;
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
    public static final String BINARY = "608060405234801561001057600080fd5b506110c7806100206000396000f3fe60806040526004361061007b5760003560e01c8063a965a9411161004e578063a965a94114610210578063ab8018161461029f578063d558b11e14610307578063ecba86ad1461035f5761007b565b80630991468f146100805780631f98829e146101395780634be18f8e1461018757806355a373d6146101df575b600080fd5b34801561008c57600080fd5b50610137600480360360408110156100a357600080fd5b813591908101906040810160208201356401000000008111156100c557600080fd5b8201836020820111156100d757600080fd5b803590602001918460208302840111640100000000831117156100f957600080fd5b91908080602002602001604051908101604052809392919081815260200183836020028082843760009201919091525092955061039e945050505050565b005b610137600480360360e081101561014f57600080fd5b506001600160a01b03813581169160208101359160408201359160608101359091169060808101359060a08101359060c0013561041c565b34801561019357600080fd5b5061013760048036036101008110156101ab57600080fd5b5080359060208101359060408101359060608101359060808101359060ff60a0820135169060c08101359060e001356107f0565b3480156101eb57600080fd5b506101f4610864565b604080516001600160a01b039092168252519081900360200190f35b34801561021c57600080fd5b5061023a6004803603602081101561023357600080fd5b5035610873565b604080516001600160a01b039b8c168152602081019a909a5289810198909852606089019690965293909716608087015260a086019190915260c085015260e08401949094526101008301939093529115156101208201529051908190036101400190f35b3480156102ab57600080fd5b5061013760048036036101208110156102c357600080fd5b5080359060208101359060408101359060608101359060808101359060a081013560ff169060c08101359060e08101359061010001356001600160a01b03166109c8565b34801561031357600080fd5b50610137600480360361010081101561032b57600080fd5b5080359060208101359060408101359060608101359060808101359060ff60a0820135169060c08101359060e00135610cfa565b34801561036b57600080fd5b506101f46004803603606081101561038257600080fd5b506001600160a01b038135169060208101359060400135610f1e565b60005b815181101561041757336000908152600160209081526040808320868452909152902082518390839081106103d257fe5b60209081029190910181015182546001808201855560009485529290932090920180546001600160a01b0319166001600160a01b0390931692909217909155016103a1565b505050565b81811161045a5760405162461bcd60e51b8152600401808060200182810382526031815260200180610fea6031913960400191505060405180910390fd5b6003546001600160a01b0385166104d75783670de0b6b3a7640000023410156104c1576040805162461bcd60e51b815260206004820152601460248201527319195c1bdcda5d081a5b9cdd59999a58da595b9d60621b604482015290519081900360640190fd5b60008181526002602052604090208490556105c5565b600080546001600160a01b0319166001600160a01b0387811691909117808355604080516323b872dd60e01b815233600482015230602482015260448101899052905191909216926323b872dd92606480820193602093909283900390910190829087803b15801561054857600080fd5b505af115801561055c573d6000803e3d6000fd5b505050506040513d602081101561057257600080fd5b50516105c5576040805162461bcd60e51b815260206004820152601b60248201527f746f6b656e207472616e73666572206e6f7420617070726f7665640000000000604482015290519081900360640190fd5b6105cd610f60565b6040518061014001604052808a6001600160a01b03168152602001898152602001888152602001838152602001876001600160a01b03168152602001868152602001858152602001848152602001600081526020016000151581525090506003819080600181540180825580915050906001820390600052602060002090600a02016000909192909190915060008201518160000160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060208201518160010155604082015181600201556060820151816003015560808201518160040160006101000a8154816001600160a01b0302191690836001600160a01b0316021790555060a0820151816005015560c0820151816006015560e0820151816007015561010082015181600801556101208201518160090160006101000a81548160ff0219169083151502179055505050506000600160008b6001600160a01b03166001600160a01b0316815260200190815260200160002060008a8152602001908152602001600020888154811061076157fe5b60009182526020918290200154604080516001600160a01b038e811682529381018d90528082018c9052606081018790528a8416608082015260a081018a905260c0810189905260e08101889052905192909116925082917f4c88b1df56e0b25cb1f70f0364488365ae0468e43296863dfcef4bd508193418918190036101000190a250505050505050505050565b6108008888888888888888610cfa565b60016003878154811061080f57fe5b90600052602060002090600a020160090160006101000a81548160ff021916908315150217905550426003878154811061084557fe5b90600052602060002090600a0201600801819055505050505050505050565b6000546001600160a01b031681565b60008060008060008060008060008061088a610f60565b60038c8154811061089757fe5b90600052602060002090600a0201604051806101400160405290816000820160009054906101000a90046001600160a01b03166001600160a01b03166001600160a01b031681526020016001820154815260200160028201548152602001600382015481526020016004820160009054906101000a90046001600160a01b03166001600160a01b03166001600160a01b03168152602001600582015481526020016006820154815260200160078201548152602001600882015481526020016009820160009054906101000a900460ff1615151515815250509050806000015181602001518260400151836060015184608001518560a001518660c001518760e001518861010001518961012001519a509a509a509a509a509a509a509a509a509a50509193959799509193959799565b6109d0610f60565b600388815481106109dd57fe5b60009182526020918290206040805161014081018252600a90930290910180546001600160a01b039081168452600182015494840194909452600281015491830191909152600380820154606084015260048201549093166080830152600581015460a0830152600681015460c0830152600781015460e083015260088101546101008301526009015460ff161515610120820152815490925089908110610a8157fe5b600091825260209091206009600a90920201015460ff161515600114610ad85760405162461bcd60e51b8152600401808060200182810382526022815260200180610fc86022913960400191505060405180910390fd5b8060e0015181610100015101421115610b225760405162461bcd60e51b815260040180806020018281038252602b81526020018061101b602b913960400191505060405180910390fd5b8060c0015181610100015101421015610b82576040805162461bcd60e51b815260206004820152601b60248201527f776974686472617744656c617920686173206e6f7420656e6465640000000000604482015290519081900360640190fd5b610b928a8a8a8a8a8a8a8a610cfa565b60808101516001600160a01b0316610bfc5760a08101516040516001600160a01b03841691670de0b6b3a76400000280156108fc02916000818181858888f19350505050158015610be7573d6000803e3d6000fd5b50600088815260026020526040812055610cee565b6080810151600080546001600160a01b0319166001600160a01b039283161780825560a08401516040805163a9059cbb60e01b81528786166004820152602481019290925251919093169263a9059cbb9260448083019360209390929083900390910190829087803b158015610c7157600080fd5b505af1158015610c85573d6000803e3d6000fd5b505050506040513d6020811015610c9b57600080fd5b5051610cee576040805162461bcd60e51b815260206004820152601b60248201527f746f6b656e207472616e73666572206e6f7420617070726f7665640000000000604482015290519081900360640190fd5b50505050505050505050565b83854014610d395760405162461bcd60e51b815260040180806020018281038252602681526020018061106d6026913960400191505060405180910390fd5b610d41610f60565b60038781548110610d4e57fe5b600091825260208083206040805161014081018252600a90940290910180546001600160a01b03908116808652600180840154878701526002840154878601526003840154606088015260048401549092166080870152600583015460a0870152600683015460c0870152600783015460e0870152600883015461010087015260099092015460ff16151561012086015290855282528084208d855290915282208054919350908a908110610dff57fe5b6000918252602080832090910154604080517f19457468657265756d205369676e6564204d6573736167653a0a33320000000081850152603c8082018c905282518083039091018152605c8201808452815191860191909120959052607c810180835285905260ff8a16609c82015260bc810189905260dc810188905290516001600160a01b03909216945060019260fc808301939192601f198301929081900390910190855afa158015610eb8573d6000803e3d6000fd5b505050602060405103516001600160a01b0316826001600160a01b031614610f115760405162461bcd60e51b81526004018080602001828103825260278152602001806110466027913960400191505060405180910390fd5b5050505050505050505050565b60016020528260005260406000206020528160005260406000208181548110610f4357fe5b6000918252602090912001546001600160a01b0316925083915050565b60405180610140016040528060006001600160a01b0316815260200160008152602001600081526020016000815260200160006001600160a01b0316815260200160008152602001600081526020016000815260200160008152602001600015158152509056fe7369676e616c576974686472617720686173206e6f74206265656e2063616c6c6564776974686472617754696d656f7574206d757374206265206c6172676572207468616e20776974686472617744656c61797769746864726177206861732074696d6564206f75742073696e6365207369676e616c57697468647261776f6e6c7920747275737465642068617264776172652063616e207369676e207769746864726177696e76616c696420626c6f636b20286e6f7420696e206c6173742032353620626c6f636b7329a265627a7a72315820da96f17c86b7526b5a50a2af57a8571d08c010229bde5a26f0da5644221b10ed64736f6c634300050d0032";

    public static final String FUNC_CHECKSIG = "checkSig";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_GETNOTE = "getNote";

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

    public RemoteFunctionCall<Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>> getNote(BigInteger _noteId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_noteId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue(), 
                                (BigInteger) results.get(8).getValue(), 
                                (Boolean) results.get(9).getValue());
                    }
                });
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
