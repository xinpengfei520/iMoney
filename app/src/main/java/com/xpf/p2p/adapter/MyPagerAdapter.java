package com.xpf.p2p.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xpf on 2018/6/10 :)
 * Function:
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public MyPagerAdapter(List<Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // 方式一
        if (position == 0) {
            return "全部理财";
        } else if (position == 1) {
            return "推荐理财";
        } else if (position == 2) {
            return "热门理财";
        } else {
            return "默认标题";
        }

        // 方式二
        // 从strings.xml中读取String构成的数组
        //return UIUtils.getStrArray(R.array.invest_tab)[position];
    }

}
