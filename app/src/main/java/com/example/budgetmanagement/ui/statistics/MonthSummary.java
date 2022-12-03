package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonthSummary {

    private static final int NUMBER_OF_MILLISECONDS_IN_ONE_DAY = 86400000;
    private BigDecimal profitBalance = new BigDecimal(0);
    private BigDecimal lossBalance = new BigDecimal(0);
    private int numberOfTransactions = 0;
    private int numberOfTransactionsExecutedAfterTheTime = 0;
    private int numberOfExecutedTransactions = 0;
    private long averageTransactionExecutedDelay = 0;

    public MonthSummary() {}

    public MonthSummary(List<ComingAndTransaction> transactionsFromMonthToSummarise) {
        for(ComingAndTransaction item : transactionsFromMonthToSummarise) {
            BigDecimal amount = new BigDecimal(item.transaction.getAmount());
            if (amount.signum() == 1) {
                profitBalance = profitBalance.add(amount);
            } else {
                lossBalance = lossBalance.add(amount);
            }
        }
    }

    public void add(ComingAndTransaction item) {
        numberOfTransactions++;

        BigDecimal amount = new BigDecimal(item.transaction.getAmount());
        if (amount.signum() == 1) {
            profitBalance = profitBalance.add(amount);
        } else {
            lossBalance = lossBalance.add(amount);
        }

        boolean isExecuted = item.coming.isExecuted();
        if (isExecuted) {
            numberOfExecutedTransactions++;
        }

        float millisAfterTheTime = item.coming.getExecutedDate() - item.coming.getExpireDate();
        boolean executedAfterTheTime = millisAfterTheTime > 0;
        if (isExecuted && executedAfterTheTime) {
            averageTransactionExecutedDelay += millisAfterTheTime;
            numberOfTransactionsExecutedAfterTheTime++;
        }
    }

    public float getLoss() {
        return lossBalance.abs().intValue();
    }

    public float getProfit() {
        return profitBalance.abs().intValue();
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public int getNumberOfOutdatedTransactions() {
        return numberOfTransactionsExecutedAfterTheTime;
    }

    public int getNumberOfRemainingTransactions() {
        return numberOfTransactions - numberOfExecutedTransactions;
    }

    public int getAverageTransactionExecutedDelayInDays() {
        return (int) TimeUnit.MILLISECONDS.toDays(averageTransactionExecutedDelay);
    }

}
