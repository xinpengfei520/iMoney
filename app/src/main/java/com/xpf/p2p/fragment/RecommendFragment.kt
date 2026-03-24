package com.xpf.p2p.fragment

import com.xpf.p2p.R
import com.xpf.p2p.adapter.StellerAdapter
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.databinding.FragmentProductRecommondBinding

/**
 * Created by xpf on 2016/11/15 :)
 * Function:推荐理财页面
 */
class RecommendFragment : BaseFragment() {

    private var _binding: FragmentProductRecommondBinding? = null
    private val binding get() = _binding!!

    override fun getLayoutId(): Int = R.layout.fragment_product_recommond

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String> = emptyMap()

    override fun initData(content: String?) {
        _binding = FragmentProductRecommondBinding.bind(mView!!)
        val stellerAdapter = StellerAdapter(mContext)
        binding.stellarMap.setAdapter(stellerAdapter)
        binding.stellarMap.setGroup(0, true)
        binding.stellarMap.setRegularity(5, 10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
