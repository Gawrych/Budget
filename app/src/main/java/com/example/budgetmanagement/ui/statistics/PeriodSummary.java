package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;

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

    public void add(ComingAndTransaction item) {
        numberOfTransactions++;

        BigDecimal amount = new BigDecimal(item.transaction.getAmount());
        if (amount.signum() == 1) {
            incomeBalance = incomeBalance.add(amount);
        } else {
            lossBalance = lossBalance.add(amount);
        }

        boolean isExecuted = item.coming.isExecuted();
        if (isExecuted) {
            numberOfExecutedTransactions++;
        }

        Calendar today = Calendar.getInstance();
        float millisOfTransactionAfterTheTime = today.getTimeInMillis() - item.coming.getExpireDate();
        boolean isTransactionAfterTheTime = millisOfTransactionAfterTheTime > 0;
        if(!isExecuted && isTransactionAfterTheTime) {
            averageTransactionDelay += millisOfTransactionAfterTheTime;
            numberOfTransactionsAfterTheTime++;
        }

        float millisOfTransactionExecutedAfterTheTime = item.coming.getExecutedDate() - item.coming.getExpireDate();
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
        if (numberOfTransactionsExecutedAfterTheTime > 0) {
            return (int) TimeUnit.MILLISECONDS.toDays(averageTransactionDelay / (numberOfTransactionsExecutedAfterTheTime+numberOfTransactionsAfterTheTime));
        }
        return 0;
    }

    public int getPercentOfTransactionsExecutedOnTime() {
        return percentOfTotal(numberOfTransactions - (numberOfTransactionsExecutedAfterTheTime+numberOfTransactionsAfterTheTime), numberOfTransactions);
    }

    private int percentOfTotal(float value, float total) {
        if (value == 0 || total == 0) {
            return 0;
        }
        return (int) ((value / total) * 100); // TODO: round instead of cast
    }
}
