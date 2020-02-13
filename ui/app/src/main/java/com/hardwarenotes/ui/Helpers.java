package com.hardwarenotes.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.util.HashMap;
import java.util.Map;


public class Helpers extends AppCompatActivity  {


    public static Map<String, String> parseDepositData(String depositData){
        String[] tokens =
        Iterables.toArray(
        Splitter
        .fixedLength(64)
        .split(depositData.substring(2)),
        String.class
                );
                        for (String string : tokens) Log.i(string, string);

                        Map<String, String> map = new HashMap<String, String>();
        map.put("manufacturerAddress", "0x" + tokens[0].substring(24));
        map.put("batchId", String.valueOf(Long.parseLong(tokens[1],16)));
        map.put("hardwareId", String.valueOf(Long.parseLong(tokens[2],16)));
        map.put("noteId", Integer.toHexString(Integer.parseInt(tokens[3],16)));
        map.put("token", "0x" + tokens[4].substring(24));
        map.put("amount", Integer.toHexString(Integer.parseInt(tokens[5],16)));
        map.put("withdrawDelay", String.valueOf(Long.parseLong(tokens[6],16)));
        map.put("withdrawTimeout",String.valueOf(Long.parseLong(tokens[7], 16)));
        map.put("eventBlockNumber", String.valueOf(Integer.valueOf(tokens[8].substring(2),16)));

        return map;
    }



}
