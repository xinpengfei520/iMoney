package com.xpf.p2p.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by xpf on 2018/6/10 :)
 * Function:
 */
@Suppress("DEPRECATION")
class MyPagerAdapter(
    private val mFragments: List<Fragment>,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "全部理财"
            1 -> "推荐理财"
            2 -> "热门理财"
            else -> "默认标题"
        }
    }
}
