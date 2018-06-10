package com.xpf.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Wechat:18091383534
 * Function:MyBaseAdapter3的基类
 */

public abstract class MyBaseAdapter3<T> extends BaseAdapter {

    public List<T> list;

    public MyBaseAdapter3(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 实现的操作:①需要加载具体的item的布局,布局是不同的 ②装配数据的过程是不同的
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            holder = getHolder();
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        T t = list.get(position);
        holder.setData(t);
        return holder.getRootView();
    }

    protected abstract BaseHolder getHolder();

}
