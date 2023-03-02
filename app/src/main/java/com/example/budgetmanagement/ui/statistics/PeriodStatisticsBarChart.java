package com.example.budgetmanagement.ui.statistics;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.PeriodStatisticsFragmentBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PeriodStatisticsBarChart implements OnChartValueSelectedListener {

    private final Context context;
    private PeriodSummary[] periodSummary;
    private final PeriodStatisticsFragmentBinding binding;
    private final ArrayList<String> chartLabels = new ArrayList<>();
    private OnValueSelected onValueSelected;
    private int positionToMoveView = 0;

    public PeriodStatisticsBarChart(PeriodStatisticsFragmentBinding binding) {
        this.binding = binding;
        context = binding.getRoot().getContext();
    }

    public void drawChart() {
        BarChart barChartView = new BarChart(context);
        barChartView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        barChartView.setId(R.id.periodBarChart);

        BarChartCreator barChartCreator = new BarChartCreator(barChartView);
        barChartCreator.create(chartLabels, getDataForBarChart());

        barChartView.setOnChartValueSelectedListener(this);
        barChartView.moveViewToX(positionToMoveView);

        LinearLayout chartLayout = binding.chartLayout;
        chartLayout.removeAllViews();
        chartLayout.addView(barChartCreator.getBarChart());
    }

    private BarData getDataForBarChart() {
        ArrayList<BarEntry> loss = new ArrayList<>();
        ArrayList<BarEntry> income = new ArrayList<>();
        for (int i = 0; i < periodSummary.length; i++) {
            income.add(new BarEntry(i, periodSummary[i].getIncome()));
            loss.add(new BarEntry(i, periodSummary[i].getLoss()));
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        addNewSetToDataSet(loss, "loss", R.color.mat_red, dataSets);
        addNewSetToDataSet(income, "income", R.color.mat_green, dataSets);

        return new BarData(dataSets);
    }

    private void addNewSetToDataSet(ArrayList<BarEntry> loss, String loss1, int mat_red, ArrayList<IBarDataSet> dataSets) {
        BarDataSet set1 = new BarDataSet(loss, loss1);
        set1.setHighlightEnabled(true);
        set1.setColors(new int[]{mat_red}, context);
        set1.setHighLightColor(Color.BLACK);
        dataSets.add(set1);
    }

    public void setData(PeriodSummary[] periodSummary) {
        this.periodSummary = periodSummary;
    }

    public void setPositionToMoveView(int position) {
        positionToMoveView = position;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int selectedPos = (int) (e.getX() - 1);
        onValueSelected.onValueSelected(selectedPos);
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
        onValueSelected = listener;
    }

    public interface OnValueSelected {
        void onValueSelected(int pos);
    }

    @Override
    public void onNothingSelected() {}
}
