package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;

import im.status.keycard.android.NFCCardManager;
import im.status.keycard.applet.ApplicationInfo;
import im.status.keycard.applet.KeycardCommandSet;
import im.status.keycard.io.APDUException;
import im.status.keycard.io.CardListener;
import im.status.keycard.io.CardChannel;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private NFCCardManager cardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Android NFC default adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Create the NFCCardManager, this class is provided by the Keycard SDK and handles connections to the card
        cardManager = new NFCCardManager();

        // The Card Listener receives the connected/disconnected events. These can happen at any time since the user can
        // introduce or remove the card to/from the field at any time. This is where your code goes.
        cardManager.setCardListener(new CardListener() {
//            @Override
            public void onConnected(CardChannel cardChannel) {
                KeycardCommandSet cmdSet = new KeycardCommandSet(cardChannel);

                try {
                    ApplicationInfo info = new ApplicationInfo(cmdSet.select().checkOK().getData());
                    // This method tells if the card is initialized (has a PIN, PUK and pairing password). If it is not, it must be
                    // initialized and no other operation is possible. Note that initialization touches only credentials to authenticate
                    // the user or the client, but does not touch the creation of a wallet on the card
                    info.isInitializedCard();

                    // Returns the instance UID of the applet. This can be used to identify this specific applet instance, very
                    // useful when storing instance-specific data on the client (pairing info, cached data, etc).
                    info.getInstanceUID();

                    // Returns the version of the applet.
                    info.getAppVersion();

                    // Returns the number of free pairing slots. If you are not yet paired with the card, it helps you know if you can still
                    // pair or not
                    info.getFreePairingSlots();

                    // Tells if the card has a wallet or not. If no wallet is available, you must create once before you can perform most
                    // operations on the card
                    info.hasMasterKey();

                    // Returns the UID of the master key of the wallet. The UID is value generated starting from the public key and is
                    // useful to identify if the card has the expected wallet.
                    info.getKeyUID();

                    // Usually, you want to check if the card is initialized before trying to initialize it, otherwise you will receive an
//                // error.
//                if (!info.isInitializedCard()) {
//                    // The PIN must be 6 digits, the PUK 12 digits and the pairing password can be any password.
//                    // All parameters are strings
//                    cmdSet.init(pin, puk, pairingPassword).checkOK();
//                }
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
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onPause() {
        super.onPause();

        // We disable the reader on pause to allow other apps to use it.
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }

}
