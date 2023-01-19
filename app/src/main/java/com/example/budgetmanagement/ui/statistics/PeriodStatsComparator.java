package com.example.budgetmanagement.ui.statistics;

public class PeriodStatsComparator {

    private final PeriodSummary firstPeriod;
    private final PeriodSummary secondPeriod;

    public PeriodStatsComparator(PeriodSummary firstPeriod, PeriodSummary secondPeriod) {
        this.firstPeriod = firstPeriod;
        this.secondPeriod = secondPeriod;
    }

    public int getPercentIncome() {
        return percentGrowth(getObtainedIncome(), secondPeriod.getIncome());
    }

    public int getPercentProfit() {
        float secondProfit = secondPeriod.getIncome() - secondPeriod.getLoss();
        return percentGrowth(getObtainedProfit(), secondProfit);

//         TODO: Think about it
//        float secondProfit = secondPeriod.getIncome() - secondPeriod.getLoss();
//        int obtainedProfit = getObtainedProfit();
//        if (obtainedProfit < 0) {   // don't calculate profit if base is below zero
//            return percentageGrowth(0, secondProfit);
//        }
//        return percentageGrowth(obtainedProfit, secondProfit);
    }

    public int getPercentLoss() {
        return percentGrowth(getObtainedLoss(), secondPeriod.getLoss());
    }

    public int getObtainedIncome() {
        return (int) (firstPeriod.getIncome() - secondPeriod.getIncome());
    }

    public int getObtainedProfit() {
        float firstProfit = firstPeriod.getIncome() - firstPeriod.getLoss();
        float secondProfit = secondPeriod.getIncome() - secondPeriod.getLoss();
        return (int) (firstProfit - secondProfit);
    }

    public int getObtainedLoss() {
        return (int) (firstPeriod.getLoss() - secondPeriod.getLoss());
    }

    public int getGrowthAverageTimeAfterTheDeadlineInDays() {
        return firstPeriod.getAverageTimeAfterTheDeadlineInDays() - secondPeriod.getAverageTimeAfterTheDeadlineInDays();
    }

    public int getGrowthOfPercentOfTransactionsExecutedOnTime() {
        return firstPeriod.getPercentageOfTransactionsExecutedOnTime() - secondPeriod.getPercentageOfTransactionsExecutedOnTime();
    }

    private int percentGrowth(float obtained, float total) {
        if (obtained == 0 && total == 0) {
            return 0;
        } else if (total == 0) {
            return 100;
        }
        return (int) (obtained * 100 / total);
    }

    public PeriodSummary getFirstPeriod() {
        return firstPeriod;
    }

    public PeriodSummary getSecondPeriod() {
        return secondPeriod;
    }
}
