package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.FragmentPeriodComparatorBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;
import java.util.HashMap;

public class PeriodComparatorElementsCreator {

    private final String[] shortMonths = DateProcessor.getMonths();
    private final FragmentPeriodComparatorBinding binding;
    private final ComingViewModel comingViewModel;
    private int firstMonth;
    private int firstYear;
    private int secondMonth;
    private int secondYear;
    private String[] labels;
    private PeriodStatsComparator statsComparator;
    private BottomSheetMonthYearPicker datesPicker;

    public PeriodComparatorElementsCreator(FragmentPeriodComparatorBinding binding, ComingViewModel comingViewModel) {
        this.binding = binding;
        this.comingViewModel = comingViewModel;
        setDefaultDates();
        initializePeriodPicker();
    }

    private void setDefaultDates() {
        Calendar currentDate = Calendar.getInstance();
        this.firstMonth = currentDate.get(Calendar.MONTH);
        this.firstYear = currentDate.get(Calendar.YEAR);

        Calendar secondDate = Calendar.getInstance();
        secondDate.add(Calendar.MONTH, -1);
        this.secondMonth = secondDate.get(Calendar.MONTH);
        this.secondYear = secondDate.get(Calendar.YEAR);
    }

    private void initializePeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, firstYear, firstMonth, secondYear, secondMonth);
        datesPicker.setCancelable(false);
    }

    public void setMonthsModeLabelsForChart() {
        String[] labels = new String[2];
        String[] months = DateProcessor.getMonths();
        labels[1] = months[firstMonth] + " " + firstYear;
        labels[0] = months[secondMonth] + " " + secondYear;
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

        PeriodSummary firstPeriod = yearsSummary.get(firstYear);
        PeriodSummary secondPeriod = yearsSummary.get(secondYear);

        if (firstPeriod == null) {
            firstPeriod = new PeriodSummary();
        }

        if (secondPeriod == null) {
            secondPeriod = new PeriodSummary();
        }

        this.statsComparator = new PeriodStatsComparator(firstPeriod, secondPeriod);
    }

    public void createBarChart() {
        PeriodComparatorBarChart barChart = new PeriodComparatorBarChart(binding);
        barChart.setLabels(this.labels);
        barChart.setData(this.statsComparator);
        barChart.drawChart();
    }

    public void setNewDates(int firstYear, int firstMonth, int secondYear, int secondMonth) {
        this.firstMonth = firstMonth;
        this.firstYear = firstYear;
        this.secondMonth = secondMonth;
        this.secondYear = secondYear;
    }

    public PeriodStatsComparator getStatsComparator() {
        return statsComparator;
    }

    public BottomSheetMonthYearPicker getDatesPicker() {
        return datesPicker;
    }

    public String getFirstMonth() {
        return shortMonths[firstMonth];
    }

    public String getFirstYear() {
        return String.valueOf(firstYear);
    }

    public String getSecondMonth() {
        return shortMonths[secondMonth];
    }

    public String getSecondYear() {
        return String.valueOf(secondYear);
    }
}
