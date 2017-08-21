package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */

public class LineChartManager {

    private static String lineName = null;
    private static String lineName1 = null;

    private static LineDataSet dataSet;

    private static LineChart chart;

    /**
     * @param context    上下文
     * @param mLineChart 折线图控件
     * @param xValues    折线在x轴的值
     * @param yValue     折线在y轴的值
     * @Description:创建两条折线
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue) {
        initDataStyle(context, mLineChart);
        chart = mLineChart;
        //设置折线的样式
        dataSet = new LineDataSet(yValue, lineName);
        dataSet.setColor(Color.RED);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.invalidate();
    }

    public static void addData(Entry entry) {
        dataSet.addEntry(entry);
        chart.notifyDataSetChanged(); // let the chart know it's data changed
        chart.invalidate();
    }

    public static void clearData() {
        dataSet.clear();
        chart.notifyDataSetChanged(); // let the chart know it's data changed
        chart.invalidate();
    }

    public static void changChartStyle() {

    }

    public static void changeData(List<Float> floatList) {
        dataSet.clear();
        try {
            for (int i = 0; i < floatList.size(); i++) {
                if (floatList != null && floatList.get(i) != null) {
                    Entry entry = new Entry(floatList.get(i), i);
                    dataSet.addEntry(entry);
                }
            }
            chart.notifyDataSetChanged(); // let the chart know it's data changed
            chart.invalidate();
        } catch (NullPointerException e) {

        }

    }

    /**
     * @param context    上下文
     * @param mLineChart 折线图控件
     * @param yValue     折线在y轴的值
     * @param yValue1    另一条折线在y轴的值
     * @Description:创建两条折线
     */
    public static void initDoubleLineChart(Context context, LineChart mLineChart,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1, ArrayList<Entry> yValue2) {

        initDataStyle(context, mLineChart);

        LineDataSet dataSet = new LineDataSet(yValue, "间隙1");
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2);

        LineDataSet dataSet1 = new LineDataSet(yValue1, "间隙2");
        dataSet1.setColor(Color.parseColor("#66CDAA"));
        dataSet1.setCircleColor(Color.parseColor("#66CDAA"));
        dataSet1.setDrawValues(false);
        dataSet1.setDrawCircles(false);
        dataSet1.setLineWidth(2);


        LineDataSet dataSet2 = new LineDataSet(yValue2, "间隙3");
        dataSet2.setColor(Color.parseColor("#000000"));
        dataSet2.setCircleColor(Color.parseColor("#000000"));
        dataSet2.setDrawValues(false);
        dataSet2.setDrawCircles(false);
        dataSet2.setLineWidth(2);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet);
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(2000, Easing.EasingOption.Linear);
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     * @param context
     * @param mLineChart
     * @Description:初始化图表的样式
     */
    private static void initDataStyle(Context context, LineChart mLineChart) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(false);
        mLineChart.setScaleEnabled(false);
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
        title.setEnabled(true);

        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.parseColor("#66CDAA"));
        xAxis.setAxisMaxValue(100);
        xAxis.setAxisMinValue(0);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        //设置是否显示x轴
        xAxis.setEnabled(false);


        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

    }

    /**
     * @param name
     * @Description:设置折线的名称
     */
    public static void setLineName(String name) {
        lineName = name;
    }

    /**
     * @param name
     * @Description:设置另一条折线的名称
     */
    public static void setLineName1(String name) {
        lineName1 = name;
    }
}
