package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import static com.hardwarenotes.ui.MainActivity.amount;
import static com.hardwarenotes.ui.MainActivity.batchId;
import static com.hardwarenotes.ui.MainActivity.currentBlock;
import static com.hardwarenotes.ui.MainActivity.hardwareId;
import static com.hardwarenotes.ui.MainActivity.isInFlight;
import static com.hardwarenotes.ui.MainActivity.manufacturerAddress;
import static com.hardwarenotes.ui.MainActivity.noteAddress;
import static com.hardwarenotes.ui.MainActivity.noteId;
import static com.hardwarenotes.ui.MainActivity.token;
import static com.hardwarenotes.ui.MainActivity.withdrawDelay;
import static com.hardwarenotes.ui.MainActivity.withdrawStart;
import static com.hardwarenotes.ui.MainActivity.withdrawTimeout;

public class DisplayNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        String s = "<b>noteAddress:</b><br>" + "0x" + noteAddress + "<br><br>" +
                "<b>manufacturerAddress:</b><br>" + manufacturerAddress + "<br><br>" +
                "<b>token:</b><br>" + token + "<br><br>" +
                "<b>batchId:</b> " + batchId + "<br>" +
                "<b>hardwareId:</b> " + hardwareId + "<br>" +
                "<b>noteId:</b> " + noteId + "<br>" +
                "<b>amount:</b> " + amount + "<br>" +
                "<b>withdrawDelay:</b> " + withdrawDelay + " blocks<br>" +
                "<b>withdrawTimeout:</b> " + withdrawTimeout + " blocks<br>" +
                "<b>withdrawStart:</b> " + withdrawStart + "<br>" +
                "<b>isInFlight:</b> " + isInFlight + "<br><br>" +
                "<b>currentBlock:</b> " + currentBlock;
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(Html.fromHtml(s));

    }
}
