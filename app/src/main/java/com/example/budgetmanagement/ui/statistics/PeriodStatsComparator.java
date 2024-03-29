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
        int obtainedProfit = getObtainedProfit();

        if (secondProfit < 0) {
            secondProfit = 0;
        }

        return percentGrowth(obtainedProfit, secondProfit);
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

    public String getGrowthOfPercentOfTransactionsExecutedOnTime() {
        return percentIncreaseOrDecrease(firstPeriod.getPercentageOfTransactionsExecutedOnTime(), secondPeriod.getPercentageOfTransactionsExecutedOnTime());
    }

    private int percentGrowth(float obtained, float total) {
        if (obtained == 0 && total == 0) {
            return 0;
        } else if (total == 0) {
            return 100;
        }
        return (int) (obtained * 100 / total);
    }

    private String percentIncreaseOrDecrease(float newNumber, float originalNumber) {
        float obtained = 0;
        String sign = "";

        if (newNumber < originalNumber) {
            obtained = newNumber - originalNumber;
            sign = "-";
        } else {
            obtained = originalNumber - newNumber;
            sign = "+";
        }

        return sign + Math.abs(percentGrowth(obtained, originalNumber));
    }

    public PeriodSummary getFirstPeriod() {
        return firstPeriod;
    }

    public PeriodSummary getSecondPeriod() {
        return secondPeriod;
    }
}
