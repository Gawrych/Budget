package com.example.budgetmanagement.ui.statistics;

import android.content.Context;

import com.example.budgetmanagement.R;

import java.text.BreakIterator;

public class PeriodComparatorValues {

    private final Context context;
    private final String percentageOfIncomeIncrease;
    private final String amountOfIncomeIncrease;
    private final String averageTimeAfterTheDeadlineFirstPeriod;
    private final String averageTimeAfterTheDeadlineSecondPeriod;
    private final String payOnTimeInPercentageFirstPeriod;
    private final String payOnTimeInPercentageSecondPeriod;
    private final String amountOfProfitIncrease;
    private final String percentageOfProfitIncrease;
    private final String amountOfLossIncrease;
    private final String averageGrowthTimeAfterTheDeadline;
    private final String growthPayOnTimeInPercentagePoints;
    private final String percentageOfLossIncrease;

    public PeriodComparatorValues(Context context, PeriodStatsComparator statsComparator) {
        this.context = context;
        String currency = context.getResources().getString(R.string.currency);

        percentageOfIncomeIncrease = formatPercent(getNumberWithSign(statsComparator.getPercentIncome()));
        percentageOfProfitIncrease = formatPercent(getNumberWithSign(statsComparator.getPercentProfit()));
        percentageOfLossIncrease = formatPercent(getNumberWithSign(statsComparator.getPercentLoss()));

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

    private String formatPercent(String value) {
        return getFormattedAmount(R.string.amount_with_percent_string, value);
    }

    private String formatDays(float value) {
        return getFormattedAmount(R.string.amount_with_day_label, String.valueOf(value));
    }

    private String formatPercent(float value) {
        return getFormattedAmount(R.string.amount_with_percent, value);
    }

    private String formatCurrencyWithSign(float value, String currency) {
        return getFormattedAmount(R.string.amount_with_currency, getNumberWithSign(value), currency);
    }

    private String formatDaysWithSign(float value) {
        return getFormattedAmount(R.string.amount_with_day_label, getNumberWithSign(value));
    }

    private String formatPercentagePointsWithSign(float value) {
        return getFormattedAmount(R.string.amount_with_percentage_points, getNumberWithSign(value));
    }

    private String getFormattedAmount(int formatResId, Object... args) {
        return context.getString(formatResId, args);
    }

    private String getNumberWithSign(float number) {
        String numberInString = String.valueOf(Math.round(number));
        if (number > 0) {
            return getFormattedAmount(R.string.number_with_plus, numberInString);
        }
        return numberInString;
    }

    public String getPercentageOfIncomeIncrease() {
        return percentageOfIncomeIncrease;
    }

    public String getAmountOfIncomeIncrease() {
        return amountOfIncomeIncrease;
    }

    public String getAverageTimeAfterTheDeadlineFirstPeriod() {
        return averageTimeAfterTheDeadlineFirstPeriod;
    }

    public String getAverageTimeAfterTheDeadlineSecondPeriod() {
        return averageTimeAfterTheDeadlineSecondPeriod;
    }

    public String getPayOnTimeInPercentageFirstPeriod() {
        return payOnTimeInPercentageFirstPeriod;
    }

    public String getPayOnTimeInPercentageSecondPeriod() {
        return payOnTimeInPercentageSecondPeriod;
    }

    public String getAmountOfProfitIncrease() {
        return amountOfProfitIncrease;
    }

    public String getPercentageOfProfitIncrease() {
        return percentageOfProfitIncrease;
    }

    public String getAmountOfLossIncrease() {
        return amountOfLossIncrease;
    }

    public String getAverageGrowthTimeAfterTheDeadline() {
        return averageGrowthTimeAfterTheDeadline;
    }

    public String getGrowthPayOnTimeInPercentagePoints() {
        return growthPayOnTimeInPercentagePoints;
    }

    public String getPercentageOfLossIncrease() {
        return percentageOfLossIncrease;
    }
}
