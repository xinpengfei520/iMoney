package com.xpf.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

/**
 * Created by xpf on 2018/6/12 :)
 * Function:
 */
public class VpGuideAdapter extends PagerAdapter {

    private ArrayList<ImageView> mImageViews; //存放图片的集合

    public VpGuideAdapter(ArrayList<ImageView> imageViews) {
        this.mImageViews = imageViews;
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImageViews.get(position);
        container.addView(imageView); // 把图片添加到container中
        return imageView; // 把图片返回给框架，用来缓存
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        //object:刚才创建的对象，即要销毁的对象
        container.removeView((View) object);
    }
}
