package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.bouncycastle.util.encoders.Hex;

import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple10;

import java.math.BigInteger;

import javax.annotation.Nullable;

import im.status.keycard.android.NFCCardManager;
import im.status.keycard.applet.CashApplicationInfo;
import im.status.keycard.applet.CashCommandSet;
import im.status.keycard.io.CardListener;
import im.status.keycard.io.CardChannel;


public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private NFCCardManager cardManager;
    PendingIntent mPendingIntent;
	Tag tag;

	public static byte[] pubKey;
	public static String noteAddress;
	public static String manufacturerAddress;
    public static String batchId;
    public static String hardwareId;
    public static String noteId;
    public static String token;
    public static String amount;
    public static String withdrawDelay;
    public static String withdrawTimeout;
    public static String withdrawStart;
    public static String isInFlight;

    public static String currentBlock = "";

    public static final String contract = "0xa2ff8dAEf58467b2Ac3c93c955449EE1342F6F9E";
	public static final int startBlock = 16685179;
//	public static final String provider = "https://kovan.infura.io/v3/1bef5b4350a648c7a9439ea7bc9f8846";
    public static final String provider = "https://kovan.poa.network/";
	public static final String adminPrivKey = "0x7e84cb2db4e2019719853616233be0f3fed271a8f4668534d485348dbd333424";

    public static final Credentials credentials = Credentials.create(adminPrivKey);

    public static final Web3j web3j = Web3j.build(new HttpService(provider));
    public static final HardwareNotes hardwareNotes = HardwareNotes.load(
            contract, web3j, credentials, BigInteger.valueOf(100000), BigInteger.valueOf(100000));


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        Log.i("onCreate", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView2);
        tv.append(currentBlock);

        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }

        // Get the Android NFC default adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Create the NFCCardManager, this class is provided by the Keycard SDK and handles connections to the card
        cardManager = new NFCCardManager();

        // The Card Listener receives the connected/disconnected events. These can happen at any time since the user can
        // introduce or remove the card to/from the field at any time. This is where your code goes.
        cardManager.setCardListener(new CardListener() {
//            @Override
            public void onConnected(CardChannel cardChannel) {

                Log.i("onConnected", "onConnected");

				CashCommandSet cashCmdSet = new CashCommandSet(cardChannel);


                try {

                    CashApplicationInfo info = new CashApplicationInfo(cashCmdSet.select().checkOK().getData());
                    pubKey = info.getPubKey();
                    setAddress(pubKey);
                    getDepositEvent(noteAddress);
                    getNote(Integer.parseInt(noteId));
                    getCurrentBlock();

//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            getDepositEvent(noteAddress);
//                        }
//                     }, 3000);

                } catch (Exception IOException) {

                }

            }

//            @Override
            public void onDisconnected() {
                // Card is disconnected (was removed from the field). You can perform cleanup here.
            }
        });
       cardManager.start();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onResume() {
        super.onResume();

        // We need to enable the reader on resume.
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, this.cardManager, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
//            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }

        TextView tv = findViewById(R.id.textView2);
        tv.setText("your last sync was at block: " + currentBlock);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onPause() {
        super.onPause();

        // We disable the reader on pause to allow other apps to use it.
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
//            nfcAdapter.disableForegroundDispatch(this);
        }

    }



    public static void setAddress(byte[] pubKey) throws Exception {

        byte[] addressBytes = Keys.getAddress(pubKey);
        noteAddress = Hex.toHexString(addressBytes);
        Log.i("getAddress", noteAddress);

    }



    public void getDepositEvent(String noteAddress) throws Exception {

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if (connected){
            Log.i("getDepositEvent", "getDepositEvent");

            EthFilter eventFilter = new EthFilter(
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlock)), // filter: from block
                    DefaultBlockParameter.valueOf("latest"), // filter: to block
                    contract // filter: smart contract address
            );
            String DEPOSIT_EVENT_HASH = EventEncoder.encode(hardwareNotes.DEPOSIT_EVENT);

            eventFilter.addSingleTopic(DEPOSIT_EVENT_HASH); // filter: event type (topic[0])
            eventFilter.addOptionalTopics("0x"+ Strings.padStart(noteAddress, 64, '0'));

            web3j.ethLogFlowable(eventFilter).subscribe(log -> {
                String eventHash = log.getTopics().get(0);
                Log.i("eventHash", eventHash);

                String depositData = log.getData();
                Log.i("depositData", depositData);

                parseDepositData(depositData);

                saveToPreferences(noteId, depositData);

            });
        }
        else {

            String depositData = readFromPreferences(noteAddress);
            parseDepositData(depositData);

        }


    }

    public static void getNote(int noteId) throws Exception {
        Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> result = hardwareNotes.getNote(BigInteger.valueOf(noteId)).send();
        withdrawStart = String.valueOf(result.getValue9());
        isInFlight = String.valueOf(result.getValue10());
    }

    public void clickNoteInfo(View view) throws Exception {
        Intent intent = new Intent(this, DisplayNoteActivity.class);
        startActivity(intent);
    }

    public static void getCurrentBlock() throws Exception {
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        currentBlock = String.valueOf(block.getNumber());
    }

    public void clickHardwareNotes(View view){

        Intent intent = new Intent(this, DisplaySourceActivity.class);
        startActivity(intent);

    }


    public void clickSyncState(View view){
//        syncDepositEvents();
        Intent intent = new Intent(this, SyncStateActivity.class);
        startActivity(intent);
    }

    public void syncDepositEvents(){
        Log.i("syncDepositEvents", "syncDepositEvents");

        EthFilter eventFilter = new EthFilter(
                DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlock)), // filter: from block
                DefaultBlockParameter.valueOf("latest"), // filter: to block
                contract // filter: smart contract address
        );
        String DEPOSIT_EVENT_HASH = EventEncoder.encode(hardwareNotes.DEPOSIT_EVENT);

        eventFilter.addSingleTopic(DEPOSIT_EVENT_HASH); // filter: event type (topic[0])

        web3j.ethLogFlowable(eventFilter).subscribe(log -> {
            String eventHash = log.getTopics().get(0);
            Log.i("eventHash", eventHash);

            String depositData = log.getData();
            Log.i("depositData", depositData);

            String _noteId = getNoteIdFromDepositData(depositData);

            saveToPreferences(_noteId, depositData);

        });
    }

    public void saveToPreferences(String prefName, String prefValue){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }

    public String readFromPreferences(String prefName){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        return pref.getString(prefName, null);
    }

    public void parseDepositData(String depositData){
        String[] tokens =
                Iterables.toArray(
                        Splitter
                                .fixedLength(64)
                                .split(depositData.substring(2)),
                        String.class
                );
        for (String string : tokens) Log.i(string, string);
        manufacturerAddress = "0x" + tokens[0].substring(24);
        batchId = String.valueOf(Long.parseLong(tokens[1],16));
        hardwareId = String.valueOf(Long.parseLong(tokens[2],16));
        noteId = Integer.toHexString(Integer.parseInt(tokens[3]));
        token = "0x" + tokens[4].substring(24);
        amount = Integer.toHexString(Integer.parseInt(tokens[5]));
        withdrawDelay = String.valueOf(Long.parseLong(tokens[6],16));
        withdrawTimeout = String.valueOf(Long.parseLong(tokens[7], 16));
    }

    public String getNoteIdFromDepositData(String depositData){
        String[] tokens =
                Iterables.toArray(
                        Splitter
                                .fixedLength(64)
                                .split(depositData.substring(2)),
                        String.class
                );
        return Integer.toHexString(Integer.parseInt(tokens[3]));
    }

}
