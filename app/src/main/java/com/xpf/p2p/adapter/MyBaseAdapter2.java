package com.xpf.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Wechat:18091383534
 * Function:MyBaseAdapter2的基类,将布局和装配数据的方法暴露出去让子类自己去实现
 */

public abstract class MyBaseAdapter2<T> extends BaseAdapter {

    public List<T> list;

    public MyBaseAdapter2(List<T> list) {
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

        ViewHolder holder;
        if (convertView == null) {
            convertView = initView();
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 装配数据
        T t = list.get(position);
        setData(convertView, t);

        return convertView;
    }

    // 装配数据
    protected abstract void setData(View convertView, T t);

    // 加载布局(自己所特有的布局)
    protected abstract View initView();

    class ViewHolder {
        public ViewHolder(View convertView) {
            convertView.setTag(this);
        }
    }
}
