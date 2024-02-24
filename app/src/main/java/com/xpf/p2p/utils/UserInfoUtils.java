package com.xpf.p2p.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.xpf.common.bean.User;

/**
 * UserInfoUtils
 * @author Vance :)
 * @date 2024/2/24
 */
public class UserInfoUtils {

    /**
     * 保存用户信息的操作,使用sp存储
     * @param context
     * @param user
     */
    @SuppressLint("ApplySharedPref")
    public static void saveUser(Context context, User user) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("UF_ACC", user.UF_ACC);
        edit.putString("UF_AVATAR_URL", user.UF_AVATAR_URL);
        edit.putString("UF_IS_CERT", user.UF_IS_CERT);
        edit.putString("UF_PHONE", user.UF_PHONE);
        edit.commit(); // 只有提交以后,才可以创建此文件,并保存数据
    }

    /**
     * 读取数据,得到内存中的User对象
     * @param context
     * @return
     */
    public static User readUser(Context context) {
        if (context == null) {
            return null;
        }
        User user = new User();
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user.UF_ACC = sp.getString("UF_ACC", "");
        user.UF_AVATAR_URL = sp.getString("UF_AVATAR_URL", "");
        user.UF_IS_CERT = sp.getString("UF_IS_CERT", "");
        user.UF_PHONE = sp.getString("UF_PHONE", "");
        return user;
    }
}
