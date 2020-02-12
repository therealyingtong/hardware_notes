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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Strings;

import org.bouncycastle.util.encoders.Hex;

import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple10;

import java.math.BigInteger;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import im.status.keycard.android.NFCCardManager;
import im.status.keycard.applet.CashApplicationInfo;
import im.status.keycard.applet.CashCommandSet;
import im.status.keycard.io.CardListener;
import im.status.keycard.io.CardChannel;
import io.reactivex.disposables.Disposable;

import static com.hardwarenotes.ui.Helpers.parseDepositData;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private NFCCardManager cardManager;
    PendingIntent mPendingIntent;
    Tag tag;

    public static byte[] pubKey;
    public static String noteAddress;

    public static final String contract = "0xa2ff8dAEf58467b2Ac3c93c955449EE1342F6F9E";
    public static final int startBlock = 16685179;
    //	public static final String provider = "https://kovan.infura.io/v3/1bef5b4350a648c7a9439ea7bc9f8846";
    public static final String adminPrivKey = "0x7e84cb2db4e2019719853616233be0f3fed271a8f4668534d485348dbd333424";

    public static final Credentials credentials = Credentials.create(adminPrivKey);

    public static final String provider = "https://kovan.poa.network/";
    public static final Web3j web3j = Web3j.build(new HttpService(provider));
    public static final HardwareNotes hardwareNotes = HardwareNotes.load(
            contract, web3j, credentials, BigInteger.valueOf(100000), BigInteger.valueOf(100000));



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i("onCreate", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView2);

        getCurrentBlock();

        String currentBlock = readFromPreferences("currentBlock");
        if (currentBlock != null) tv.append(currentBlock);

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
        String currentBlock = readFromPreferences("currentBlock");
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


    public void clickNoteInfo(View view) {

        if (noteAddress != null){
            try {
                view.setEnabled(false);
                boolean connected;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;

                int delay = 0;

                if (connected){
                    getDepositEventOnline(noteAddress);
                    delay = 4000;

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(MainActivity.this, DisplayNoteActivity.class);
                        MainActivity.this.startActivity(intent);
//                MainActivity.this.finish();
                        view.setEnabled(true);
                    }
                }, delay);
            } catch (Exception exception){
                view.setEnabled(true);
            }
        }


    }


    public void clickHardwareNotes(View view){
        view.setEnabled(false);
        Intent intent = new Intent(this, DisplaySourceActivity.class);
        startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);


    }


    public void clickSyncState(View view) {
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
            view.setEnabled(false);

            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    syncDepositEvents();
                }
            });
            thread.start();
//            thread.interrupt();
            syncNotes();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 30000);

        } else {

        }



//        Intent intent = new Intent(this, SyncStateActivity.class);
//        startActivity(intent);
    }


    public void syncDepositEvents(){
        Log.i("syncDepositEvents", "syncDepositEvents");

        getCurrentBlock();

        EthFilter eventFilter = new EthFilter(
                DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlock)), // filter: from block
                DefaultBlockParameter.valueOf("latest"), // filter: to block
                contract // filter: smart contract address
        );
        String DEPOSIT_EVENT_HASH = EventEncoder.encode(hardwareNotes.DEPOSIT_EVENT);

        eventFilter.addSingleTopic(DEPOSIT_EVENT_HASH); // filter: event type (topic[0])

        Logger logger = Logger.getLogger("com.hardwarenotes.ui");
        Disposable disposable = web3j.ethLogFlowable(eventFilter).subscribe(log -> {

            String eventHash = log.getTopics().get(0);
            Log.i("eventHash", eventHash);

            String _noteAddress = log.getTopics().get(1).substring(26);
            Log.i("_noteAddress", _noteAddress);

            String blockNumber = log.getBlockNumberRaw();
            Log.i("eventBlockNumber", blockNumber);

            String depositData = log.getData().concat(blockNumber);
            Log.i("depositData", depositData);

            saveToPreferences(_noteAddress, depositData);
            Log.i("saved _noteAddress", _noteAddress);

        }, error -> logger.log(Level.SEVERE, "error", error));

        disposable.dispose();

    }

    public void syncNotes(){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                boolean noMoreNotes = false;

                int i = 0;

                while (!noMoreNotes){
                    try{
                        getNote(i);
                        i++;

                    } catch (Exception exception){
                        noMoreNotes = true;
                    }
                }            }
        });
        thread.start();

    }


    public void getDepositEventOnline(String noteAddress) {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                Log.i("getDepositEvent", "getDepositEvent");

                EthFilter eventFilter = new EthFilter(
                        DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlock)), // filter: from block
                        DefaultBlockParameter.valueOf("latest"), // filter: to block
                        contract // filter: smart contract address
                );
                String DEPOSIT_EVENT_HASH = EventEncoder.encode(hardwareNotes.DEPOSIT_EVENT);

                eventFilter.addSingleTopic(DEPOSIT_EVENT_HASH); // filter: event type (topic[0])
                eventFilter.addOptionalTopics("0x" + Strings.padStart(noteAddress, 64, '0'));

                Logger logger = Logger.getLogger("com.hardwarenotes.ui");

                Disposable disposable = web3j.ethLogFlowable(eventFilter).subscribe(log -> {
                    String eventHash = log.getTopics().get(0);
                    Log.i("eventHash", eventHash);

                    String blockNumber = log.getBlockNumberRaw();
                    Log.i("eventBlockNumber", blockNumber);

                    String depositData = log.getData().concat(blockNumber);
                    Log.i("depositData", depositData);

                    Map<String,String> depositDataMap = parseDepositData(depositData);

                    saveToPreferences(noteAddress, depositData);

                    String noteId = depositDataMap.get("noteId");
                    getNote(Integer.parseInt(noteId));
                    getCurrentBlock();

                }, error -> logger.log(Level.SEVERE, "error", error));
                disposable.dispose();

            }
        });
        thread.start();
//        thread.interrupt();

    }


    public void getNote(int noteId) {

        try{
            Tuple10<String, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> result = hardwareNotes.getNote(BigInteger.valueOf(noteId)).send();
            String withdrawStart = String.valueOf(result.getValue9());
            String isInFlight = String.valueOf(result.getValue10());

            saveToPreferences(noteId+"withdrawStart", withdrawStart);
            Log.i("saved withdrawStart", noteId+withdrawStart);

            saveToPreferences(noteId+"isInFlight", isInFlight);
            Log.i("saved isInFlight", noteId+isInFlight);

        } catch (Exception exception) {

        }

    }


    public void getCurrentBlock() {
        try {
            EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
            String currentBlock = String.valueOf(block.getNumber());
            saveToPreferences("currentBlock", currentBlock);
        } catch (Exception exception){
        }

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







}

