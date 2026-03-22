package com.xpf.p2p.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xpf.p2p.utils.UIUtils
import com.xpf.p2p.widget.randomLayout.StellarMap
import java.util.Random

/**
 * Created by xpf on 2017/12/9 :)
 * Function:
 */
class StellerAdapter(private val context: Context?) : StellarMap.Adapter {

    private val datas = arrayOf(
        "超级新手计划", "乐享活系列90天计划", "钱包计划", "30天理财计划(加息2%)", "90天理财计划(加息5%)", "180天理财计划(加息10%)",
        "林业局投资商业经营", "中学老师购买车辆", "屌丝下海经商计划", "新西游影视拍摄投资", "公司老板资金周转", "养猪场扩大经营",
        "旅游公司扩大规模", "阿里巴巴洗钱计划", "铁路局回款计划", "高级白领赢取白富美投资计划"
    )

    private val firstDatas = Array(datas.size / 2) { "" }
    private val secondDatas = Array(datas.size - datas.size / 2) { "" }
    private val random = Random()

    init {
        initData()
    }

    private fun initData() {
        for (i in datas.indices) {
            if (i < datas.size / 2) {
                firstDatas[i] = datas[i]
            } else {
                secondDatas[i - datas.size / 2] = datas[i]
            }
        }
    }

    override fun getGroupCount(): Int = 2

    override fun getCount(group: Int): Int {
        return if (group == 0) datas.size / 2 else datas.size - datas.size / 2
    }

    override fun getView(group: Int, position: Int, convertView: View?): View {
        val tv = TextView(context)
        if (group == 0) {
            tv.text = firstDatas[position]
        } else {
            tv.text = secondDatas[position]
        }

        val red = random.nextInt(200)
        val green = random.nextInt(200)
        val blue = random.nextInt(200)

        tv.setTextColor(Color.rgb(red, green, blue))
        tv.textSize = (random.nextInt(UIUtils.dp2px(10)) + UIUtils.dp2px(5)).toFloat()

        tv.setOnClickListener {
            Log.e("TAG", "context = $context")
            Toast.makeText(context, tv.text, Toast.LENGTH_SHORT).show()
        }

        return tv
    }

    override fun getNextGroupOnPan(group: Int, degree: Float): Int = 0

    override fun getNextGroupOnZoom(group: Int, isZoomIn: Boolean): Int {
        return if (group == 0) 1 else 0
    }
}
