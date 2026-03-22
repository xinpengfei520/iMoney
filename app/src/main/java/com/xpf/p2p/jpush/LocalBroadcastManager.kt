package com.xpf.p2p.jpush

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Message
import com.xpf.p2p.utils.LogUtils

/**
 * Created by efan on 2017/4/14.
 */
class LocalBroadcastManager private constructor(context: Context) {

    private val mAppContext: Context = context
    private val mReceivers = HashMap<BroadcastReceiver, ArrayList<IntentFilter>>()
    private val mActions = HashMap<String, ArrayList<ReceiverRecord>>()
    private val mPendingBroadcasts = ArrayList<BroadcastRecord>()
    private val mHandler: Handler

    init {
        mHandler = object : Handler(context.mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MSG_EXEC_PENDING_BROADCASTS -> executePendingBroadcasts()
                    else -> super.handleMessage(msg)
                }
            }
        }
    }

    fun registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
        synchronized(mReceivers) {
            val entry = ReceiverRecord(filter, receiver)
            var filters = mReceivers[receiver]
            if (filters == null) {
                filters = ArrayList(1)
                mReceivers[receiver] = filters
            }
            filters.add(filter)

            for (i in 0 until filter.countActions()) {
                val action = filter.getAction(i)
                var entries = mActions[action]
                if (entries == null) {
                    entries = ArrayList(1)
                    mActions[action] = entries
                }
                entries.add(entry)
            }
        }
    }

    fun unregisterReceiver(receiver: BroadcastReceiver) {
        synchronized(mReceivers) {
            val filters = mReceivers.remove(receiver) ?: return
            for (i in 0 until filters.size) {
                val filter = filters[i]
                for (j in 0 until filter.countActions()) {
                    val action = filter.getAction(j)
                    val receivers = mActions[action]
                    if (receivers != null) {
                        var k = 0
                        while (k < receivers.size) {
                            if (receivers[k].receiver === receiver) {
                                receivers.removeAt(k)
                                k--
                            }
                            k++
                        }
                        if (receivers.size <= 0) {
                            mActions.remove(action)
                        }
                    }
                }
            }
        }
    }

    fun sendBroadcast(intent: Intent): Boolean {
        synchronized(mReceivers) {
            val action = intent.action
            val type = intent.resolveTypeIfNeeded(mAppContext.contentResolver)
            val data = intent.data
            val scheme = intent.scheme
            val categories = intent.categories
            val debug = intent.flags and 8 != 0
            if (debug) {
                LogUtils.v(TAG, "Resolving type $type scheme $scheme of intent $intent")
            }

            val entries = mActions[intent.action]
            if (entries != null) {
                if (debug) {
                    LogUtils.v(TAG, "Action list: $entries")
                }

                var receivers: ArrayList<ReceiverRecord>? = null

                for (i in 0 until entries.size) {
                    val receiver = entries[i]
                    if (debug) {
                        LogUtils.v(TAG, "Matching against filter ${receiver.filter}")
                    }

                    if (receiver.broadcasting) {
                        if (debug) {
                            LogUtils.v(TAG, "  Filter's target already added")
                        }
                    } else {
                        val match = receiver.filter.match(action, type, scheme, data, categories, "LocalBroadcastManager")
                        if (match >= 0) {
                            if (debug) {
                                LogUtils.v(TAG, "  Filter matched!  match=0x${Integer.toHexString(match)}")
                            }
                            if (receivers == null) {
                                receivers = ArrayList()
                            }
                            receivers.add(receiver)
                            receiver.broadcasting = true
                        } else if (debug) {
                            val reason = when (match) {
                                -4 -> "category"
                                -3 -> "action"
                                -2 -> "data"
                                -1 -> "type"
                                else -> "unknown reason"
                            }
                            LogUtils.v(TAG, "Filter did not match: $reason")
                        }
                    }
                }

                if (receivers != null) {
                    for (i in 0 until receivers.size) {
                        receivers[i].broadcasting = false
                    }
                    mPendingBroadcasts.add(BroadcastRecord(intent, receivers))
                    if (!mHandler.hasMessages(MSG_EXEC_PENDING_BROADCASTS)) {
                        mHandler.sendEmptyMessage(MSG_EXEC_PENDING_BROADCASTS)
                    }
                    return true
                }
            }
            return false
        }
    }

    fun sendBroadcastSync(intent: Intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts()
        }
    }

    private fun executePendingBroadcasts() {
        while (true) {
            val brs: Array<BroadcastRecord>
            synchronized(mReceivers) {
                val n = mPendingBroadcasts.size
                if (n <= 0) return
                brs = mPendingBroadcasts.toTypedArray()
                mPendingBroadcasts.clear()
            }
            for (br in brs) {
                for (j in 0 until br.receivers.size) {
                    br.receivers[j].receiver.onReceive(mAppContext, br.intent)
                }
            }
        }
    }

    private class BroadcastRecord(val intent: Intent, val receivers: ArrayList<ReceiverRecord>)

    private class ReceiverRecord(val filter: IntentFilter, val receiver: BroadcastReceiver) {
        var broadcasting: Boolean = false

        override fun toString(): String {
            return "Receiver{$receiver filter=$filter}"
        }
    }

    companion object {
        private const val TAG = "LocalBroadcastManager"
        private const val DEBUG = false
        const val MSG_EXEC_PENDING_BROADCASTS = 1
        private val mLock = Any()
        private var mInstance: LocalBroadcastManager? = null

        @JvmStatic
        fun getInstance(context: Context): LocalBroadcastManager {
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = LocalBroadcastManager(context.applicationContext)
                }
                return mInstance!!
            }
        }
    }
}
