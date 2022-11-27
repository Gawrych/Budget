package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.ui.utils.DecimalPrecision;

import java.math.BigDecimal;
import java.util.List;

public class MonthBalance {

    private BigDecimal profitBalance = new BigDecimal(0);
    private BigDecimal lossBalance = new BigDecimal(0);
    private BigDecimal profitAndLossBalance = new BigDecimal(0);

    public MonthBalance(List<ComingAndTransaction> transactionsFromMonthToSummarise) {
        for(ComingAndTransaction item : transactionsFromMonthToSummarise) {
            BigDecimal amount = new BigDecimal(item.transaction.getAmount());
            if (amount.signum() == 1) {
                profitBalance = profitBalance.add(amount);
            } else {
                lossBalance = lossBalance.add(amount);
            }
        }
        profitAndLossBalance = profitAndLossBalance.add(profitBalance).add(lossBalance);
    }

    public String getLoss() {
        return lossBalance.toPlainString();
    }

    public String getProfit() {
        return profitBalance.toPlainString();
    }

    public String getBalance() {
        return profitAndLossBalance.toPlainString();
    }
}
