package com.xpf.p2p.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by xpf on 2016/11/15 :)
 * Function:MyBaseAdapter2的基类,将布局和装配数据的方法暴露出去让子类自己去实现
 */
abstract class MyBaseAdapter2<T>(val list: List<T>?) : BaseAdapter() {

    override fun getCount(): Int = list?.size ?: 0

    override fun getItem(position: Int): Any? = list?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = initView()
            ViewHolder(view)
        }

        val t = list!![position]
        setData(view!!, t)
        return view
    }

    protected abstract fun setData(convertView: View, t: T)

    protected abstract fun initView(): View

    internal inner class ViewHolder(convertView: View) {
        init {
            convertView.tag = this
        }
    }
}
