package com.xpf.p2p.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xpf.p2p.R;
import com.xpf.p2p.common.BaseFragment;
import com.xpf.p2p.utils.UIUtils;
import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:我要投资页面
 */

public class InvestFragment extends BaseFragment {

    @BindView(R.id.tab_indicator)
    TabPageIndicator tabIndicator;
    @BindView(R.id.invest_viewPager)
    ViewPager investViewPager;

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
        initFragments();
        // 显示列表
        investViewPager.setAdapter(new MyAdapter(getFragmentManager()));
        // 关联TabPagerIndicator和ViewPager
        tabIndicator.setViewPager(investViewPager);
    }

    private List<Fragment> fragments = new ArrayList<>();

    // 初始化3个Fragment并保存在集合中
    private void initFragments() {

        ProductListFragment productListFragment = new ProductListFragment();
        RecommendFragment recommendFragment = new RecommendFragment();
        ProductHotFragment productHotFragment = new ProductHotFragment();

        fragments.add(productListFragment);
        fragments.add(recommendFragment);
        fragments.add(productHotFragment);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //方式一
//            if(position == 0){
//                return "全部理财";
//            }else if(position == 1){
//                return "推荐理财";
//            }else ..
            // 方式二
            // 从strings.xml中读取String构成的数组
            return UIUtils.getStrArray(R.array.invest_tab)[position];
        }
    }

}
