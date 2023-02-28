package com.example.budgetmanagement.ui.statistics;

public class PeriodStatsComparator {

    private final PeriodSummary firstPeriod;
    private final PeriodSummary secondPeriod;

    public PeriodStatsComparator(PeriodSummary firstPeriod, PeriodSummary secondPeriod) {
        this.firstPeriod = firstPeriod;
        this.secondPeriod = secondPeriod;
    }

    public int getPercentIncome() {
        return percentGrowth(getObtainedIncome(), this.secondPeriod.getIncome());
    }

    public int getPercentProfit() {
        float secondProfit = this.secondPeriod.getIncome() - this.secondPeriod.getLoss();
        int obtainedProfit = getObtainedProfit();

        if (secondProfit < 0) {
            secondProfit = 0;
        }

        return percentGrowth(obtainedProfit, secondProfit);
    }

    public int getPercentLoss() {
        return percentGrowth(getObtainedLoss(), this.secondPeriod.getLoss());
    }

    public int getObtainedIncome() {
        return (int) (this.firstPeriod.getIncome() - this.secondPeriod.getIncome());
    }

    public int getObtainedProfit() {
        float firstProfit = this.firstPeriod.getIncome() - this.firstPeriod.getLoss();
        float secondProfit = this.secondPeriod.getIncome() - this.secondPeriod.getLoss();
        return (int) (firstProfit - secondProfit);
    }

    public int getObtainedLoss() {
        return (int) (this.firstPeriod.getLoss() - this.secondPeriod.getLoss());
    }

    public int getGrowthAverageTimeAfterTheDeadlineInDays() {
        return this.firstPeriod.getAverageTimeAfterTheDeadlineInDays() - this.secondPeriod.getAverageTimeAfterTheDeadlineInDays();
    }

    public int getGrowthOfPercentOfTransactionsExecutedOnTime() {
        return this.firstPeriod.getPercentageOfTransactionsExecutedOnTime() - this.secondPeriod.getPercentageOfTransactionsExecutedOnTime();
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
        return this.firstPeriod;
    }

    public PeriodSummary getSecondPeriod() {
        return this.secondPeriod;
    }
}
