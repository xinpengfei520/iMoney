package com.xpf.p2p.jpush

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.xpf.p2p.utils.LogUtils
import org.json.JSONException
import org.json.JSONObject

/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle = intent.extras!!
            LogUtils.i(TAG, "[MyReceiver] onReceive - ${intent.action}, extras: ${printBundle(bundle)}")

            when {
                JPushInterface.ACTION_REGISTRATION_ID == intent.action -> {
                    val regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                    LogUtils.i(TAG, "[MyReceiver] 接收Registration Id : $regId")
                }
                JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action -> {
                    LogUtils.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: ${bundle.getString(JPushInterface.EXTRA_MESSAGE)}")
                    processCustomMessage(context, bundle)
                }
                JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action -> {
                    LogUtils.i(TAG, "[MyReceiver] 接收到推送下来的通知")
                    val notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                    LogUtils.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: $notificationId")
                }
                JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action -> {
                    LogUtils.i(TAG, "[MyReceiver] 用户点击打开了通知")
                }
                JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.action -> {
                    LogUtils.i(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: ${bundle.getString(JPushInterface.EXTRA_EXTRA)}")
                }
                JPushInterface.ACTION_CONNECTION_CHANGE == intent.action -> {
                    val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                    LogUtils.e(TAG, "[MyReceiver]${intent.action} connected state change to $connected")
                }
                else -> {
                    LogUtils.i(TAG, "[MyReceiver] Unhandled intent - ${intent.action}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processCustomMessage(context: Context, bundle: Bundle) {
        val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
        val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)
        val msgIntent = Intent(MESSAGE_RECEIVED_ACTION)
        msgIntent.putExtra(KEY_MESSAGE, message)
        if (!ExampleUtil.isEmpty(extras)) {
            try {
                val extraJson = JSONObject(extras!!)
                if (extraJson.length() > 0) {
                    msgIntent.putExtra(KEY_EXTRAS, extras)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent)
    }

    companion object {
        private const val TAG = "JIGUANG-Example"
        private const val MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION"
        private const val KEY_MESSAGE = "message"
        private const val KEY_EXTRAS = "extras"

        private fun printBundle(bundle: Bundle): String {
            val sb = StringBuilder()
            for (key in bundle.keySet()) {
                when {
                    key == JPushInterface.EXTRA_NOTIFICATION_ID -> {
                        sb.append("\nkey:$key, value:${bundle.getInt(key)}")
                    }
                    key == JPushInterface.EXTRA_CONNECTION_CHANGE -> {
                        sb.append("\nkey:$key, value:${bundle.getBoolean(key)}")
                    }
                    key == JPushInterface.EXTRA_EXTRA -> {
                        if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                            LogUtils.i(TAG, "This message has no Extra data")
                            continue
                        }
                        try {
                            val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA)!!)
                            val it = json.keys()
                            while (it.hasNext()) {
                                val myKey = it.next()
                                sb.append("\nkey:$key, value: [$myKey - ${json.optString(myKey)}]")
                            }
                        } catch (e: JSONException) {
                            LogUtils.e(TAG, "Get message extra JSON error!")
                        }
                    }
                    else -> {
                        sb.append("\nkey:$key, value:${bundle.getString(key)}")
                    }
                }
            }
            return sb.toString()
        }
    }
}
