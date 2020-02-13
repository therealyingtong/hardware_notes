package com.hardwarenotes.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import im.status.keycard.applet.RecoverableSignature;

import static com.hardwarenotes.ui.Helpers.bytesToBytes32;
import static com.hardwarenotes.ui.Helpers.hexStringToByteArray;
import static com.hardwarenotes.ui.Helpers.parseDepositData;


public class WithdrawActivity extends MainActivity {

    BigInteger batchId;
    BigInteger hardwareId;
    BigInteger noteId;
    BigInteger blockNum;
    byte[] blockHash;
    BigInteger v;
    byte[] r;
    byte[] s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        isMainActivity = false;

    }

    public void clickWithdraw1(View view) {

        boolean connected = isConnected();
        if (connected){

            try{
                view.setEnabled(false);
//        Intent intent = new Intent(this, Withdraw1Activity.class);
//        startActivity(intent);

                Map<String,Object> signatureMap = parseSignature(signature);
                Log.i("noteAddress",noteAddress);

                String depositData = readFromPreferences(noteAddress);
                Map<String, String> depositMap = parseDepositData(depositData);
                batchId = new BigInteger(depositMap.get("batchId"));
                noteId = new BigInteger(depositMap.get("noteId"));
                hardwareId = new BigInteger(depositMap.get("hardwareId"));
                blockNum = new BigInteger(depositMap.get("eventBlockNumber"));
                String currentBlockHash = readFromPreferences("currentBlockHash");
                blockHash = bytesToBytes32(hexStringToByteArray(currentBlockHash.substring(2)));
                v = (BigInteger) signatureMap.get("v");
                r = bytesToBytes32((byte[]) signatureMap.get("r"));
                s = bytesToBytes32((byte[]) signatureMap.get("s"));

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            TransactionReceipt result = hardwareNotes.signalWithdraw(
                                    batchId,
                                    hardwareId,
                                    noteId,
                                    blockNum,
                                    blockHash,
                                    v,
                                    r,
                                    s
                            ).send();


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    TextView tv = findViewById(R.id.textView5);
                                    tv.setText(result.toString());
                                    view.setEnabled(true);
                                }
                            }, 2000);
                        } catch (Exception exception){

                        }

                    }
                });
                thread.start();
//            thread.interrupt();

            } catch (Exception exception){
                Logger logger = Logger.getLogger("com.hardwarenotes.ui");
                logger.log(Level.SEVERE, "failed to get block", exception);
                view.setEnabled(true);

            }

        }


    }

    public void clickWithdraw2(View view) {


        boolean connected = isConnected();
        if (connected){

            try{
                view.setEnabled(false);
//        Intent intent = new Intent(this, Withdraw1Activity.class);
//        startActivity(intent);

                Map<String,Object> signatureMap = parseSignature(signature);

                String depositData = readFromPreferences(noteAddress);
                Map<String, String> depositMap = parseDepositData(depositData);
                batchId = new BigInteger(depositMap.get("batchId"));
                noteId = new BigInteger(depositMap.get("noteId"));
                hardwareId = new BigInteger(depositMap.get("hardwareId"));
                blockNum = new BigInteger(depositMap.get("blockNum"));
                blockHash = readFromPreferences("currentBlockHash").getBytes();
                v = (BigInteger) signatureMap.get("v");
                r = (byte[]) signatureMap.get("r");
                s = (byte[]) signatureMap.get("s");
                String recipient = "";

                TransactionReceipt result = hardwareNotes.withdraw(
                        batchId,
                        hardwareId,
                        noteId,
                        blockNum,
                        blockHash,
                        v,
                        r,
                        s,
                        recipient
                ).send();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.textView5);
                        tv.setText(result.toString());
                        view.setEnabled(true);
                    }
                }, 2000);
            } catch (Exception exception){
                Logger logger = Logger.getLogger("com.hardwarenotes.ui");
                logger.log(Level.SEVERE, "failed to get block", exception);
                view.setEnabled(true);

            }

        }

    }


    public Map<String, Object> parseSignature(RecoverableSignature signature) {

        if (signature != null){
            try {

                Map<String, Object> map = new HashMap<>();

                BigInteger v = BigInteger.valueOf(signature.getRecId());
                Log.i("v", String.valueOf(v));
                map.put("v", v);

                byte[] r = signature.getR();
                Log.i("R", new String(r));
                map.put("r", r);

                byte[] s = signature.getS();
                Log.i("S", new String(s));
                map.put("s", s);

                return map;

            } catch (Exception exception) {
                Logger logger = Logger.getLogger("com.hardwarenotes.ui");
                logger.log(Level.SEVERE, "failed to parse signature", exception);
                return null;
            }
        } else{
            Log.e("error","signature is null");
            return null;
        }



//        saveToPreferences(_noteAddress, depositData);
    }

    public void saveToPreferences(String prefName, String prefValue) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }

    public String readFromPreferences(String prefName) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        return pref.getString(prefName, null);
    }

}

