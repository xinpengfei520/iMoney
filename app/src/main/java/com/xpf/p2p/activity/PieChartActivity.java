package com.xpf.p2p.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xpf.p2p.R;
import com.xpf.p2p.base.BaseActivity;

import java.util.ArrayList;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:饼状图展示页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class PieChartActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivSetting;
    private PieChart pieChart;

    private Typeface mTf;

    @Override
    protected void initData() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        pieChart = findViewById(R.id.pieChart);
        ivBack.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.GONE);
        tvTitle.setText("饼状图");

        ivBack.setOnClickListener(v -> removeCurrentActivity());

        // 初始化字体库
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Description description = new Description();
        description.setText("目前android市场的占比情况");
        pieChart.setDescription(description);
        // 设置内部圆的半径
        pieChart.setHoleRadius(52f);
        // 设置包裹内部圆的半径
        pieChart.setTransparentCircleRadius(67f);

        pieChart.setCenterText("Android\n市场占比");
        // 设置中间显示的文本的字体
        pieChart.setCenterTextTypeface(mTf);
        // 设置中间显示的文本的字体大小
        pieChart.setCenterTextSize(18f);
        // 显示的各个部分的占比和是否为100%
        pieChart.setUsePercentValues(true);

        // 产生饼状图的数据
        PieData mChartData = generateDataPie();

        // 设置显示数据的格式
        mChartData.setValueFormatter(new PercentFormatter());
        mChartData.setValueTypeface(mTf);
        // 设置显示各个部分的文字的字体大小
        mChartData.setValueTextSize(11f);
        // 设置显示各个部分的文字的字体颜色
        mChartData.setValueTextColor(Color.RED);
        // set data
        pieChart.setData(mChartData);

        //获取图示的说明结构
        Legend l = pieChart.getLegend();
        //设置显示的位置
        //l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //设置几项说明在Y轴方向的间距
        l.setYEntrySpace(10f);
        //设置第一项距离y轴顶部的间距
        l.setYOffset(30f);

        // do not forget to refresh the chart
        // pieChart.invalidate();
        pieChart.animateXY(900, 900);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pie_chart;
    }

    /**
     * generates a random ChartData object with just one DataSet
     */
    private PieData generateDataPie() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((int) (Math.random() * 70) + 30, i));
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        // 设置各个扇形部分之间的间距
        pieDataSet.setSliceSpace(12f);
        // 设置各个扇形部分的颜色
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        //PieData pieData = new PieData(getQuarters(), pieDataSet);
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    private ArrayList<String> getQuarters() {
        ArrayList<String> q = new ArrayList<>();
        q.add("三星");
        q.add("apple");
        q.add("华为");
        q.add("oppo");
        return q;
    }
}
