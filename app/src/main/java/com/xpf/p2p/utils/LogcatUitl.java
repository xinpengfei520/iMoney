package com.xpf.p2p.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by xpf on 2017/12/20 :)
 * Function:
 */

public class LogcatUitl {

    public static void catLogcat() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            Log.e("TAG", "LOGCAT===" + log.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
