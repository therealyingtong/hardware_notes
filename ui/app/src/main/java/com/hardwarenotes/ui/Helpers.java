package com.hardwarenotes.ui;

import android.util.Log;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.hardwarenotes.ui.MainActivity.contract;
import static com.hardwarenotes.ui.MainActivity.credentials;
import static com.hardwarenotes.ui.MainActivity.currentBlock;

public class Helpers{


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
