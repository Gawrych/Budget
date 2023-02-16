package com.example.budgetmanagement.ui.transaction;

public class TransactionDataForUi {

    public String title;
    public String amount;
    public String categoryName;
    public String startDate;
    public boolean isProfit;

    public TransactionDataForUi(String title, String amount, String categoryName, String startDate, boolean isProfit) {
        this.title = title;
        this.amount = amount;
        this.categoryName = categoryName;
        this.startDate = startDate;
        this.isProfit = isProfit;
    }
}
