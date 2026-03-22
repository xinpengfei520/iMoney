package com.xpf.p2p.fragment

import com.loopj.android.http.RequestParams
import com.xpf.p2p.R
import com.xpf.p2p.adapter.StellerAdapter
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.widget.randomLayout.StellarMap

/**
 * Created by xpf on 2016/11/15 :)
 * Function:推荐理财页面
 */
class RecommendFragment : BaseFragment() {

    private lateinit var stellarMap: StellarMap

    override fun getLayoutId(): Int = R.layout.fragment_product_recommond

    override fun getUrl(): String = ""

    override fun getParams(): RequestParams = RequestParams()

    override fun initData(content: String?) {
        stellarMap = mView!!.findViewById(R.id.stellarMap)
        val stellerAdapter = StellerAdapter(mContext)
        stellarMap.setAdapter(stellerAdapter)
        stellarMap.setGroup(0, true)
        stellarMap.setRegularity(5, 10)
    }
}
