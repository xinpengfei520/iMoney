package com.xpf.p2p.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.xpf.p2p.R
import com.xpf.p2p.adapter.InvestPagerAdapter
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.databinding.FragmentInvestBinding

/**
 * Created by xpf on 2016/11/11 :)
 * Function:我要投资页面
 */
class InvestFragment : BaseFragment() {

    private var _binding: FragmentInvestBinding? = null
    private val binding get() = _binding!!

    private val tabTitles = arrayOf("全部理财", "推荐理财", "热门理财")

    override fun getLayoutId(): Int = R.layout.fragment_invest

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String>? = null

    override fun initData(content: String?) {
        _binding = FragmentInvestBinding.bind(mView!!)

        val fragments = listOf<Fragment>(
            ProductListFragment(),
            RecommendFragment(),
            ProductHotFragment()
        )

        binding.investViewPager.adapter = InvestPagerAdapter(this, fragments)

        TabLayoutMediator(binding.tabLayout, binding.investViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
