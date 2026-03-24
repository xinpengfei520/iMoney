package com.xpf.p2p.activity

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.databinding.ActivityLineChartBinding

/**
 * Created by xpf on 2016/11/11 :)
 * Function:折线图页面
 */
class LineChartActivity : BaseActivity<ActivityLineChartBinding>() {

    private lateinit var mTf: Typeface

    override fun createViewBinding(inflater: LayoutInflater) = ActivityLineChartBinding.inflate(inflater)

    override fun initData() {
        binding.titleBar.ivBack.visibility = View.VISIBLE
        binding.titleBar.tvTitle.text = "折线图"
        binding.titleBar.ivSetting.visibility = View.GONE
        binding.titleBar.ivBack.setOnClickListener { removeCurrentActivity() }

        mTf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")

        val description = Description()
        description.text = "我的资产的变化情况"
        binding.lineChart.description = description
        binding.lineChart.setDrawGridBackground(false)

        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = binding.lineChart.axisLeft
        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)

        val rightAxis = binding.lineChart.axisRight
        rightAxis.typeface = mTf
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.isEnabled = false

        binding.lineChart.data = generateDataLine()
        binding.lineChart.animateX(750)
    }

    private fun generateDataLine(): LineData {
        val entries = ArrayList<Entry>()
        for (i in 0 until 12) {
            entries.add(Entry(i.toFloat(), ((Math.random() * 65) + 40).toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "New DataSet 1")
        lineDataSet.lineWidth = 2.5f
        lineDataSet.circleRadius = 4.5f
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.setDrawValues(false)

        val sets = ArrayList<ILineDataSet>()
        sets.add(lineDataSet)

        return LineData(sets)
    }
}
