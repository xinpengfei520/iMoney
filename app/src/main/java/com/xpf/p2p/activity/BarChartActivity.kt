package com.xpf.p2p.activity

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.databinding.ActivityBarChartBinding

/**
 * Created by xpf on 2016/11/11 :)
 * Function:柱状图页面
 */
class BarChartActivity : BaseActivity<ActivityBarChartBinding>() {

    private lateinit var mTf: Typeface

    override fun createViewBinding(inflater: LayoutInflater) = ActivityBarChartBinding.inflate(inflater)

    override fun initData() {
        binding.titleBar.ivBack.visibility = View.VISIBLE
        binding.titleBar.ivSetting.visibility = View.GONE
        binding.titleBar.tvTitle.text = "柱状图"

        binding.titleBar.ivBack.setOnClickListener { removeCurrentActivity() }

        mTf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")

        val description = Description()
        description.text = "三星note7爆炸事件关注度"
        binding.barChart.description = description
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.setDrawBarShadow(false)

        val xAxis = binding.barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = binding.barChart.axisLeft
        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)
        leftAxis.spaceTop = 50f

        val rightAxis = binding.barChart.axisRight
        rightAxis.isEnabled = false

        val mChartData = generateDataBar()
        mChartData.setValueTypeface(mTf)

        binding.barChart.data = mChartData
        binding.barChart.animateY(700)
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
