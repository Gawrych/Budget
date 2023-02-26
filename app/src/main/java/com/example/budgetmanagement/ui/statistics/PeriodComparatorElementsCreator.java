package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.github.mikephil.charting.charts.BarChart;

import java.util.Calendar;
import java.util.HashMap;

public class PeriodComparatorElementsCreator {

    private final Context context;
    private final TransactionViewModel transactionViewModel;
    private int firstMonth;
    private int firstYear;
    private int secondMonth;
    private int secondYear;
    private String[] labels;
    private PeriodStatsComparator statsComparator;
    private BottomSheetMonthYearPicker datesPicker;

    public PeriodComparatorElementsCreator(Context context, ViewModelStoreOwner owner) {
        this.transactionViewModel = new ViewModelProvider(owner).get(TransactionViewModel.class);
        this.context = context;
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
        this.datesPicker = BottomSheetMonthYearPicker.newInstance(MONTHS_AND_YEAR_MODE,
                this.firstYear, this.firstMonth, this.secondYear, this.secondMonth);
        this.datesPicker.setDatesFromBundle();
        this.datesPicker.setCancelable(false);
    }

    public void setMonthsModeLabelsForChart() {
        String[] labels = new String[2];
        String[] monthsNames = context.getResources().getStringArray(R.array.months);
        labels[1] = monthsNames[this.firstMonth] + " " + this.firstYear;
        labels[0] = monthsNames[this.secondMonth] + " " + this.secondYear;
        this.labels = labels;
    }

    public void setYearsModeLabelsForChart() {
        String[] labels = new String[2];
        labels[1] = String.valueOf(this.firstYear);
        labels[0] = String.valueOf(this.secondYear);
        this.labels = labels;
    }

    public void setMonthsSummaryStats() {
        MonthsStatsCollector monthsStatsCollector = new MonthsStatsCollector(this.transactionViewModel);
        PeriodSummary[] firstPeriodSummary = monthsStatsCollector.getStats(this.firstYear);
        PeriodSummary[] secondPeriodSummary = monthsStatsCollector.getStats(this.secondYear);
        this.statsComparator = new PeriodStatsComparator(firstPeriodSummary[this.firstMonth], secondPeriodSummary[this.secondMonth]);
    }

    public void setYearsSummaryStats() {
        YearsStatsCollector yearsStatsCollector = new YearsStatsCollector(this.transactionViewModel);
        HashMap<Integer, PeriodSummary> yearsSummary = yearsStatsCollector.getStats();

        PeriodSummary firstPeriod = yearsSummary.getOrDefault(this.firstYear, new PeriodSummary());
        PeriodSummary secondPeriod = yearsSummary.getOrDefault(this.secondYear, new PeriodSummary());

        this.statsComparator = new PeriodStatsComparator(firstPeriod, secondPeriod);
    }

    public BarChart createBarChart() {
        PeriodComparatorBarChart barChart = new PeriodComparatorBarChart(this.context);
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
        setNewDates(this.secondYear, this.secondMonth, this.firstYear, this.firstMonth);
        this.datesPicker.swapDates();
    }

    public PeriodStatsComparator getStatsComparator() {
        return this.statsComparator;
    }

    public BottomSheetMonthYearPicker getDatesPicker() {
        return this.datesPicker;
    }
}
