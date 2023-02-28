package com.example.budgetmanagement.ui.statistics;

public class PeriodComparatorValues {

    public final String percentageOfIncomeIncrease;
    public final String amountOfIncomeIncrease;
    public final String averageTimeAfterTheDeadlineFirstPeriod;
    public final String averageTimeAfterTheDeadlineSecondPeriod;
    public final int payOnTimeInPercentageFirstPeriod;
    public final int payOnTimeInPercentageSecondPeriod;
    public final String amountOfProfitIncrease;
    public final String percentageOfProfitIncrease;
    public final String amountOfLossIncrease;
    public final String averageGrowthTimeAfterTheDeadline;
    public final String growthPayOnTimeInPercentagePoints;
    public final String percentageOfLossIncrease;

    public PeriodComparatorValues(PeriodStatsComparator statsComparator) {
        percentageOfIncomeIncrease = getNumberWithSign(statsComparator.getPercentIncome());
        percentageOfProfitIncrease = getNumberWithSign(statsComparator.getPercentProfit());
        percentageOfLossIncrease = getNumberWithSign(statsComparator.getPercentLoss());

        averageTimeAfterTheDeadlineFirstPeriod = String.valueOf(statsComparator.getFirstPeriod().getAverageTimeAfterTheDeadlineInDays());
        averageTimeAfterTheDeadlineSecondPeriod = String.valueOf(statsComparator.getSecondPeriod().getAverageTimeAfterTheDeadlineInDays());

        payOnTimeInPercentageFirstPeriod = statsComparator.getFirstPeriod().getPercentageOfTransactionsExecutedOnTime();
        payOnTimeInPercentageSecondPeriod = statsComparator.getSecondPeriod().getPercentageOfTransactionsExecutedOnTime();

        amountOfIncomeIncrease = getNumberWithSign(statsComparator.getObtainedIncome());
        amountOfProfitIncrease = getNumberWithSign(statsComparator.getObtainedProfit());
        amountOfLossIncrease = getNumberWithSign(statsComparator.getObtainedLoss());

        averageGrowthTimeAfterTheDeadline = getNumberWithSign(statsComparator.getGrowthAverageTimeAfterTheDeadlineInDays());
        growthPayOnTimeInPercentagePoints = getNumberWithSign(statsComparator.getGrowthOfPercentOfTransactionsExecutedOnTime());
    }

    private String getNumberWithSign(int number) {
        if (number > 0) {
            return "+" + number;
        }
        return String.valueOf(number);
    }
}
