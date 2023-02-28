package com.example.budgetmanagement.ui.statistics;

import android.graphics.Color;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class BarChartCreator {

    private final BarChart barChart;
    private float barSpace = 0.1f;
    private float groupSpace = 0.2f;
    private float barWidth = 0.3f;

    public BarChartCreator(BarChart barChart) {
        this.barChart = barChart;
    }

    public void create(ArrayList<String> chartLabels, BarData data) {
        data.setBarWidth(barWidth);
        setXAxis(chartLabels);
        setYAxis();
        setBarChartOptions(chartLabels, data);
        this.barChart.invalidate();
    }

    private void setBarChartOptions(ArrayList<String> chartLabels, BarData data) {
        this.barChart.setHighlightPerTapEnabled(true);
        this.barChart.setDrawBarShadow(false);
        this.barChart.getDescription().setEnabled(false);
        this.barChart.setPinchZoom(false);
        this.barChart.setScaleEnabled(false);
        this.barChart.setDrawGridBackground(false);
        this.barChart.getAxisRight().setEnabled(false);
        this.barChart.getLegend().setEnabled(false);
        this.barChart.setData(data);
        this.barChart.setExtraBottomOffset(10);
        this.barChart.setVisibleXRangeMaximum(4f);
        this.barChart.moveViewToX(chartLabels.size());
        this.barChart.groupBars(1f, groupSpace, barSpace);
        this.barChart.resetZoom();
        this.barChart.setFitBars(true);
    }

    private void setYAxis() {
        YAxis leftAxis = this.barChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(4, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
    }

    private void setXAxis(ArrayList<String> chartLabels) {
        XAxis xAxis = this.barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(chartLabels));
        xAxis.setAxisMaximum(chartLabels.size() - 1.1f);
    }

    public void setChartDimensions(float groupSpace, float barSpace, float barWidth) {
        this.groupSpace = groupSpace;
        this.barSpace = barSpace;
        this.barWidth = barWidth;
    }

    public BarChart getBarChart() {
        return barChart;
    }
}
