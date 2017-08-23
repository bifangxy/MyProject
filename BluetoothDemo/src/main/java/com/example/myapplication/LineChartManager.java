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

    private static LineDataSet dataSet1;

    private static LineDataSet dataSet2;

    private static LineDataSet dataSet3;

    private static LineDataSet dataSet4;

    private static LineChart chart;

    private static LineChart chart1;

    private static LineChart chart2;

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
                    Entry entry = new Entry(i, floatList.get(i));
                    dataSet.addEntry(entry);
                }
            }
            chart1.notifyDataSetChanged();
            chart1.invalidate();
        } catch (NullPointerException e) {

        }

    }

    public static void changeData1(List<Float> floatList) {
        dataSet1.clear();
        try {
            for (int i = 0; i < floatList.size(); i++) {
                if (floatList != null && floatList.get(i) != null) {
                    Entry entry = new Entry(i, floatList.get(i));
                    dataSet1.addEntry(entry);
                }
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            dataSets.add(dataSet1);
            dataSets.add(dataSet2);
            LineData lineData = new LineData(dataSets);
            chart1.setData(lineData);
            chart1.invalidate();
        } catch (NullPointerException e) {

        }

    }

    public static void changeData2(List<Float> floatList) {
        dataSet2.clear();
        try {
            for (int i = 0; i < floatList.size(); i++) {
                if (floatList != null && floatList.get(i) != null) {
                    Entry entry = new Entry(i, floatList.get(i));
                    dataSet2.addEntry(entry);
                }
            }
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(dataSet);
//            dataSets.add(dataSet1);
//            dataSets.add(dataSet2);
//            LineData lineData = new LineData(dataSets);
            chart1.notifyDataSetChanged();
            chart1.invalidate();
        } catch (NullPointerException e) {

        }

    }

    public static void changeData3(List<Float> floatList) {
        dataSet3.clear();
        try {
            for (int i = 0; i < floatList.size(); i++) {
                if (floatList != null && floatList.get(i) != null) {
                    Entry entry = new Entry(i, floatList.get(i));
                    dataSet3.addEntry(entry);
                }
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet3);
            dataSets.add(dataSet4);
            LineData lineData = new LineData(dataSets);
            chart2.setData(lineData);
            chart2.invalidate();
        } catch (NullPointerException e) {

        }

    }

    public static void changeData4(List<Float> floatList) {
        dataSet4.clear();
        try {
            for (int i = 0; i < floatList.size(); i++) {
                if (floatList != null && floatList.get(i) != null) {
                    Entry entry = new Entry(i, floatList.get(i));
                    dataSet4.addEntry(entry);
                }
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet3);
            dataSets.add(dataSet4);
            LineData lineData = new LineData(dataSets);
            chart2.setData(lineData);
            chart2.invalidate();
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
    public static void initThridLineChart(Context context, LineChart mLineChart,
                                          ArrayList<Entry> yValue, ArrayList<Entry> yValue1, ArrayList<Entry> yValue2) {
        chart1 = mLineChart;
        initDataStyle(context, chart1);
        dataSet = new LineDataSet(yValue, "间隙1");
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2);

        dataSet1 = new LineDataSet(yValue1, "间隙2");
        dataSet1.setColor(Color.parseColor("#66CDAA"));
        dataSet1.setCircleColor(Color.parseColor("#66CDAA"));
        dataSet1.setDrawValues(false);
        dataSet1.setDrawCircles(false);
        dataSet1.setLineWidth(2);


        dataSet2 = new LineDataSet(yValue2, "间隙3");
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
        mLineChart.invalidate();
    }

    public static void initDoubleLineChart(Context context, LineChart mLineChart,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1) {
        chart2 = mLineChart;
        initDataStyle2(context, chart2);

        dataSet3 = new LineDataSet(yValue, "加速度1");
        dataSet3.setColor(Color.RED);
        dataSet3.setCircleColor(Color.RED);
        dataSet3.setDrawValues(false);
        dataSet3.setDrawCircles(false);
        dataSet3.setLineWidth(2);

        dataSet4 = new LineDataSet(yValue1, "加速度2");
        dataSet4.setColor(Color.parseColor("#66CDAA"));
        dataSet4.setCircleColor(Color.parseColor("#66CDAA"));
        dataSet4.setDrawValues(false);
        dataSet4.setDrawCircles(false);
        dataSet4.setLineWidth(2);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet3);
        dataSets.add(dataSet4);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
//        mLineChart.animateY(2000, Easing.EasingOption.Linear);
//        mLineChart.animateX(2000, Easing.EasingOption.Linear);
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
        xAxis.setAxisMaxValue(200);
        xAxis.setAxisMinValue(0);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        //设置是否显示x轴
        xAxis.setEnabled(false);
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setAxisMaximum(30);

        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

    }

    private static void initDataStyle2(Context context, LineChart mLineChart) {
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
        xAxis.setAxisMaxValue(200);
        xAxis.setAxisMinValue(0);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        //设置是否显示x轴
        xAxis.setEnabled(false);
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(-55);
        yAxisLeft.setAxisMaximum(55);
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
