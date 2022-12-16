package com.example.budgetmanagement.ui.statistics;

import android.graphics.Color;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;
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

    private final StatsCollector statsCollector;
    private PeriodSummary[] periodSummary;
    private final MonthStatisticsBinding binding;
    private int selectedValue;
    private int[] years = new int[0];
    private ArrayList<String> chartLabels = new ArrayList<>();

    public PeriodBarChart(MonthStatisticsBinding binding, ComingViewModel comingViewModel) {
        this.binding = binding;
        statsCollector = new StatsCollector(comingViewModel);
    }

    public void drawChart() {
        BarChart mChart = binding.barChart;
        mChart.invalidate();
        mChart.fitScreen();

        if(mChart.getData() != null)
            mChart.getData().clearValues();

        mChart.setFitBars(true);

        mChart.animateY(1000);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(chartLabels));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

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
        mChart.setData(data);
        mChart.setExtraBottomOffset(10);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(4f);
        mChart.moveViewToX(chartLabels.size());
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.setOnChartValueSelectedListener(this);
    }

    public void setData(PeriodSummary[] periodSummary) {
        this.periodSummary = periodSummary;
    }

    public void setChartStats() {
        PeriodSummary selectedPeriodSummary = periodSummary[selectedValue];
        binding.allTransactionNumber.setText(String.valueOf(selectedPeriodSummary.getNumberOfTransactions()));
        binding.numberOfTransactionsExecutedAfterTheTime.setText(String.valueOf(selectedPeriodSummary.getNumberOfTransactionsExecutedAfterTheTime()));
        binding.numberOfRemainingTransaction.setText(String.valueOf(selectedPeriodSummary.getNumberOfRemainingTransactions()));
    }

    public void setSelectedValue(int selectedValue) {
        this.selectedValue = selectedValue;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        selectedValue = (int) (e.getX() - 1);
        setChartStats();
    }

    public void notifyDataChanged() {
        drawChart();
        setChartStats();
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

    @Override
    public void onNothingSelected() {}
}
