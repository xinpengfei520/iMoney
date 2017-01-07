package com.xpf.p2p.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:18091383534
 * Function:提供当前应用中所有创建的Activity的管理器(单例:饿汉式,懒汉式)
 * 涉及到activity的添加、删除指定、删除当前、删除所有、返回栈大小的方法
 */

public class ActivityManager {

    // 使用恶汉式,实现单例模式
    public ActivityManager() {
    }

    private static ActivityManager instance = new ActivityManager();

    public static ActivityManager getInstance() {
        return instance;
    }

    // 提供操作activity的容器：Stack
    private Stack<Activity> activityStack = new Stack<>();

    // activity的添加
    public void add(Activity activity) {
        if (activity != null) {
            activityStack.push(activity);
        }
    }

    // 删除指定的activity
    public void remove(Activity activity) {

//        for(int i = 0; i < activityStack.size(); i++) {
//            if(activity != null && activity.getClass().equals(activityStack.get(i).getClass())){
//                activity.finish();//销毁当前的activity对象
//                activityStack.remove(i);//将指定的activity对象从栈空间移除
//            }
//        }

        for (int i = activityStack.size(); i >= 0; i--) {
            if (activity != null && activity.getClass().equals(activityStack.get(i).getClass())) {

                activity.finish();       // 销毁当前的activity
                activityStack.remove(i); // 将指定的activity对象从栈空间移除
            }
        }
    }

    // 删除当前的activity(栈顶的activity)
    public void removeCurrent() {
        //方式一：
//        Activity activity = activityStack.get(activityStack.size() - 1);
//        activity.finish();
//        activityStack.remove(activityStack.size() - 1);
        //方式二：
        activityStack.lastElement().finish();
        activityStack.remove(activityStack.lastElement());
//        activityStack.pop().finish();
    }

    // 删除所有的activity
    public void removeAll() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            activityStack.get(i).finish();
            activityStack.remove(i);
        }
    }

    // 返回栈大小
    public int getSize() {
        return activityStack.size();
    }
}
