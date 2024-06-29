package com.xpf.p2p.fragment;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.TabPageIndicator;
import com.xpf.common.base.BaseFragment;
import com.xpf.p2p.R;
import com.xpf.p2p.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:我要投资页面
 */

public class InvestFragment extends BaseFragment {

    TabPageIndicator tabIndicator;
    ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<>();

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
        tabIndicator = (TabPageIndicator) mView.findViewById(R.id.tab_indicator);
        mViewPager = (ViewPager) mView.findViewById(R.id.invest_viewPager);
        initFragments();
        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        if (mFragments != null && mFragments.size() > 0) {
            mAdapter = new MyPagerAdapter(mFragments, getFragmentManager());
            // 显示列表
            mViewPager.setAdapter(mAdapter);
            // 关联TabPagerIndicator和ViewPager
            tabIndicator.setViewPager(mViewPager);
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
