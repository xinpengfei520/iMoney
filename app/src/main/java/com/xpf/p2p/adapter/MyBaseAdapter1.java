package com.xpf.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Function:MyBaseAdapter1基类,让子类去实现自己特有的getView()方法
 */

public abstract class MyBaseAdapter1<T> extends BaseAdapter {

    public List<T> list;

    public MyBaseAdapter1(List<T> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return MyGetView(position, convertView, parent);
    }

    protected abstract View MyGetView(int position, View convertView, ViewGroup parent);
}
