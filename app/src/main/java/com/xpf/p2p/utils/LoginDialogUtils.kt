package com.xpf.p2p.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import com.xpf.p2p.R
import com.xpf.p2p.ui.login.view.LoginActivity

object LoginDialogUtils {

    /**
     * 显示登录提示弹框
     * @return true 表示已登录，false 表示未登录（已弹出登录提示）
     */
    fun checkLoginOrPrompt(context: Context?): Boolean {
        if (context == null) return false
        if (TimeUtil.isLoginValid()) return true
        showLoginDialog(context)
        return false
    }

    private fun showLoginDialog(context: Context) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_login_prompt, null)
        dialog.setContentView(view)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = attributes
            params.width = (context.resources.displayMetrics.widthPixels * 0.8).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
        }
        dialog.setCancelable(true)

        view.findViewById<android.view.View>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<android.view.View>(R.id.btn_login).setOnClickListener {
            dialog.dismiss()
            LoginActivity.actionStart(context)
        }

        dialog.show()
    }
}
