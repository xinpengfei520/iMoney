package com.xpf.p2p.activity

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity

/**
 * Created by xpf on 2016/11/11 :)
 * Function:折线图页面
 */
class LineChartActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var lineChart: LineChart
    private lateinit var mTf: Typeface

    override fun getLayoutId(): Int = R.layout.activity_line_chart

    override fun initData() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        lineChart = findViewById(R.id.lineChart)
        ivBack.visibility = View.VISIBLE
        tvTitle.text = "折线图"
        ivSetting.visibility = View.GONE
        ivBack.setOnClickListener { removeCurrentActivity() }

        mTf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")

        val description = Description()
        description.text = "我的资产的变化情况"
        lineChart.description = description
        lineChart.setDrawGridBackground(false)

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = lineChart.axisLeft
        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)

        val rightAxis = lineChart.axisRight
        rightAxis.typeface = mTf
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.isEnabled = false

        lineChart.data = generateDataLine()
        lineChart.animateX(750)
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
