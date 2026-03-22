package com.xpf.p2p.fragment

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.loopj.android.http.RequestParams
import com.xpf.p2p.R
import com.xpf.p2p.adapter.MyPagerAdapter
import com.xpf.p2p.base.BaseFragment
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Created by xpf on 2016/11/11 :)
 * Function:我要投资页面
 */
class InvestFragment : BaseFragment() {

    private lateinit var tabIndicator: MagicIndicator
    private lateinit var mViewPager: ViewPager
    private var mAdapter: MyPagerAdapter? = null
    private val mFragments = ArrayList<Fragment>()
    private val mTitleDataList = ArrayList<String>()

    override fun getLayoutId(): Int = R.layout.fragment_invest

    override fun getUrl(): String = ""

    override fun getParams(): RequestParams? = null

    override fun initData(content: String?) {
        tabIndicator = mView!!.findViewById(R.id.tab_indicator)
        mViewPager = mView!!.findViewById(R.id.invest_viewPager)
        initFragments()
        setViewPagerAdapter()
    }

    private fun setViewPagerAdapter() {
        mTitleDataList.add("全部理财")
        mTitleDataList.add("推荐理财")
        mTitleDataList.add("热门理财")

        if (mFragments.isNotEmpty()) {
            mAdapter = MyPagerAdapter(mFragments, parentFragmentManager)
            mViewPager.adapter = mAdapter

            val commonNavigator = CommonNavigator(context)
            commonNavigator.adapter = object : CommonNavigatorAdapter() {

                override fun getCount(): Int = mTitleDataList.size

                override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                    val titleView = ColorTransitionPagerTitleView(context)
                    titleView.normalColor = Color.GRAY
                    titleView.selectedColor = Color.BLACK
                    titleView.text = mTitleDataList[index]
                    titleView.setOnClickListener { mViewPager.currentItem = index }
                    return titleView
                }

                override fun getIndicator(context: Context): IPagerIndicator {
                    val indicator = LinePagerIndicator(context)
                    indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    return indicator
                }
            }
            tabIndicator.navigator = commonNavigator
            ViewPagerHelper.bind(tabIndicator, mViewPager)
        }
    }

    private fun initFragments() {
        mFragments.add(ProductListFragment())
        mFragments.add(RecommendFragment())
        mFragments.add(ProductHotFragment())
    }
}
