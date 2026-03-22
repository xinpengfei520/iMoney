package com.xpf.p2p.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by xpf on 2016/11/15 :)
 * Function:MyBaseAdapter3的基类
 */
abstract class MyBaseAdapter3<T>(val list: List<T>?) : BaseAdapter() {

    override fun getCount(): Int = list?.size ?: 0

    override fun getItem(position: Int): Any? = list?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    @Suppress("UNCHECKED_CAST")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: BaseHolder<T> = if (convertView == null) {
            getHolder()
        } else {
            convertView.tag as BaseHolder<T>
        }

        val t = list!![position]
        holder.data = t
        return holder.rootView
    }

    protected abstract fun getHolder(): BaseHolder<T>
}
