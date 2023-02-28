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
        this.numberOfTransactions++;

        if (item.isProfit()) {
            this.incomeBalance = this.incomeBalance.add(new BigDecimal(item.getAmount()));
        } else {
            this.lossBalance = this.lossBalance.add(new BigDecimal(item.getAmount()));
        }

        boolean isExecuted = item.isExecuted();
        if (isExecuted) {
            this.numberOfExecutedTransactions++;
        }

        float millisOfTransactionAfterTheTime = System.currentTimeMillis() - item.getDeadline();
        boolean isTransactionAfterTheTime = millisOfTransactionAfterTheTime > 0;
        if(!isExecuted && isTransactionAfterTheTime) {
            this.averageTransactionDelay += millisOfTransactionAfterTheTime;
            this.numberOfTransactionsAfterTheTime++;
        }

        float millisOfTransactionExecutedAfterTheTime = item.getExecutedDate() - item.getDeadline();
        boolean isTransactionExecutedAfterTheTime = millisOfTransactionExecutedAfterTheTime > 0;
        if (isExecuted && isTransactionExecutedAfterTheTime) {
            this.averageTransactionDelay += millisOfTransactionExecutedAfterTheTime;
            this.numberOfTransactionsExecutedAfterTheTime++;
        }
    }

    public float getLoss() {
        return this.lossBalance.abs().intValue();
    }

    public float getIncome() {
        return this.incomeBalance.abs().intValue();
    }

    public float getProfit() {
        return getIncome() - getLoss();
    }

    public int getNumberOfTransactions() {
        return this.numberOfTransactions;
    }

    public int getNumberOfTransactionsAfterTheTime() {
        return this.numberOfTransactionsAfterTheTime;
    }

    public int getNumberOfRemainingTransactions() {
        return this.numberOfTransactions - this.numberOfExecutedTransactions;
    }

    public int getAverageTimeAfterTheDeadlineInDays() {
        int sumNumberOfTransactionsAfterTheTime = this.numberOfTransactionsExecutedAfterTheTime+this.numberOfTransactionsAfterTheTime;
        if (sumNumberOfTransactionsAfterTheTime > 0) {
            return (int) TimeUnit.MILLISECONDS.toDays(this.averageTransactionDelay / sumNumberOfTransactionsAfterTheTime);
        }
        return 0;
    }

    public int getPercentageOfTransactionsExecutedOnTime() {
        int sumNumberOfTransactionsAfterTheTime = this.numberOfTransactionsExecutedAfterTheTime+this.numberOfTransactionsAfterTheTime;
        return percentageOfTotal(this.numberOfTransactions - sumNumberOfTransactionsAfterTheTime, this.numberOfTransactions);
    }

    private int percentageOfTotal(float value, float total) {
        if (value == 0 || total == 0) {
            return 0;
        }
        return Math.round((value / total) * 100);
    }
}
