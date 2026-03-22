package com.xpf.p2p.utils

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by xpf on 2017/12/20 :)
 * Function:
 */
object LogcatUitl {

    @JvmStatic
    fun catLogcat() {
        try {
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            val log = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                log.append(line)
            }
            Log.e("TAG", "LOGCAT===$log")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
