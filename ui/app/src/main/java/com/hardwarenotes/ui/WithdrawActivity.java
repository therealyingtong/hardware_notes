package com.hardwarenotes.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import im.status.keycard.applet.RecoverableSignature;


import static com.hardwarenotes.ui.MainActivity.signature;

public class WithdrawActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        parseSignature(signature);
        isMainActivity = false;

    }

    public void clickWithdraw1 (View view){

        view.setEnabled(false);
//        Intent intent = new Intent(this, Withdraw1Activity.class);
//        startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView tv = findViewById(R.id.textView3);
                tv.setText("hi");
                view.setEnabled(true);
            }
        }, 2000);

    }

    public void clickWithdraw2 (View view){
        view.setEnabled(false);
//        Intent intent = new Intent(this, Withdraw1Activity.class);
//        startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);

    }


    public void parseSignature(RecoverableSignature signature){
        try{
            int v = signature.getRecId();
            Log.i("v", String.valueOf(v));

            byte[] r = signature.getR();
            Log.i("R", new String(r));

            byte[] s = signature.getS();
            Log.i("S", new String(s));
        } catch (Exception exception){

        }


//        saveToPreferences(_noteAddress, depositData);
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

    public boolean isConnected(){
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }


}

