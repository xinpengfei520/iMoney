package com.xpf.p2p.fragment;


import android.content.Context;
import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.loopj.android.http.RequestParams;
import com.xpf.p2p.R;
import com.xpf.p2p.adapter.MyPagerAdapter;
import com.xpf.p2p.base.BaseFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:我要投资页面
 */

public class InvestFragment extends BaseFragment {

    MagicIndicator tabIndicator;
    ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitleDataList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invest;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {
        tabIndicator = mView.findViewById(R.id.tab_indicator);
        mViewPager = mView.findViewById(R.id.invest_viewPager);
        initFragments();
        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        mTitleDataList.add("全部理财");
        mTitleDataList.add("推荐理财");
        mTitleDataList.add("热门理财");

        if (mFragments != null && !mFragments.isEmpty()) {
            mAdapter = new MyPagerAdapter(mFragments, getParentFragmentManager());
            // 显示列表
            mViewPager.setAdapter(mAdapter);

            CommonNavigator commonNavigator = new CommonNavigator(getContext());
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                @Override
                public int getCount() {
                    return mTitleDataList == null ? 0 : mTitleDataList.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int index) {
                    ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                    colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                    colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                    colorTransitionPagerTitleView.setText(mTitleDataList.get(index));
                    colorTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                    return colorTransitionPagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                    return indicator;
                }
            });
            tabIndicator.setNavigator(commonNavigator);
            // 关联TabPagerIndicator和ViewPager
            ViewPagerHelper.bind(tabIndicator, mViewPager);
        }
    }

    /**
     * 初始化3个Fragment并保存在集合中
     */
    private void initFragments() {
        mFragments.add(new ProductListFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new ProductHotFragment());
    }

}
