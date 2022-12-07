package com.example.budgetmanagement.ui.statistics;

public class PeriodStatsComparator {

    private final PeriodSummary firstMonth;
    private final PeriodSummary secondMonth;

    public PeriodStatsComparator(PeriodSummary firstMonth, PeriodSummary secondMonth) {
        this.firstMonth = firstMonth;
        this.secondMonth = secondMonth;
    }

    public int getPercentIncome() {
        return percentGrowth(getObtainedIncome(), secondMonth.getIncome());
    }

    public int getPercentProfit() {
        float secondProfit = secondMonth.getIncome() - secondMonth.getLoss();
        return percentGrowth(getObtainedProfit(), secondProfit);
    }

    public int getPercentLoss() {
        return percentGrowth(getObtainedLoss(), secondMonth.getLoss());
    }

    public int getObtainedIncome() {
        return (int) (firstMonth.getIncome() - secondMonth.getIncome());
    }

    public int getObtainedProfit() {
        float firstProfit = firstMonth.getIncome() - firstMonth.getLoss();
        float secondProfit = secondMonth.getIncome() - secondMonth.getLoss();
        return (int) (firstProfit - secondProfit);
    }

    public int getObtainedLoss() {
        return (int) (firstMonth.getLoss() - secondMonth.getLoss());
    }

    private int percentGrowth(float obtained, float total) {
        if (obtained == 0 && total == 0) {
            return 0;
        } else if (total == 0) {
            return 100;
        }
        return (int) (obtained * 100 / total);
    }
}
