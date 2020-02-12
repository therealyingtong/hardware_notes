package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.Map;

import static com.hardwarenotes.ui.MainActivity.noteAddress;
import static com.hardwarenotes.ui.Helpers.parseDepositData;

public class DisplayNoteActivity extends AppCompatActivity {

    private String manufacturerAddress = "";
    private String token = "";
    private String batchId = "";
    private String hardwareId = "";
    private String noteId = "";
    private String amount = "";
    private String withdrawDelay = "";
    private String withdrawTimeout = "";
    private String withdrawStart = "";
    private String isInFlight = "";
    private String eventBlockNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String depositData = pref.getString(noteAddress, null);
        String currentBlock = pref.getString("currentBlock", null);

        if (depositData != null) {

            Map<String, String> depositDataMap = parseDepositData(depositData);
            manufacturerAddress = depositDataMap.get("manufacturerAddress");
            token = depositDataMap.get("token");
            batchId = depositDataMap.get("batchId");
            hardwareId = depositDataMap.get("hardwareId");
            noteId = depositDataMap.get("noteId");
            amount = depositDataMap.get("amount");
            withdrawDelay = depositDataMap.get("withdrawDelay");
            withdrawTimeout = depositDataMap.get("withdrawTimeout");
            withdrawStart = pref.getString(noteId + "withdrawStart", null);
            isInFlight = pref.getString(noteId + "isInFlight", null);
            eventBlockNumber = depositDataMap.get("eventBlockNumber");

        }

        String s = "<b>noteAddress:</b><br>" + "0x" + noteAddress + "<br><br>" +
                "<b>manufacturerAddress:</b><br>" + manufacturerAddress + "<br><br>" +
                "<b>token:</b><br>" + token + "<br><br>" +
                "<b>batchId:</b> " + batchId +
                "<br>" +
                "<b>hardwareId:</b> " + hardwareId + "<br>" +
                "<b>noteId:</b> " + noteId + "<br>" +
                "<b>amount:</b> " + amount + "<br>" +
                "<b>withdrawDelay:</b> " + withdrawDelay + " blocks<br>" +
                "<b>withdrawTimeout:</b> " + withdrawTimeout + " blocks<br>" +
                "<b>withdrawStart:</b> " + withdrawStart + "<br>" +
                "<b>isInFlight:</b> " + isInFlight + "<br><br>" +
                "<b>deposit made at block: </b>" + eventBlockNumber + "<br>" +
                "<b>your state is at block: </b>" + currentBlock +  "<br><br>";

            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(Html.fromHtml(s));
        }



}
