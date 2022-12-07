package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class PeriodStats {

    private final MonthStatisticsBinding binding;
    private PeriodSummary[] periodSummary;
    private BottomSheetMonthYearPicker startPeriodMonthYearPicker;
    private BottomSheetMonthYearPicker endPeriodMonthYearPicker;
    private final String[] shortMonths;
    private final int currentMonth;
    private final int currentYear;
    private int startPeriodMonth;
    private int startPeriodYear;
    private int endPeriodMonth;
    private int endPeriodYear;

    public PeriodStats(MonthStatisticsBinding binding, ComingViewModel comingViewModel) {
        this.binding = binding;

        MonthsStats monthsStats = new MonthsStats(comingViewModel);
        this.periodSummary = monthsStats.getMonthsStatsFromYear(2022);

        Calendar currentDate = Calendar.getInstance();
        this.currentMonth = currentDate.get(Calendar.MONTH);
        this.currentYear = currentDate.get(Calendar.YEAR);
        this.startPeriodMonth = this.currentMonth;
        this.endPeriodMonth = this.currentMonth -1;
        this.startPeriodYear = currentYear;
        this.endPeriodYear = currentYear;

        shortMonths = DateProcessor.getShortMonths();

        binding.startDate.setText(startPeriodYear + " " + shortMonths[startPeriodMonth]);
        binding.endDate.setText(endPeriodYear + " " + shortMonths[endPeriodMonth]);


        setStartPeriodPicker();
        setEndPeriodPicker();
    }

    private void setStartPeriodPicker() {
        startPeriodMonthYearPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, currentYear, currentMonth);

        startPeriodMonthYearPicker.setOnDateSelectedListener((year, month) -> {
            startPeriodMonth = month;
            startPeriodYear = year;
            notifyPeriodChanged();
            binding.startDate.setText(year + " " + shortMonths[month]);
        });
    }

    private void setEndPeriodPicker() {
        endPeriodMonthYearPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, currentYear, currentMonth);

        endPeriodMonthYearPicker.setOnDateSelectedListener((year, month) -> {
            endPeriodMonth = month;
            endPeriodYear = year;
            notifyPeriodChanged();
            binding.endDate.setText(year + " " + shortMonths[month]);
        });
    }

    public void notifyPeriodChanged() {
        PeriodStatsComparator statsComparator = new PeriodStatsComparator(periodSummary[startPeriodMonth], periodSummary[endPeriodMonth]);
        binding.amountOfIncomeIncrease.setText(statsComparator.getObtainedIncome()+" zł");
        binding.amountOfProfitIncrease.setText(statsComparator.getObtainedProfit()+" zł");
        binding.amountOfLossIncrease.setText(statsComparator.getObtainedLoss()+" zł");

        binding.incomeIncrease.setText(statsComparator.getPercentIncome() + "%");
        binding.profitIncrease.setText(statsComparator.getPercentProfit() + "%");
        binding.lossIncrease.setText(statsComparator.getPercentLoss() + "%");
    }

    public BottomSheetMonthYearPicker getStartPeriodMonthYearPicker() {
        return startPeriodMonthYearPicker;
    }

    public BottomSheetMonthYearPicker getEndPeriodMonthYearPicker() {
        return endPeriodMonthYearPicker;
    }
}
