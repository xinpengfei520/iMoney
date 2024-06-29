package com.xpf.p2p.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.xpf.common.cons.SpKey;
import com.xpf.common.utils.SpUtil;
import com.xpf.common.utils.UIUtils;
import com.xpf.p2p.P2PApplication;
import com.xpf.p2p.R;
import com.xpf.p2p.adapter.VpGuideAdapter;
import com.xpf.p2p.ui.login.view.LoginActivity;

import java.util.ArrayList;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:引导页
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class GuideActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout llPointGroup;
    ImageView ivRedPoint;
    RelativeLayout rlPoints;
    TextView tvRightAway;
    // 两点间的间距
    private int leftMarg;
    private int widthDpi;
    private ArrayList<ImageView> mImageViews;
    private final int mResIds[] = new int[]{R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3, R.drawable.guide4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager = findViewById(R.id.viewPager);
        llPointGroup = findViewById(R.id.llPointGroup);
        ivRedPoint = findViewById(R.id.ivRedPoint);
        rlPoints = findViewById(R.id.rlPoints);
        tvRightAway = findViewById(R.id.tvRightAway);
        tvRightAway.setOnClickListener(v -> {
            startActivity(new Intent(GuideActivity.this, LoginActivity.class));
            finish();
        });
        initData();
        initImageViewList();
    }

    private void initImageViewList() {
        //保存图片资源的集合
        mImageViews = new ArrayList<>();
        ImageView imageView;
        //循环遍历图片资源，然后保存到集合中
        for (int resId : mResIds) {
            //添加图片到集合中
            imageView = new ImageView(this);
            imageView.setBackgroundResource(resId);
            mImageViews.add(imageView);
        }

        if (mImageViews != null && mImageViews.size() > 0) {
            viewPager.setAdapter(new VpGuideAdapter(mImageViews));
        }
    }

    /**
     * 初始化引导页的数据
     */
    private void initData() {
        widthDpi = UIUtils.dp2px(8);
        for (int i = 0; i < mResIds.length; i++) {
            // 添加灰色的点
            ImageView point = new ImageView(this);
            // 给Point设置shape为oval,即圆
            point.setImageResource(R.drawable.point_gray);
            // 设置点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDpi, widthDpi);
            if (i != 0) {
                params.leftMargin = widthDpi; //设置间距
            }
            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }

        /**
         * 求间距 构造方法--> 测量(measure--onMeasure)--> layout-->onLayout-->draw-->onDraw
         */
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        // 监听viewPager页面滑动的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * @param position             当前滑动页面的下标位置
         * @param positionOffset       滑动了页面的百分比
         * @param positionOffsetPixels 滑动了页面多少像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 坐标 = 起始位置 + 红点移动的距离
            float leftMargin = (position + positionOffset) * leftMarg;
            RelativeLayout.LayoutParams paramgs = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            // 距离左边的距离
            paramgs.leftMargin = (int) leftMargin;
            ivRedPoint.setLayoutParams(paramgs);
        }

        @Override
        public void onPageSelected(int position) {
            tvRightAway.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            // 间距 = 第一个点距离左边距离 - 第0个点距离左边距离
            leftMarg = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpUtil.getInstance(P2PApplication.getContext()).save(SpKey.IS_NEED_GUIDE, false);
    }
}
