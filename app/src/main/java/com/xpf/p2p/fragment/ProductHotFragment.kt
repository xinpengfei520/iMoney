package com.xpf.p2p.fragment

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.loopj.android.http.RequestParams
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.utils.DrawUtils
import com.xpf.p2p.utils.UIUtils
import com.xpf.p2p.widget.FlowLayout
import java.util.Random

/**
 * Created by xpf on 2016/11/15 :)
 * Function:热门理财页面
 */
class ProductHotFragment : BaseFragment() {

    private lateinit var flowLayout: FlowLayout

    private val mData = arrayOf(
        "新手计划", "乐享活系列90天计划", "钱包", "30天理财计划(加息2%)",
        "林业局投资商业经营与大捞一笔", "中学老师购买车辆", "屌丝下海经商计划", "新西游影视拍", "Java培训老师自己周转",
        "HelloWorld", "C++-C-ObjectC-java", "Android vs ios", "算法与数据结构", "JNI与NDK", "team working"
    )

    override fun getLayoutId(): Int = R.layout.fragment_product_hot

    override fun getUrl(): String = ""

    override fun getParams(): RequestParams? = null

    override fun initData(content: String?) {
        flowLayout = mView!!.findViewById(R.id.flow_layout)
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
            flowLayout.addView(tv)
        }
    }
}
