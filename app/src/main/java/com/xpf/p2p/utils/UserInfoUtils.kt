package com.xpf.p2p.utils

import android.annotation.SuppressLint
import android.content.Context
import com.xpf.p2p.entity.User

/**
 * UserInfoUtils
 * @author Vance :)
 * @date 2024/2/24
 */
object UserInfoUtils {

    @SuppressLint("ApplySharedPref")
    @JvmStatic
    fun saveUser(context: Context?, user: User) {
        if (context == null) return
        val sp = context.applicationContext.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putString("UF_ACC", user.UF_ACC)
        edit.putString("UF_AVATAR_URL", user.UF_AVATAR_URL)
        edit.putString("UF_IS_CERT", user.UF_IS_CERT)
        edit.putString("UF_PHONE", user.UF_PHONE)
        edit.commit()
    }

    @JvmStatic
    fun readUser(context: Context?): User? {
        if (context == null) return null
        val user = User()
        val sp = context.applicationContext.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        user.UF_ACC = sp.getString("UF_ACC", "")
        user.UF_AVATAR_URL = sp.getString("UF_AVATAR_URL", "")
        user.UF_IS_CERT = sp.getString("UF_IS_CERT", "")
        user.UF_PHONE = sp.getString("UF_PHONE", "")
        return user
    }
}
