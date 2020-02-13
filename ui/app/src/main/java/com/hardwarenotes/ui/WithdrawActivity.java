package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import im.status.keycard.applet.CashApplicationInfo;
import im.status.keycard.applet.CashCommandSet;
import im.status.keycard.applet.RecoverableSignature;

public class WithdrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

    }

    public void clickWithdraw1 (View view){

        view.setEnabled(false);
        Intent intent = new Intent(this, Withdraw1Activity.class);
        startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);

    }

    public void clickWithdraw2 (View view){
        view.setEnabled(false);
        Intent intent = new Intent(this, Withdraw1Activity.class);
        startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);

    }
}
