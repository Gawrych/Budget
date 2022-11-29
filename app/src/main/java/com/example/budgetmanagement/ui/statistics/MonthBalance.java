package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.ui.utils.DecimalPrecision;

import java.math.BigDecimal;
import java.util.List;

public class MonthBalance {

    private BigDecimal profitBalance = new BigDecimal(0);
    private BigDecimal lossBalance = new BigDecimal(0);

    public MonthBalance() {}

    public MonthBalance(List<ComingAndTransaction> transactionsFromMonthToSummarise) {
        for(ComingAndTransaction item : transactionsFromMonthToSummarise) {
            BigDecimal amount = new BigDecimal(item.transaction.getAmount());
            if (amount.signum() == 1) {
                profitBalance = profitBalance.add(amount);
            } else {
                lossBalance = lossBalance.add(amount);
            }
        }
    }

    public float getLoss() {
        return lossBalance.abs().intValue();
    }

    public float getProfit() {
        return profitBalance.abs().intValue();
    }

    public float getBalance() {
        BigDecimal profitAndLossBalance = profitBalance.add(lossBalance);
        return profitAndLossBalance.abs().intValue();
    }

    public void add(String value) {
        BigDecimal amount = new BigDecimal(value);
        if (amount.signum() == 1) {
            profitBalance = profitBalance.add(amount);
        } else {
            lossBalance = lossBalance.add(amount);
        }
    }
}
