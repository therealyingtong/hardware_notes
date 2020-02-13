package com.hardwarenotes.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import org.web3j.abi.datatypes.generated.Bytes32;

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

    public static byte[] bytesToBytes32(byte[] byteValue) {
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
