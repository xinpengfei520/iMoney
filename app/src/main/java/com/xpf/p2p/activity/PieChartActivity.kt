package com.xpf.p2p.activity

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.databinding.ActivityPieChartBinding

/**
 * Created by xpf on 2016/11/11 :)
 * Function:饼状图展示页面
 */
class PieChartActivity : BaseActivity<ActivityPieChartBinding>() {

    private lateinit var mTf: Typeface

    override fun createViewBinding(inflater: LayoutInflater) = ActivityPieChartBinding.inflate(inflater)

    override fun initData() {
        binding.titleBar.ivBack.visibility = View.VISIBLE
        binding.titleBar.ivSetting.visibility = View.GONE
        binding.titleBar.tvTitle.text = "饼状图"

        binding.titleBar.ivBack.setOnClickListener { removeCurrentActivity() }

        mTf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")

        val description = Description()
        description.text = "目前android市场的占比情况"
        binding.pieChart.description = description
        binding.pieChart.holeRadius = 52f
        binding.pieChart.transparentCircleRadius = 67f
        binding.pieChart.centerText = "Android\n市场占比"
        binding.pieChart.setCenterTextTypeface(mTf)
        binding.pieChart.setCenterTextSize(18f)
        binding.pieChart.setUsePercentValues(true)

        val mChartData = generateDataPie()
        mChartData.setValueFormatter(PercentFormatter())
        mChartData.setValueTypeface(mTf)
        mChartData.setValueTextSize(11f)
        mChartData.setValueTextColor(Color.RED)
        binding.pieChart.data = mChartData

        val l = binding.pieChart.legend
        l.yEntrySpace = 10f
        l.yOffset = 30f

        binding.pieChart.animateXY(900, 900)
    }

    private fun generateDataPie(): PieData {
        val entries = ArrayList<PieEntry>()
        for (i in 0 until 4) {
            entries.add(PieEntry(((Math.random() * 70) + 30).toFloat(), i.toFloat()))
        }

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.sliceSpace = 12f
        pieDataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toList()

        return PieData(pieDataSet)
    }
}
