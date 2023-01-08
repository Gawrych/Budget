package com.example.budgetmanagement.ui.statistics;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.FragmentPeriodComparatorBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Collections;

public class PeriodComparatorBarChart {

    private final Context context;
    private PeriodStatsComparator periodStatsComparator;
    private final FragmentPeriodComparatorBinding binding;
    private ArrayList<String> chartLabels = new ArrayList<>();

    public PeriodComparatorBarChart(FragmentPeriodComparatorBinding binding) {
        this.binding = binding;
        this.context = binding.getRoot().getContext();
    }

    public void drawChart() {
        BarChart barChartView = new BarChart(context);
        barChartView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        barChartView.setId(R.id.periodBarChart);

        // (barSpace + barWidth) * 3 + groupSpace = 1
        float groupSpace = 0.31f;
        float barSpace = 0.08f;
        float barWidth = 0.15f;

        BarChartCreator barChartCreator = new BarChartCreator(barChartView);
        barChartCreator.setChartDimensions(groupSpace, barSpace, barWidth);
        barChartCreator.create(chartLabels, getDataForBarChart());

        barChartView.getLegend().setEnabled(true);
        barChartView.setHighlightPerTapEnabled(false);
        barChartView.setVisibleXRangeMaximum(6f);

        LinearLayout chartLayout = binding.chartLayout;
        chartLayout.removeAllViews();
        chartLayout.addView(barChartCreator.getBarChart());
    }

    private BarData getDataForBarChart() {
        ArrayList<BarEntry> loss = new ArrayList<>();
        ArrayList<BarEntry> income = new ArrayList<>();
        ArrayList<BarEntry> profit = new ArrayList<>();

        PeriodSummary firstPeriod = periodStatsComparator.getFirstPeriod();
        PeriodSummary secondPeriod = periodStatsComparator.getSecondPeriod();

        loss.add(new BarEntry(1, secondPeriod.getLoss()));
        income.add(new BarEntry(1, secondPeriod.getIncome()));
        profit.add(new BarEntry(1, secondPeriod.getProfit()));

        loss.add(new BarEntry(0, firstPeriod.getLoss()));
        income.add(new BarEntry(0, firstPeriod.getIncome()));
        profit.add(new BarEntry(0, firstPeriod.getProfit()));

        String lossLegendTag = context.getResources().getString(R.string.loss);
        BarDataSet set1 = new BarDataSet(loss, lossLegendTag);
        set1.setHighlightEnabled(false);
        set1.setColors(new int[]{R.color.mat_red}, context);

        String incomeLegendTag = context.getResources().getString(R.string.income);
        BarDataSet set2 = new BarDataSet(income, incomeLegendTag);
        set2.setHighlightEnabled(false);
        set2.setColors(new int[]{R.color.mat_green}, context);

        String profitLegendTag = context.getResources().getString(R.string.profit);
        BarDataSet set3 = new BarDataSet(profit, profitLegendTag);
        set3.setHighlightEnabled(false);
        set3.setColors(new int[]{R.color.mat_blue}, context);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        return new BarData(dataSets);
    }

    public void setData(PeriodStatsComparator periodStatsComparator) {
        this.periodStatsComparator = periodStatsComparator;
    }

    public void setLabels(String[] labels) {
        chartLabels.clear();
        chartLabels.add("Chart is skipping this label, but it have to be here");
        Collections.addAll(chartLabels, labels);
        chartLabels.add("Chart is skipping this label, but it have to be here");
    }
}
