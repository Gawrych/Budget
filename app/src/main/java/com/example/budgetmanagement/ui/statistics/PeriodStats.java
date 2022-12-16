package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;
import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.ONLY_YEAR_MODE;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class PeriodStats {

    private final MonthStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    private StatsCollector statsCollector;
    private PeriodSummary[] startPeriodSummary;
    private PeriodSummary[] endPeriodSummary;
    private BottomSheetMonthYearPicker datesPicker;
    private String[] shortMonths;
    private int currentMonth;
    private int currentYear;
    private int startPeriodMonth;
    private int startPeriodYear;
    private int endPeriodMonth;
    private int endPeriodYear;

    public PeriodStats(MonthStatisticsBinding binding, ComingViewModel comingViewModel) {
        this.binding = binding;
        this.comingViewModel = comingViewModel;
    }

    public void currentMonthIsJanuary() {

    }

    public void setMonthsPeriod() {
        Calendar currentDate = Calendar.getInstance();
        this.currentMonth = currentDate.get(Calendar.MONTH);
        this.currentYear = currentDate.get(Calendar.YEAR);
        this.startPeriodYear = currentYear;
        this.endPeriodYear = currentYear;
        this.startPeriodMonth = this.currentMonth;
        this.endPeriodMonth = this.currentMonth - 1;

        shortMonths = DateProcessor.getShortMonths();

        statsCollector = new StatsCollector(comingViewModel);
        this.startPeriodSummary = statsCollector.getMonthsStats(this.currentYear);
        this.endPeriodSummary = statsCollector.getMonthsStats(this.currentYear);

        binding.startDate.setText(startPeriodYear + " " + shortMonths[startPeriodMonth]);
        binding.endDate.setText(endPeriodYear + " " + shortMonths[endPeriodMonth]);

        initializeMonthAndYearPeriodPicker();
    }

    public void setYearsPeriod() {
        Calendar currentDate = Calendar.getInstance();
        this.currentYear = currentDate.get(Calendar.YEAR);
        this.startPeriodYear = currentYear;
        this.endPeriodYear = currentYear;

//        statsCollector = new StatsCollector(comingViewModel);
//        this.startPeriodSummary = statsCollector.getYearStats();
//        this.endPeriodSummary = statsCollector.getYearStats();

        binding.startDate.setText(String.valueOf(startPeriodYear));
        binding.endDate.setText(String.valueOf(endPeriodYear));

        initializeYearPeriodPicker();
    }

    private void initializeMonthAndYearPeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, startPeriodYear, startPeriodMonth, endPeriodYear, endPeriodMonth);
        datesPicker.setCancelable(false);

        datesPicker.setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            startPeriodMonth = firstMonth;
            startPeriodYear = firstYear;
            endPeriodYear = secondYear;
            endPeriodMonth = secondMonth;
            notifyPeriodChanged();
            binding.startDate.setText(firstYear + " " + shortMonths[firstMonth]);
        });
    }

    private void initializeYearPeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(ONLY_YEAR_MODE, startPeriodYear, startPeriodMonth, endPeriodYear, endPeriodMonth);
        datesPicker.setCancelable(false);

        datesPicker.setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            startPeriodYear = firstYear;
            endPeriodYear = secondYear;
            notifyPeriodChanged();
            binding.startDate.setText(firstYear + secondYear);
        });
    }

    public void notifyPeriodChanged() {
        this.startPeriodSummary = statsCollector.getMonthsStats(this.startPeriodYear);
        this.endPeriodSummary = statsCollector.getMonthsStats(this.endPeriodYear);
        binding.startDate.setText(startPeriodYear + " " + shortMonths[startPeriodMonth]);
        binding.endDate.setText(endPeriodYear + " " + shortMonths[endPeriodMonth]);

        PeriodStatsComparator statsComparator = new PeriodStatsComparator(startPeriodSummary[startPeriodMonth], endPeriodSummary[endPeriodMonth]);
        binding.amountOfIncomeIncrease.setText(statsComparator.getObtainedIncome()+" zł");
        binding.amountOfProfitIncrease.setText(statsComparator.getObtainedProfit()+" zł");
        binding.amountOfLossIncrease.setText(statsComparator.getObtainedLoss()+" zł");

        binding.incomeIncrease.setText(statsComparator.getPercentIncome() + "%");
        binding.profitIncrease.setText(statsComparator.getPercentProfit() + "%");
        binding.lossIncrease.setText(statsComparator.getPercentLoss() + "%");
    }

    public BottomSheetMonthYearPicker getDatesPicker() {
        return datesPicker;
    }
}
