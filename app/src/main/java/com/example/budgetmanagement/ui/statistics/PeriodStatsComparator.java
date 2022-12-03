package com.example.budgetmanagement.ui.statistics;

public class PeriodStatsComparator {

    private final MonthStatsSummary firstMonth;
    private final MonthStatsSummary secondMonth;

    public PeriodStatsComparator(MonthStatsSummary firstMonth, MonthStatsSummary secondMonth) {
        this.firstMonth = firstMonth;
        this.secondMonth = secondMonth;
    }

    public int getIncome() {
        if (secondMonth.getProfit() == 0) {
            return 100;
        }
        return (int) (((firstMonth.getProfit() - secondMonth.getProfit()) / secondMonth.getProfit()) * 100);
    }

    public int getProfit() {
        return 0;
    }
}
