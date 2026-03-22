package com.xpf.p2p.activity

import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity

/**
 * Created by xpf on 2016/11/11 :)
 * Function:柱状图页面
 */
class BarChartActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var barChart: BarChart
    private lateinit var mTf: Typeface

    override fun getLayoutId(): Int = R.layout.activity_bar_chart

    override fun initData() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        barChart = findViewById(R.id.barChart)
        ivBack.visibility = View.VISIBLE
        ivSetting.visibility = View.GONE
        tvTitle.text = "柱状图"

        ivBack.setOnClickListener { removeCurrentActivity() }

        mTf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")

        val description = Description()
        description.text = "三星note7爆炸事件关注度"
        barChart.description = description
        barChart.setDrawGridBackground(false)
        barChart.setDrawBarShadow(false)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = barChart.axisLeft
        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)
        leftAxis.spaceTop = 50f

        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        val mChartData = generateDataBar()
        mChartData.setValueTypeface(mTf)

        barChart.data = mChartData
        barChart.animateY(700)
    }

    private fun generateDataBar(): BarData {
        val entries = ArrayList<BarEntry>()
        for (i in 0 until 12) {
            entries.add(BarEntry(i.toFloat(), ((Math.random() * 70) + 30).toFloat()))
        }

        val d = BarDataSet(entries, "New DataSet ")
        d.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
        d.highLightAlpha = 255

        return BarData(d)
    }
}
