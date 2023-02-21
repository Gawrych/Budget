package com.example.budgetmanagement.ui.transaction;

public class TransactionValuesForBinding {

    public String title;
    public String amount;
    public long deadline;
    public boolean isProfit;

    public TransactionValuesForBinding(String title, String amount, long deadline, boolean isProfit) {
        this.title = title;
        this.amount = amount;
        this.deadline = deadline;
        this.isProfit = isProfit;
    }
}
