package com.example.budgetmanagement.ui.statistics;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.budgetmanagement.R;
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
    private final ArrayList<String> chartLabels = new ArrayList<>();
    private PeriodSummary firstPeriod;
    private PeriodSummary secondPeriod;

    public PeriodComparatorBarChart(Context context) {
        this.context = context;
    }

    public BarChart drawChart() {
        BarChart barChartView = new BarChart(context);
        barChartView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        barChartView.setId(R.id.periodBarChart);

        // (barSpace + barWidth) * 3 + groupSpace = 1
        float groupSpace = 0.16f;
        float barSpace = 0.08f;
        float barWidth = 0.20f;

        BarChartCreator barChartCreator = new BarChartCreator(barChartView);
        barChartCreator.setChartDimensions(groupSpace, barSpace, barWidth);
        barChartCreator.create(chartLabels, getBarData());

        barChartView.setHighlightPerTapEnabled(false);
        barChartView.setVisibleXRangeMaximum(6f);

        return barChartCreator.getBarChart();
    }

    private BarData getBarData() {
        firstPeriod = periodStatsComparator.getFirstPeriod();
        secondPeriod = periodStatsComparator.getSecondPeriod();

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(createLossDataSet());
        dataSets.add(createIncomeDataSet());
        dataSets.add(createProfitDataSet());

        return new BarData(dataSets);
    }

    private BarDataSet createLossDataSet() {
        String lossLegendTag = context.getString(R.string.loss);
        BarDataSet set1 = new BarDataSet(createLossData(), lossLegendTag);
        set1.setHighlightEnabled(false);
        set1.setColors(new int[]{R.color.mat_red}, context);
        return set1;
    }

    private ArrayList<BarEntry> createLossData() {
        ArrayList<BarEntry> loss = new ArrayList<>();
        loss.add(new BarEntry(1, secondPeriod.getLoss()));
        loss.add(new BarEntry(0, firstPeriod.getLoss()));
        return loss;
    }

    private BarDataSet createIncomeDataSet() {
        String incomeLegendTag = context.getString(R.string.income);
        BarDataSet set2 = new BarDataSet(createIncomeData(), incomeLegendTag);
        set2.setHighlightEnabled(false);
        set2.setColors(new int[]{R.color.mat_green}, context);
        return set2;
    }

    private ArrayList<BarEntry> createIncomeData() {
        ArrayList<BarEntry> income = new ArrayList<>();
        income.add(new BarEntry(1, secondPeriod.getIncome()));
        income.add(new BarEntry(0, firstPeriod.getIncome()));
        return income;
    }

    private BarDataSet createProfitDataSet() {
        String profitLegendTag = context.getString(R.string.profit);
        BarDataSet set3 = new BarDataSet(createProfitData(), profitLegendTag);
        set3.setHighlightEnabled(false);
        set3.setColors(new int[]{R.color.mat_blue}, context);
        return set3;
    }

    private ArrayList<BarEntry> createProfitData() {
        ArrayList<BarEntry> profit = new ArrayList<>();
        profit.add(new BarEntry(1, secondPeriod.getProfit()));
        profit.add(new BarEntry(0, firstPeriod.getProfit()));
        return profit;
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
