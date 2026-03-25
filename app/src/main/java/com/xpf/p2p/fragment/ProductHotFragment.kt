package com.xpf.p2p.fragment

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.databinding.FragmentProductHotBinding
import com.xpf.p2p.utils.DrawUtils
import com.xpf.p2p.utils.UIUtils
import java.util.Random

/**
 * Created by xpf on 2016/11/15 :)
 * Function:热门理财页面
 */
class ProductHotFragment : BaseFragment() {

    private var _binding: FragmentProductHotBinding? = null
    private val binding get() = _binding!!

    private val mData = arrayOf(
        "新手专享计划", "乐享活期90天", "零钱宝", "30天稳健计划(加息2%)",
        "小微企业经营周转", "个人消费信贷", "供应链金融计划", "影视文化产业基金", "教育分期项目",
        "智能定投组合", "季度增利计划", "年度稳盈计划", "灵活申赎产品", "新能源产业基金", "科技创新计划"
    )

    override fun getLayoutId(): Int = R.layout.fragment_product_hot

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String>? = null

    override fun initData(content: String?) {
        _binding = FragmentProductHotBinding.bind(mView!!)
        val random = Random()

        for (text in mData) {
            val tv = TextView(activity)
            tv.text = text
            tv.textSize = UIUtils.dp2px(6).toFloat()

            val mp = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            mp.leftMargin = UIUtils.dp2px(8)
            mp.topMargin = UIUtils.dp2px(8)
            mp.rightMargin = UIUtils.dp2px(8)
            mp.bottomMargin = UIUtils.dp2px(8)

            tv.layoutParams = mp

            val red = random.nextInt(210)
            val green = random.nextInt(210)
            val blue = random.nextInt(210)

            tv.background = DrawUtils.getSelector(
                DrawUtils.getDrawable(Color.rgb(red, green, blue), UIUtils.dp2px(4).toFloat()),
                DrawUtils.getDrawable(Color.WHITE, UIUtils.dp2px(4).toFloat())
            )

            tv.setOnClickListener {
                Toast.makeText(activity, tv.text, Toast.LENGTH_SHORT).show()
            }

            val padding = UIUtils.dp2px(4)
            tv.setPadding(padding, padding, padding, padding)
            binding.flowLayout.addView(tv)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
