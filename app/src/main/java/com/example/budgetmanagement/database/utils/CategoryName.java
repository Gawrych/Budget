package com.example.budgetmanagement.database.utils;

public class CategoryName {

    private String name;
    private int budget;

    public CategoryName(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }
}
