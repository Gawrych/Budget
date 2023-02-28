package com.example.budgetmanagement.ui.category;

import java.math.BigDecimal;

public class CategorySimpleDataForBinding {

    public String name;
    public String budget;
    public boolean isProfit;

    public CategorySimpleDataForBinding(String name, String budget, boolean isProfit) {
        this.name = name;
        this.budget = new BigDecimal(budget).abs().stripTrailingZeros().toPlainString();
        this.isProfit = isProfit;
    }
}
