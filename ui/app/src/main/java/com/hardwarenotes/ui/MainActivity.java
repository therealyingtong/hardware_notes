package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.nio.file.Files;
import java.security.interfaces.ECPublicKey;
import java.util.Scanner;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import im.status.keycard.android.NFCCardManager;
import im.status.keycard.applet.ApplicationInfo;
import im.status.keycard.applet.CashApplicationInfo;
import im.status.keycard.applet.KeycardCommandSet;
import im.status.keycard.applet.CashCommandSet;
import im.status.keycard.io.CardListener;
import im.status.keycard.io.CardChannel;


public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private NFCCardManager cardManager;
    PendingIntent mPendingIntent;
	Tag tag;
	public static byte[] pubKey;
    public static String pubKeyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
				// KeycardCommandSet cmdSet = new KeycardCommandSet(cardChannel);

				CashCommandSet cashCmdSet = new CashCommandSet(cardChannel);

//                NdefMessage msg = new NdefMessage(
//                    new NdefRecord[] {
//                            NdefRecord.createApplicationRecord("com.hardwarenotes.ui")
//                    }
//                );

//                Ndef ndef = Ndef.get(tag);

                try {

//                    ndef.writeNdefMessage(msg);

                    CashApplicationInfo info = new CashApplicationInfo(cashCmdSet.select().checkOK().getData());
                    pubKey = info.getPubKey();

                    setPubKeyString(pubKey);
                    Log.i("pubKeyString",pubKeyString);

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

    public static void setPubKeyString(byte[] pubKey) throws Exception {

        pubKeyString = Base64.encodeToString(pubKey, Base64.DEFAULT);
//        pubKeyString = "dummyPubKeyString";

    }

    public void clickNoteInfo(View view) throws Exception {
        setContentView(R.layout.activity_display_pubkey);
        TextView tv = (TextView) findViewById(R.id.textview);
//        setPubKeyString(pubKey);
        tv.setText(pubKeyString);
    }

    public void clickHardwareNotes(View view){
        setContentView(R.layout.activity_display_source);
        WebView wv = (WebView) findViewById(R.id.webview);

        String customHtml = "<html><pre style='color:black;background:white;'>\n" +
                "contract ERC20Interface <i>{</i>\n" +
                "    <b>function</b> transferFrom<i>(</i>address from<i>,</i> address to<i>,</i> uint256 value<i>)</i> <b>public</b> returns<i>(</i>bool<i>)</i> <i>{</i><i>}</i>\n" +
                "\t<b>function</b> transfer<i>(</i>address recipient<i>,</i> uint value<i>)</i> <b>public</b> returns <i>(</i>bool<i>)</i> <i>{</i><i>}</i>\n" +
                "<i>}</i>\n" +
                "\n" +
                "contract HardwareNotes <i>{</i>\n" +
                "\n" +
                "\tERC20Interface <b>public</b> tokenContract<i>;</i>\n" +
                "\n" +
                "\tmapping <i>(</i>address <i>=</i><i>></i> mapping<i>(</i>uint256 <i>=</i><i>></i> address<i>[</i><i>]</i><i>)</i><i>)</i> <b>public</b> hardware<i>;</i>\n" +
                "\tmapping <i>(</i>uint256 <i>=</i><i>></i> uint256<i>)</i> ETHBalance<i>;</i> <i>// map noteId to value</i>\n" +
                "\n" +
                "\tstruct note <i>{</i>\n" +
                "\t\taddress manufacturer<i>;</i>\n" +
                "\t\tuint batchId<i>;</i>\n" +
                "\t\tuint hardwareId<i>;</i>\n" +
                "\t\tuint noteId<i>;</i>\n" +
                "\t\taddress token<i>;</i>\n" +
                "\t\tuint amount<i>;</i>\n" +
                "\t\tuint withdrawDelay<i>;</i>\n" +
                "\t\tuint withdrawTimeout<i>;</i>\n" +
                "\t\tuint withdrawStart<i>;</i>\n" +
                "\t\tbool isInFlight<i>;</i>\n" +
                "\t<i>}</i>\n" +
                "\n" +
                "\tnote<i>[</i><i>]</i> notes<i>;</i>\n" +
                "\n" +
                "\t<b>function</b> registerBatch<i>(</i>uint256 batch<i>,</i> address<i>[</i><i>]</i> memory pubKeys<i>)</i> <b>public</b> <i>{</i>\n" +
                "\n" +
                "\t\tuint i<i>;</i>\n" +
                "\t\t<b>for</b> <i>(</i>i <i>=</i> 0<i>;</i> i <i>&lt;</i> pubKeys<i>.</i><b>length</b><i>;</i> i<i>++</i><i>)</i><i>{</i>\n" +
                "\t\t\thardware<i>[</i>msg<i>.</i>sender<i>]</i><i>[</i>batch<i>]</i><i>.</i>push<i>(</i>pubKeys<i>[</i>i<i>]</i><i>)</i><i>;</i>\n" +
                "\t\t<i>}</i>\n" +
                "\t<i>}</i>\n" +
                "\n" +
                "\t<b>function</b> deposit<i>(</i>\n" +
                "\t\taddress manufacturer<i>,</i>\n" +
                "\t\tuint batchId<i>,</i>\n" +
                "\t\tuint hardwareId<i>,</i>\n" +
                "\t\tuint noteId<i>,</i>\n" +
                "\t\taddress token<i>,</i>\n" +
                "\t\tuint amount<i>,</i>\n" +
                "\t\tuint withdrawDelay<i>,</i>\n" +
                "\t\tuint withdrawTimeout<i>)</i>\n" +
                "\t<b>public</b> payable <i>{</i>\n" +
                "\n" +
                "\t\trequire<i>(</i>withdrawTimeout <i>></i> withdrawDelay<i>,</i> <b>\"</b><b>withdrawTimeout must be larger than withdrawDelay</b><b>\"</b><i>)</i><i>;</i>\n" +
                "\n" +
                "\t\t<i>// ETH deposit</i>\n" +
                "\t\t<b>if</b> <i>(</i>token <i>==</i> address<i>(</i>0<i>)</i><i>)</i><i>{</i>\n" +
                "\t\t\trequire<i>(</i>msg<i>.</i>value <i>>=</i> amount <i>*</i> 1 ether<i>,</i> <b>\"</b><b>deposit insufficient</b><b>\"</b><i>)</i><i>;</i>\n" +
                "\t\t\tETHBalance<i>[</i>noteId<i>]</i> <i>=</i> amount<i>;</i>\n" +
                "\t\t<i>// non-ETH deposit</i>\n" +
                "\t\t<i>}</i> <b>else</b> <i>{</i>\n" +
                "\t\t\ttokenContract <i>=</i> ERC20Interface<i>(</i>token<i>)</i><i>;</i>\n" +
                "\t\t\trequire<i>(</i>\n" +
                "\t\t\t\ttokenContract<i>.</i>transferFrom<i>(</i>msg<i>.</i>sender<i>,</i> address<i>(</i><b>this</b><i>)</i><i>,</i> amount<i>)</i><i>,</i>\n" +
                "\t\t\t\t<b>\"</b><b>token transfer not approved</b><b>\"</b>\n" +
                "\t\t\t<i>)</i><i>;</i>\n" +
                "\t\t<i>}</i>\n" +
                "\n" +
                "\t\tnote memory newNote <i>=</i> note<i>(</i>\n" +
                "\t\t\tmanufacturer<i>,</i> batchId<i>,</i> hardwareId<i>,</i> noteId<i>,</i>\n" +
                "\t\t\ttoken<i>,</i> amount<i>,</i>\n" +
                "\t\t\twithdrawDelay<i>,</i> withdrawTimeout<i>,</i> 0<i>,</i> <b>false</b>\n" +
                "\t\t<i>)</i><i>;</i>\n" +
                "\t\tnotes<i>.</i>push<i>(</i>newNote<i>)</i><i>;</i>\n" +
                "\t<i>}</i>\n" +
                "\n" +
                "\t<b>function</b> signalWithdraw<i>(</i>\n" +
                "\t\tuint batchId<i>,</i>\n" +
                "\t\tuint hardwareId<i>,</i>\n" +
                "\t\tuint noteId<i>,</i>\n" +
                "\t\tuint blockNum<i>,</i>\n" +
                "\t\tbytes32 blockHash<i>,</i>\n" +
                "\t\tuint8 v<i>,</i>\n" +
                "\t\tbytes32 r<i>,</i>\n" +
                "\t\tbytes32 s<i>)</i>\n" +
                "\t<b>public</b> <i>{</i>\n" +
                "\t\tcheckSig<i>(</i>batchId<i>,</i> hardwareId<i>,</i> noteId<i>,</i> blockNum<i>,</i>blockHash<i>,</i> v<i>,</i> r<i>,</i> s<i>)</i><i>;</i>\n" +
                "\t\tnotes<i>[</i>noteId<i>]</i><i>.</i>isInFlight <i>=</i> <b>true</b><i>;</i>\n" +
                "\t\tnotes<i>[</i>noteId<i>]</i><i>.</i>withdrawStart <i>=</i> block<i>.</i>timestamp<i>;</i>\n" +
                "\t<i>}</i>\n" +
                "\n" +
                "\t<b>function</b> withdraw<i>(</i>\n" +
                "\t\tuint batchId<i>,</i>\n" +
                "\t\tuint hardwareId<i>,</i>\n" +
                "\t\tuint noteId<i>,</i>\n" +
                "\t\tuint blockNum<i>,</i>\n" +
                "\t\tbytes32 blockHash<i>,</i>\n" +
                "\t\tuint8 v<i>,</i>\n" +
                "\t\tbytes32 r<i>,</i>\n" +
                "\t\tbytes32 s<i>,</i>\n" +
                "\t\taddress payable recipient<i>)</i>\n" +
                "\t<b>public</b> <i>{</i>\n" +
                "\n" +
                "\t\tnote memory _note <i>=</i> notes<i>[</i>noteId<i>]</i><i>;</i>\n" +
                "\t\trequire<i>(</i>notes<i>[</i>noteId<i>]</i><i>.</i>isInFlight <i>==</i> <b>true</b><i>,</i> <b>\"</b><b>signalWithdraw has not been called</b><b>\"</b><i>)</i><i>;</i>\n" +
                "\t\trequire<i>(</i>block<i>.</i>timestamp <i>&lt;=</i> _note<i>.</i>withdrawStart <i>+</i> _note<i>.</i>withdrawTimeout<i>,</i> <b>\"</b><b>withdraw has timed out since signalWithdraw</b><b>\"</b><i>)</i><i>;</i>\n" +
                "\t\trequire<i>(</i>block<i>.</i>timestamp <i>>=</i> _note<i>.</i>withdrawStart <i>+</i> _note<i>.</i>withdrawDelay<i>,</i> <b>\"</b><b>withdrawDelay has not ended</b><b>\"</b><i>)</i><i>;</i>\n" +
                "\t\tcheckSig<i>(</i>batchId<i>,</i> hardwareId<i>,</i> noteId<i>,</i> blockNum<i>,</i>blockHash<i>,</i> v<i>,</i> r<i>,</i> s<i>)</i><i>;</i>\n" +
                "\n" +
                "\t\t<i>// ETH deposit</i>\n" +
                "\t\t<b>if</b> <i>(</i>_note<i>.</i>token <i>==</i> address<i>(</i>0<i>)</i><i>)</i><i>{</i>\n" +
                "\t\t\trecipient<i>.</i>transfer<i>(</i>_note<i>.</i>amount <i>*</i> 1 ether<i>)</i><i>;</i>\n" +
                "\t\t\tETHBalance<i>[</i>noteId<i>]</i> <i>=</i> 0<i>;</i>\n" +
                "\t\t<i>// non-ETH deposit</i>\n" +
                "\t\t<i>}</i> <b>else</b> <i>{</i>\n" +
                "\t\t\ttokenContract <i>=</i> ERC20Interface<i>(</i>_note<i>.</i>token<i>)</i><i>;</i>\n" +
                "\t\t\trequire<i>(</i>\n" +
                "\t\t\t\ttokenContract<i>.</i>transfer<i>(</i>recipient<i>,</i> _note<i>.</i>amount<i>)</i><i>,</i>\n" +
                "\t\t\t\t<b>\"</b><b>token transfer not approved</b><b>\"</b>\n" +
                "\t\t\t<i>)</i><i>;</i>\n" +
                "\t\t<i>}</i>\n" +
                "\n" +
                "\t<i>}</i>\n" +
                "\n" +
                "\t<b>function</b> checkSig<i>(</i>uint batchId<i>,</i> uint hardwareId<i>,</i> uint noteId<i>,</i> uint blockNum<i>,</i> bytes32 blockHash<i>,</i> uint8 v<i>,</i> bytes32 r<i>,</i> bytes32 s<i>)</i> <b>public</b> view <i>{</i>\n" +
                "\t\trequire<i>(</i>blockhash<i>(</i>blockNum<i>)</i> <i>==</i> blockHash<i>,</i> <b>\"</b><b>invalid block (not in last 256 blocks)</b><b>\"</b><i>)</i><i>;</i>\n" +
                "\n" +
                "\t\tnote memory curNote <i>=</i> notes<i>[</i>noteId<i>]</i><i>;</i>\n" +
                "\t\taddress notePubKey <i>=</i> hardware<i>[</i>curNote<i>.</i>manufacturer<i>]</i><i>[</i>batchId<i>]</i><i>[</i>hardwareId<i>]</i><i>;</i>\n" +
                "\n" +
                "\t\tbytes32 messageDigest <i>=</i> keccak256<i>(</i>abi<i>.</i>encodePacked<i>(</i><b>\"</b><i>\\x19</i><b>Ethereum Signed Message:</b><i>\\n</i><b>32</b><b>\"</b><i>,</i> blockHash<i>)</i><i>)</i><i>;</i>\n" +
                "\n" +
                "\t\trequire<i>(</i>notePubKey <i>==</i> ecrecover<i>(</i>messageDigest<i>,</i> v<i>,</i> r<i>,</i> s<i>)</i><i>,</i> <b>'</b><b>only trusted hardware can sign withdraw</b><b>'</b><i>)</i><i>;</i>\n" +
                "\n" +
                "\n" +
                "\t<i>}</i>\n" +
                "\n" +
                "<i>}</i>\n" +
                "</pre>\n" +
                "</html>";

        wv.loadData(customHtml, "text/html", "UTF-8");
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
    }

}
