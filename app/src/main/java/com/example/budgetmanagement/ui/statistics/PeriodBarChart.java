package com.example.budgetmanagement.ui.statistics;

import android.graphics.Color;
import android.widget.LinearLayout;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.FragmentPeriodStatisticsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PeriodBarChart implements OnChartValueSelectedListener {

    private BarChart barChart;
    private PeriodSummary[] periodSummary;
    private final FragmentPeriodStatisticsBinding binding;
    private ArrayList<String> chartLabels = new ArrayList<>();
    private OnValueSelected onValueSelected;

    public PeriodBarChart(FragmentPeriodStatisticsBinding binding, BarChart barChart) {
        this.binding = binding;
        this.barChart = barChart;
    }

    public void drawChart() {
        LinearLayout chartLayout = binding.chartLayout;
        chartLayout.removeAllViews();
        barChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        barChart.setId(R.id.periodBarChart);

        barChart.invalidate();
        barChart.resetZoom();
        barChart.setFitBars(true);
        barChart.fitScreen();
        barChart.resetPivot();
        barChart.resetTracking();
        barChart.resetViewPortOffsets();

        if(barChart.getData() != null)
            barChart.getData().clearValues();

        barChart.setFitBars(true);

        barChart.setHighlightPerTapEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
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
        leftAxis.setAxisMinimum(0);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        ArrayList<BarEntry> loss = new ArrayList<>();
        ArrayList<BarEntry> income = new ArrayList<>();
        for (int i = 0; i < periodSummary.length; i++) {
            income.add(new BarEntry(i, periodSummary[i].getIncome()));
            loss.add(new BarEntry(i, periodSummary[i].getLoss()));
        }

        BarDataSet set1 = new BarDataSet(loss, "loss");
        set1.setHighlightEnabled(true);
        set1.setColors(new int[]{R.color.mat_red}, binding.getRoot().getContext());
        set1.setHighLightColor(Color.BLACK);
        BarDataSet set2 = new BarDataSet(income, "income");
        set2.setHighlightEnabled(true);
        set2.setColors(new int[]{R.color.mat_green}, binding.getRoot().getContext());
        set2.setHighLightColor(Color.BLACK);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.2f;
        float barSpace = 0.1f;
        float barWidth = 0.3f;
        data.setBarWidth(barWidth);
        xAxis.setAxisMaximum(chartLabels.size() - 1.1f);
        barChart.setData(data);
        barChart.setExtraBottomOffset(10);
        barChart.setScaleEnabled(false);
        barChart.setVisibleXRangeMaximum(4f);
        barChart.moveViewToX(chartLabels.size());
        barChart.groupBars(1f, groupSpace, barSpace);
        barChart.invalidate();
        barChart.setOnChartValueSelectedListener(this);
        chartLayout.addView(barChart);
    }

    public void setData(PeriodSummary[] periodSummary) {
        this.periodSummary = periodSummary;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int selectedPos = (int) (e.getX() - 1);
        this.onValueSelected.onValueSelected(selectedPos);
    }

    public void notifyDataChanged() {
        drawChart();
    }

    public void setYearsAsLabels(int[] years) {
        String[] labels = Arrays.stream(years)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        chartLabels.clear();
        chartLabels.add("Chart is skipping this line, but it have to be here");
        Collections.addAll(chartLabels, labels);
        chartLabels.add("Chart is skipping this line, but it have to be here");
    }

    public void setMonthsAsLabels(String[] labels) {
        chartLabels.clear();
        chartLabels.add("Chart is skipping this line, but it have to be here");
        Collections.addAll(chartLabels, labels);
        chartLabels.add("Chart is skipping this line, but it have to be here");
    }

    public void setOnValueSelected(OnValueSelected listener) {
        this.onValueSelected = listener;
    }

    public interface OnValueSelected {
        void onValueSelected(int pos);
    }

    @Override
    public void onNothingSelected() {}
}
