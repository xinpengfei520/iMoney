package com.xpf.p2p.jpush

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.SparseArray
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.JPushMessage
import com.xpf.p2p.utils.LogUtils
import java.util.Locale

/**
 * 处理 tag alias 相关的逻辑
 */
class TagAliasOperatorHelper private constructor() {

    private var context: Context? = null
    private val setActionCache = SparseArray<Any>()

    @SuppressLint("HandlerLeak")
    private val delaySendHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                DELAY_SEND_ACTION -> {
                    if (msg.obj is TagAliasBean) {
                        LogUtils.i(TAG, "on delay time")
                        sequence++
                        val tagAliasBean = msg.obj as TagAliasBean
                        setActionCache.put(sequence, tagAliasBean)
                        if (context != null) {
                            handleAction(context!!, sequence, tagAliasBean)
                        } else {
                            LogUtils.e(TAG, "#unexcepted - context was null")
                        }
                    } else {
                        LogUtils.e(TAG, "#unexcepted - msg obj was incorrect")
                    }
                }
                DELAY_SET_MOBILE_NUMBER_ACTION -> {
                    if (msg.obj is String) {
                        LogUtils.i(TAG, "retry set mobile number")
                        sequence++
                        val mobileNumber = msg.obj as String
                        setActionCache.put(sequence, mobileNumber)
                        if (context != null) {
                            handleAction(context!!, sequence, mobileNumber)
                        } else {
                            LogUtils.e(TAG, "#unexcepted - context was null")
                        }
                    } else {
                        LogUtils.e(TAG, "#unexcepted - msg obj was incorrect")
                    }
                }
            }
        }
    }

    fun init(context: Context?) {
        if (context != null) {
            this.context = context.applicationContext
        }
    }

    operator fun get(sequence: Int): Any? = setActionCache.get(sequence)

    fun remove(sequence: Int): Any? = setActionCache.get(sequence)

    fun put(sequence: Int, tagAliasBean: Any) {
        setActionCache.put(sequence, tagAliasBean)
    }

    fun handleAction(context: Context, sequence: Int, mobileNumber: String) {
        put(sequence, mobileNumber)
        LogUtils.d(TAG, "sequence:$sequence,mobileNumber:$mobileNumber")
        JPushInterface.setMobileNumber(context, sequence, mobileNumber)
    }

    fun handleAction(context: Context, sequence: Int, tagAliasBean: TagAliasBean?) {
        init(context)
        if (tagAliasBean == null) {
            LogUtils.e(TAG, "tagAliasBean was null")
            return
        }
        put(sequence, tagAliasBean)
        if (tagAliasBean.isAliasAction) {
            when (tagAliasBean.action) {
                ACTION_GET -> JPushInterface.getAlias(context, sequence)
                ACTION_DELETE -> JPushInterface.deleteAlias(context, sequence)
                ACTION_SET -> JPushInterface.setAlias(context, sequence, tagAliasBean.alias)
                else -> LogUtils.e(TAG, "unSupport alias action type")
            }
        } else {
            when (tagAliasBean.action) {
                ACTION_ADD -> JPushInterface.addTags(context, sequence, tagAliasBean.tags)
                ACTION_SET -> JPushInterface.setTags(context, sequence, tagAliasBean.tags)
                ACTION_DELETE -> JPushInterface.deleteTags(context, sequence, tagAliasBean.tags)
                ACTION_CHECK -> {
                    val tag = tagAliasBean.tags!!.toTypedArray()[0] as String
                    JPushInterface.checkTagBindState(context, sequence, tag)
                }
                ACTION_GET -> JPushInterface.getAllTags(context, sequence)
                ACTION_CLEAN -> JPushInterface.cleanTags(context, sequence)
                else -> LogUtils.e(TAG, "unSupport tag action type")
            }
        }
    }

    private fun retryActionIfNeeded(errorCode: Int, tagAliasBean: TagAliasBean?): Boolean {
        if (!ExampleUtil.isConnected(context!!)) {
            LogUtils.e(TAG, "no network")
            return false
        }
        if (errorCode == 6002 || errorCode == 6014) {
            LogUtils.d(TAG, "need retry")
            if (tagAliasBean != null) {
                val message = Message()
                message.what = DELAY_SEND_ACTION
                message.obj = tagAliasBean
                delaySendHandler.sendMessageDelayed(message, 1000 * 60)
                val logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode)
                ExampleUtil.showToast(logs, context!!)
                return true
            }
        }
        return false
    }

    private fun retrySetMobileNumberActionIfNeeded(errorCode: Int, mobileNumber: String): Boolean {
        if (!ExampleUtil.isConnected(context!!)) {
            LogUtils.e(TAG, "no network")
            return false
        }
        if (errorCode == 6002 || errorCode == 6024) {
            LogUtils.d(TAG, "need retry")
            val message = Message()
            message.what = DELAY_SET_MOBILE_NUMBER_ACTION
            message.obj = mobileNumber
            delaySendHandler.sendMessageDelayed(message, 1000 * 60)
            var str = "Failed to set mobile number due to %s. Try again after 60s."
            str = String.format(Locale.ENGLISH, str, if (errorCode == 6002) "timeout" else "server internal error")
            ExampleUtil.showToast(str, context!!)
            return true
        }
        return false
    }

    private fun getRetryStr(isAliasAction: Boolean, actionType: Int, errorCode: Int): String {
        var str = "Failed to %s %s due to %s. Try again after 60s."
        str = String.format(
            Locale.ENGLISH, str, getActionStr(actionType),
            if (isAliasAction) "alias" else " tags",
            if (errorCode == 6002) "timeout" else "server too busy"
        )
        return str
    }

    private fun getActionStr(actionType: Int): String {
        return when (actionType) {
            ACTION_ADD -> "add"
            ACTION_SET -> "set"
            ACTION_DELETE -> "delete"
            ACTION_GET -> "get"
            ACTION_CLEAN -> "clean"
            ACTION_CHECK -> "check"
            else -> "unknown operation"
        }
    }

    fun onTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        LogUtils.i(TAG, "action - onTagOperatorResult, sequence:$sequence,tags:${jPushMessage.tags}")
        LogUtils.i(TAG, "tags size:${jPushMessage.tags.size}")
        init(context)
        val tagAliasBean = setActionCache.get(sequence) as? TagAliasBean
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context)
            return
        }
        if (jPushMessage.errorCode == 0) {
            LogUtils.i(TAG, "action - modify tag Success,sequence:$sequence")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " tags success"
            LogUtils.i(TAG, logs)
            ExampleUtil.showToast(logs, context)
        } else {
            var logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags"
            if (jPushMessage.errorCode == 6018) {
                logs += ", tags is exceed limit need to clean"
            }
            logs += ", errorCode:" + jPushMessage.errorCode
            LogUtils.e(TAG, logs)
            if (!retryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    fun onCheckTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        LogUtils.i(TAG, "action - onCheckTagOperatorResult, sequence:$sequence,checktag:${jPushMessage.checkTag}")
        init(context)
        val tagAliasBean = setActionCache.get(sequence) as? TagAliasBean
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context)
            return
        }
        if (jPushMessage.errorCode == 0) {
            LogUtils.i(TAG, "tagBean:$tagAliasBean")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " tag " + jPushMessage.checkTag + " bind state success,state:" + jPushMessage.tagCheckStateResult
            LogUtils.i(TAG, logs)
            ExampleUtil.showToast(logs, context)
        } else {
            val logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags, errorCode:" + jPushMessage.errorCode
            LogUtils.e(TAG, logs)
            if (!retryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    fun onAliasOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        LogUtils.i(TAG, "action - onAliasOperatorResult, sequence:$sequence,alias:${jPushMessage.alias}")
        init(context)
        val tagAliasBean = setActionCache.get(sequence) as? TagAliasBean
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context)
            return
        }
        if (jPushMessage.errorCode == 0) {
            LogUtils.i(TAG, "action - modify alias Success,sequence:$sequence")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " alias success"
            LogUtils.i(TAG, logs)
        } else {
            val logs = "Failed to " + getActionStr(tagAliasBean.action) + " alias, errorCode:" + jPushMessage.errorCode
            LogUtils.e(TAG, logs)
            if (!retryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    fun onMobileNumberOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        LogUtils.i(TAG, "action - onMobileNumberOperatorResult, sequence:$sequence,mobileNumber:${jPushMessage.mobileNumber}")
        init(context)
        if (jPushMessage.errorCode == 0) {
            LogUtils.i(TAG, "action - set mobile number Success,sequence:$sequence")
            setActionCache.remove(sequence)
        } else {
            val logs = "Failed to set mobile number, errorCode:" + jPushMessage.errorCode
            LogUtils.e(TAG, logs)
            if (!retrySetMobileNumberActionIfNeeded(jPushMessage.errorCode, jPushMessage.mobileNumber)) {
                ExampleUtil.showToast(logs, context)
            }
        }
    }

    class TagAliasBean {
        @JvmField var action: Int = 0
        @JvmField var tags: Set<String>? = null
        @JvmField var alias: String? = null
        @JvmField var isAliasAction: Boolean = false

        override fun toString(): String {
            return "TagAliasBean{action=$action, tags=$tags, alias='$alias', isAliasAction=$isAliasAction}"
        }
    }

    companion object {
        private const val TAG = "TagAliasOperatorHelper"

        @JvmStatic
        var sequence = 1

        const val ACTION_ADD = 1
        const val ACTION_SET = 2
        const val ACTION_DELETE = 3
        const val ACTION_CLEAN = 4
        const val ACTION_GET = 5
        const val ACTION_CHECK = 6
        const val DELAY_SEND_ACTION = 1
        const val DELAY_SET_MOBILE_NUMBER_ACTION = 2

        private var mInstance: TagAliasOperatorHelper? = null

        @JvmStatic
        fun getInstance(): TagAliasOperatorHelper {
            if (mInstance == null) {
                synchronized(TagAliasOperatorHelper::class.java) {
                    if (mInstance == null) {
                        mInstance = TagAliasOperatorHelper()
                    }
                }
            }
            return mInstance!!
        }
    }
}
