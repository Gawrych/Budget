package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.Transaction;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PeriodSummary {

    private BigDecimal incomeBalance = new BigDecimal(0);
    private BigDecimal lossBalance = new BigDecimal(0);
    private int numberOfTransactions;
    private int numberOfTransactionsExecutedAfterTheTime;
    private int numberOfTransactionsAfterTheTime;
    private int numberOfExecutedTransactions;
    private long averageTransactionDelay;

    public PeriodSummary() {}

    public void add(Transaction item) {
        numberOfTransactions++;

        if (item.isProfit()) {
            incomeBalance = incomeBalance.add(new BigDecimal(item.getAmount()));
        } else {
            lossBalance = lossBalance.add(new BigDecimal(item.getAmount()));
        }

        boolean isExecuted = item.isExecuted();
        if (isExecuted) {
            numberOfExecutedTransactions++;
        }

        float millisOfTransactionAfterTheTime = System.currentTimeMillis() - item.getDeadline();
        boolean isTransactionAfterTheTime = millisOfTransactionAfterTheTime > 0;
        if(!isExecuted && isTransactionAfterTheTime) {
            averageTransactionDelay += millisOfTransactionAfterTheTime;
            numberOfTransactionsAfterTheTime++;
        }

        float millisOfTransactionExecutedAfterTheTime = item.getExecutedDate() - item.getDeadline();
        boolean isTransactionExecutedAfterTheTime = millisOfTransactionExecutedAfterTheTime > 0;
        if (isExecuted && isTransactionExecutedAfterTheTime) {
            averageTransactionDelay += millisOfTransactionExecutedAfterTheTime;
            numberOfTransactionsExecutedAfterTheTime++;
        }
    }

    public float getLoss() {
        return lossBalance.abs().intValue();
    }

    public float getIncome() {
        return incomeBalance.abs().intValue();
    }

    public float getProfit() {
        return getIncome() - getLoss();
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public int getNumberOfTransactionsAfterTheTime() {
        return numberOfTransactionsAfterTheTime;
    }

    public int getNumberOfRemainingTransactions() {
        return numberOfTransactions - numberOfExecutedTransactions;
    }

    public int getAverageTimeAfterTheDeadlineInDays() {
        int sumNumberOfTransactionsAfterTheTime = numberOfTransactionsExecutedAfterTheTime+numberOfTransactionsAfterTheTime;
        if (sumNumberOfTransactionsAfterTheTime > 0) {
            return (int) TimeUnit.MILLISECONDS.toDays(averageTransactionDelay / sumNumberOfTransactionsAfterTheTime);
        }
        return 0;
    }

    public int getPercentageOfTransactionsExecutedOnTime() {
        int sumNumberOfTransactionsAfterTheTime = numberOfTransactionsExecutedAfterTheTime+numberOfTransactionsAfterTheTime;
        return percentageOfTotal(numberOfTransactions - sumNumberOfTransactionsAfterTheTime, numberOfTransactions);
    }

    private int percentageOfTotal(float value, float total) {
        if (value == 0 || total == 0) {
            return 0;
        }
        return Math.round((value / total) * 100);
    }
}
