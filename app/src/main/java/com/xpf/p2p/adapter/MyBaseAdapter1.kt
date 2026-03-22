package com.xpf.p2p.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by xpf on 2016/11/15 :)
 * Function:MyBaseAdapter1基类,让子类去实现自己特有的getView()方法
 */
abstract class MyBaseAdapter1<T>(val list: List<T>?) : BaseAdapter() {

    override fun getCount(): Int = list?.size ?: 0

    override fun getItem(position: Int): Any? = list?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myGetView(position, convertView, parent)
    }

    protected abstract fun myGetView(position: Int, convertView: View?, parent: ViewGroup): View
}
