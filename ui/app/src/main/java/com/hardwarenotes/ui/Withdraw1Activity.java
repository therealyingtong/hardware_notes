package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.web3j.protocol.core.methods.response.EthBlock;

import im.status.keycard.android.NFCCardManager;
import im.status.keycard.applet.CashApplicationInfo;
import im.status.keycard.applet.CashCommandSet;
import im.status.keycard.applet.RecoverableSignature;
import im.status.keycard.io.CardChannel;
import im.status.keycard.io.CardListener;

import static com.hardwarenotes.ui.MainActivity.cardManager;
import static com.hardwarenotes.ui.MainActivity.getCurrentBlock;

public class Withdraw1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw1);

        cardManager.setCardListener(new CardListener() {
            //            @Override
            public void onConnected(CardChannel cardChannel) {

                Log.i("onConnected", "onConnected");

                CashCommandSet cashCmdSet = new CashCommandSet(cardChannel);


                try {
                    RecoverableSignature signature = withdrawSig(cashCmdSet);

                    CashApplicationInfo info = new CashApplicationInfo(cashCmdSet.select().checkOK().getData());

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


    public RecoverableSignature withdrawSig(CashCommandSet cashCmdSet) throws Exception {

        EthBlock.Block block = getCurrentBlock();
        String currentBlock = block.getNumberRaw();
        String currentBlockHash = block.getHash();

        byte[] hash = currentBlockHash.getBytes();

        RecoverableSignature signature = new RecoverableSignature(hash, cashCmdSet.sign(hash).checkOK().getData());
        return signature;

    }
//
//    public static void saveSignature(RecoverableSignature signature){
//        String v = signature.getRecId();
//        Log.i("v", v);
//        String r = new String(signature.getR());
//        Log.i("R", r);
//        String s = new String(signature.getS());
//        Log.i("S",s);
//
//        saveToPreferences(_noteAddress, depositData);
//    }



}
