package com.hardwarenotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(
                "noteAddress: " + noteAddress + "\n" +
                        "manufacturerAddress: " + manufacturerAddress + "\n" +
                        "batchId: " + batchId + "\n" +
                        "hardwareId: " + hardwareId + "\n" +
                        "noteId:" + noteId + "\n" +
                        "token: " + token + "\n" +
                        "amount: " + amount + "\n" +
                        "withdrawDelay: " + withdrawDelay + "\n" +
                        "withdrawTimeout: " + withdrawTimeout + "\n" +
                        "withdrawStart: " + withdrawStart + "\n" +
                        "isInFlight: " + isInFlight + "\n\n" +
                        "currentBlock: " + currentBlock
        );

    }
}
