package com.xpf.p2p.activity

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity

/**
 * Created by xpf on 2016/11/11 :)
 * Function:饼状图展示页面
 */
class PieChartActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var pieChart: PieChart
    private lateinit var mTf: Typeface

    override fun getLayoutId(): Int = R.layout.activity_pie_chart

    override fun initData() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        pieChart = findViewById(R.id.pieChart)
        ivBack.visibility = View.VISIBLE
        ivSetting.visibility = View.GONE
        tvTitle.text = "饼状图"

        ivBack.setOnClickListener { removeCurrentActivity() }

        mTf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")

        val description = Description()
        description.text = "目前android市场的占比情况"
        pieChart.description = description
        pieChart.holeRadius = 52f
        pieChart.transparentCircleRadius = 67f
        pieChart.centerText = "Android\n市场占比"
        pieChart.setCenterTextTypeface(mTf)
        pieChart.setCenterTextSize(18f)
        pieChart.setUsePercentValues(true)

        val mChartData = generateDataPie()
        mChartData.setValueFormatter(PercentFormatter())
        mChartData.setValueTypeface(mTf)
        mChartData.setValueTextSize(11f)
        mChartData.setValueTextColor(Color.RED)
        pieChart.data = mChartData

        val l = pieChart.legend
        l.yEntrySpace = 10f
        l.yOffset = 30f

        pieChart.animateXY(900, 900)
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
