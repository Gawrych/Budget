package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;

import android.content.Context;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.github.mikephil.charting.charts.BarChart;

import java.util.Calendar;
import java.util.HashMap;

public class PeriodComparatorElementsCreator {

    private final Context context;
    private final ComingViewModel comingViewModel;
    private int firstMonth;
    private int firstYear;
    private int secondMonth;
    private int secondYear;
    private String[] labels;
    private PeriodStatsComparator statsComparator;
    private BottomSheetMonthYearPicker datesPicker;

    public PeriodComparatorElementsCreator(Context context, ComingViewModel comingViewModel) {
        this.context = context;
        this.comingViewModel = comingViewModel;
        setDefaultDates();
        initializePeriodPicker();
    }

    private void setDefaultDates() {
        Calendar currentDate = Calendar.getInstance();
        this.firstMonth = currentDate.get(Calendar.MONTH);
        this.firstYear = currentDate.get(Calendar.YEAR);

        currentDate.add(Calendar.MONTH, -1);
        this.secondMonth = currentDate.get(Calendar.MONTH);
        this.secondYear = currentDate.get(Calendar.YEAR);
    }

    private void initializePeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, firstYear, firstMonth, secondYear, secondMonth);
        datesPicker.setDatesFromBundle();
        datesPicker.setCancelable(false);
    }

    public void setMonthsModeLabelsForChart() {
        String[] labels = new String[2];
        String[] monthsNames = context.getResources().getStringArray(R.array.months);
        labels[1] = monthsNames[firstMonth] + " " + firstYear;
        labels[0] = monthsNames[secondMonth] + " " + secondYear;
        this.labels = labels;
    }

    public void setYearsModeLabelsForChart() {
        String[] labels = new String[2];
        labels[1] = String.valueOf(firstYear);
        labels[0] = String.valueOf(secondYear);
        this.labels = labels;
    }

    public void setMonthsSummaryStats() {
        MonthsStatsCollector monthsStatsCollector = new MonthsStatsCollector(comingViewModel);
        PeriodSummary[] firstPeriodSummary = monthsStatsCollector.getStats(firstYear);
        PeriodSummary[] secondPeriodSummary = monthsStatsCollector.getStats(secondYear);
        this.statsComparator = new PeriodStatsComparator(firstPeriodSummary[firstMonth], secondPeriodSummary[secondMonth]);
    }

    public void setYearsSummaryStats() {
        YearsStatsCollector yearsStatsCollector = new YearsStatsCollector(comingViewModel);
        HashMap<Integer, PeriodSummary> yearsSummary = yearsStatsCollector.getStats();

        PeriodSummary firstPeriod = yearsSummary.getOrDefault(firstYear, new PeriodSummary());
        PeriodSummary secondPeriod = yearsSummary.getOrDefault(secondYear, new PeriodSummary());

        this.statsComparator = new PeriodStatsComparator(firstPeriod, secondPeriod);
    }

    public BarChart createBarChart() {
        PeriodComparatorBarChart barChart = new PeriodComparatorBarChart(context);
        barChart.setLabels(this.labels);
        barChart.setData(this.statsComparator);
        return barChart.drawChart();
    }

    public void setNewDates(int firstYear, int firstMonth, int secondYear, int secondMonth) {
        this.firstMonth = firstMonth;
        this.firstYear = firstYear;
        this.secondMonth = secondMonth;
        this.secondYear = secondYear;
    }

    public void swapDates() {
        setNewDates(secondYear, secondMonth, firstYear, firstMonth);
        datesPicker.swapDates();
    }

    public PeriodStatsComparator getStatsComparator() {
        return statsComparator;
    }

    public BottomSheetMonthYearPicker getDatesPicker() {
        return datesPicker;
    }
}
