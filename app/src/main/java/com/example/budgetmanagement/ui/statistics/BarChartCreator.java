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
        barChart.setHighlightPerTapEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(chartLabels));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(5, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        data.setBarWidth(barWidth);
        xAxis.setAxisMaximum(chartLabels.size() - 1.1f);
        barChart.setData(data);
        barChart.setExtraBottomOffset(10);
        barChart.setVisibleXRangeMaximum(4f);
        barChart.moveViewToX(chartLabels.size());
        barChart.groupBars(1f, groupSpace, barSpace);
        barChart.resetZoom();
        barChart.setFitBars(true);
        barChart.invalidate();
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
