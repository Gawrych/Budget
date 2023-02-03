package com.example.budgetmanagement.ui.statistics;

import android.content.Context;

import com.example.budgetmanagement.R;

import java.text.BreakIterator;

public class PeriodComparatorValues {

    public final Context context;
    public final String percentageOfIncomeIncrease;
    public final String amountOfIncomeIncrease;
    public final String averageTimeAfterTheDeadlineFirstPeriod;
    public final String averageTimeAfterTheDeadlineSecondPeriod;
    public final String payOnTimeInPercentageFirstPeriod;
    public final String payOnTimeInPercentageSecondPeriod;
    public final String amountOfProfitIncrease;
    public final String percentageOfProfitIncrease;
    public final String amountOfLossIncrease;
    public final String averageGrowthTimeAfterTheDeadline;
    public final String growthPayOnTimeInPercentagePoints;
    public final String percentageOfLossIncrease;

    public PeriodComparatorValues(Context context, PeriodStatsComparator statsComparator) {
        this.context = context;
        String currency = context.getResources().getString(R.string.currency);

        percentageOfIncomeIncrease = formatPercentWithSign(statsComparator.getPercentIncome());
        percentageOfProfitIncrease = formatPercentWithSign(statsComparator.getPercentProfit());
        percentageOfLossIncrease = formatPercentWithSign(statsComparator.getPercentLoss());
        averageTimeAfterTheDeadlineFirstPeriod = formatDays(statsComparator.getFirstPeriod().getAverageTimeAfterTheDeadlineInDays());
        averageTimeAfterTheDeadlineSecondPeriod = formatDays(statsComparator.getSecondPeriod().getAverageTimeAfterTheDeadlineInDays());
        payOnTimeInPercentageFirstPeriod = formatPercent(statsComparator.getFirstPeriod().getPercentageOfTransactionsExecutedOnTime());
        payOnTimeInPercentageSecondPeriod = formatPercent(statsComparator.getSecondPeriod().getPercentageOfTransactionsExecutedOnTime());
        amountOfIncomeIncrease = formatCurrencyWithSign(statsComparator.getObtainedIncome(), currency);
        amountOfProfitIncrease = formatCurrencyWithSign(statsComparator.getObtainedProfit(), currency);
        amountOfLossIncrease = formatCurrencyWithSign(statsComparator.getObtainedLoss(), currency);
        averageGrowthTimeAfterTheDeadline = formatDaysWithSign(statsComparator.getGrowthAverageTimeAfterTheDeadlineInDays());
        growthPayOnTimeInPercentagePoints = formatPercentagePointsWithSign(statsComparator.getGrowthOfPercentOfTransactionsExecutedOnTime());
    }

    private String formatPercentWithSign(int value) {
        return getFormattedAmount(R.string.amount_with_percent_string, getNumberWithSign(value));
    }

    private String formatPercent(int value) {
        return getFormattedAmount(R.string.amount_with_percent, value);
    }

    private String formatDays(int value) {
        return getFormattedAmount(R.string.amount_with_day_label, String.valueOf(value));
    }

    private String formatCurrencyWithSign(int value, String currency) {
        return getFormattedAmount(R.string.amount_with_currency, getNumberWithSign(value), currency);
    }

    private String formatDaysWithSign(int value) {
        return getFormattedAmount(R.string.amount_with_day_label, getNumberWithSign(value));
    }

    private String formatPercentagePointsWithSign(int value) {
        return getFormattedAmount(R.string.amount_with_percentage_points, getNumberWithSign(value));
    }

    private String getFormattedAmount(int formatResId, Object... args) {
        return context.getString(formatResId, args);
    }

    private String getNumberWithSign(int number) {
        String numberInString = String.valueOf(Math.round(number));
        if (number > 0) {
            return getFormattedAmount(R.string.number_with_plus, numberInString);
        }
        return numberInString;
    }
}
