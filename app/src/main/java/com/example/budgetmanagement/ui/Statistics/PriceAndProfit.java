package com.example.budgetmanagement.ui.Statistics;

public class PriceAndProfit {

    private Float amount;
    private boolean profit;

    public PriceAndProfit(Float amount, boolean profit) {
        this.amount = amount;
        this.profit = profit;
    }

    public Float getAmount() {
        return amount;
    }

    public boolean isProfit() {
        return profit;
    }
}
