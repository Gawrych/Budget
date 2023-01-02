package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.FragmentPeriodComparatorBinding;

import java.util.HashMap;

public class PeriodStats {

    private final FragmentPeriodComparatorBinding binding;
    private ComingViewModel comingViewModel;
    private PeriodSummary[] startPeriodSummary;
    private PeriodSummary[] endPeriodSummary;
    private int startPeriodMonth;
    private int startPeriodYear;
    private int endPeriodMonth;
    private int endPeriodYear;

    public PeriodStats(FragmentPeriodComparatorBinding binding, ComingViewModel comingViewModel) {
        this.binding = binding;
        this.comingViewModel = comingViewModel;
    }

    public void setData(int firstYear, int firstMonth, int secondYear, int secondMonth) {
        startPeriodYear = firstYear;
        startPeriodMonth = firstMonth;
        endPeriodYear = secondYear;
        endPeriodMonth = secondMonth;
    }

    public void setMonthsSummaryStats() {
        MonthsStatsCollector monthsStatsCollector = new MonthsStatsCollector(comingViewModel);
        this.startPeriodSummary = monthsStatsCollector.getStats(this.startPeriodYear);
        this.endPeriodSummary = monthsStatsCollector.getStats(this.endPeriodYear);

        PeriodStatsComparator statsComparator = new PeriodStatsComparator(startPeriodSummary[startPeriodMonth], endPeriodSummary[endPeriodMonth]);
        binding.amountOfIncomeIncrease.setText(statsComparator.getObtainedIncome()+" zł");
        binding.amountOfProfitIncrease.setText(statsComparator.getObtainedProfit()+" zł");
        binding.amountOfLossIncrease.setText(statsComparator.getObtainedLoss()+" zł");

        binding.incomeIncrease.setText(statsComparator.getPercentIncome() + "%");
        binding.profitIncrease.setText(statsComparator.getPercentProfit() + "%");
        binding.lossIncrease.setText(statsComparator.getPercentLoss() + "%");
    }

    public void setYearsSummaryStats() {
        YearsStatsCollector yearsStatsCollector = new YearsStatsCollector(comingViewModel);
        HashMap<Integer, PeriodSummary> yearsSummary = yearsStatsCollector.getStats();

        PeriodStatsComparator statsComparator = new PeriodStatsComparator(yearsSummary.get(startPeriodYear), yearsSummary.get(endPeriodYear));
        binding.amountOfIncomeIncrease.setText(statsComparator.getObtainedIncome()+" zł");
        binding.amountOfProfitIncrease.setText(statsComparator.getObtainedProfit()+" zł");
        binding.amountOfLossIncrease.setText(statsComparator.getObtainedLoss()+" zł");

        binding.incomeIncrease.setText(statsComparator.getPercentIncome() + "%");
        binding.profitIncrease.setText(statsComparator.getPercentProfit() + "%");
        binding.lossIncrease.setText(statsComparator.getPercentLoss() + "%");
    }
}
