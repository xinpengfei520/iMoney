package com.xpf.p2p.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.itsronald.widget.ViewPagerIndicator;
import com.xpf.common.cons.SpKey;
import com.xpf.common.utils.SpUtil;
import com.xpf.p2p.P2PApplication;
import com.xpf.p2p.R;
import com.xpf.p2p.adapter.VpGuideAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by x-sir :)
 * Function:引导页
 */
public class GuideActivity extends Activity {

    @BindView(R.id.view_pager_indicator)
    ViewPagerIndicator viewPagerIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private int mResIds[] = new int[]{R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3, R.drawable.guide4};
    private ArrayList<ImageView> mImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //保存图片资源的集合
        mImageViews = new ArrayList<>();
        ImageView imageView;
        //循环遍历图片资源，然后保存到集合中
        for (int id : mResIds) {
            //添加图片到集合中
            imageView = new ImageView(this);
            imageView.setBackgroundResource(id);
            mImageViews.add(imageView);
        }

        if (mImageViews != null && mImageViews.size() > 0) {
            viewPager.setAdapter(new VpGuideAdapter(mImageViews));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpUtil.getInstance(P2PApplication.getContext()).save(SpKey.IS_NEED_GUIDE, false);
    }
}
