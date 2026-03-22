package com.xpf.p2p.utils

import android.app.Activity
import java.util.Stack

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:vancexin
 * Function:提供当前应用中所有创建的Activity的管理器(单例:饿汉式,懒汉式)
 * 涉及到activity的添加、删除指定、删除当前、删除所有、返回栈大小的方法
 */
class ActivityManager private constructor() {

    private val activityStack = Stack<Activity>()

    fun add(activity: Activity?) {
        if (activity != null) {
            activityStack.push(activity)
        }
    }

    fun remove(activity: Activity?) {
        for (i in activityStack.size downTo 0) {
            if (activity != null && activity.javaClass == activityStack[i].javaClass) {
                activity.finish()
                activityStack.removeAt(i)
            }
        }
    }

    fun removeCurrent() {
        activityStack.lastElement().finish()
        activityStack.remove(activityStack.lastElement())
    }

    fun removeAll() {
        for (i in activityStack.size - 1 downTo 0) {
            activityStack[i].finish()
            activityStack.removeAt(i)
        }
    }

    fun getSize(): Int = activityStack.size

    companion object {
        @JvmStatic
        val instance: ActivityManager = ActivityManager()
    }
}
