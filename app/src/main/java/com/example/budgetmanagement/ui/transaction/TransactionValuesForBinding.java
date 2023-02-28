package com.example.budgetmanagement.ui.transaction;

import java.math.BigDecimal;

public class TransactionValuesForBinding {

    public String title;
    public String amount;
    public long deadline;
    public boolean isProfit;

    public TransactionValuesForBinding(String title, String amount, long deadline, boolean isProfit) {
        this.title = title;
        this.amount = new BigDecimal(amount).abs().stripTrailingZeros().toPlainString();
        this.deadline = deadline;
        this.isProfit = isProfit;
    }
}
