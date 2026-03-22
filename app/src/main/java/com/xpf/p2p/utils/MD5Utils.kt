package com.xpf.p2p.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5Utils {

    @JvmStatic
    fun MD5(sourceStr: String): String {
        var result = ""
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(sourceStr.toByteArray())
            val b = md.digest()
            val buf = StringBuffer("")
            for (offset in b.indices) {
                var i = b[offset].toInt()
                if (i < 0) i += 256
                if (i < 16) buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            result = buf.toString()
        } catch (e: NoSuchAlgorithmException) {
            println(e)
        }
        return result
    }
}
