package com.example.budgetmanagement.ui.utils;

public class Filter {

    private final String filterName;
    private final int filterValue;

    public Filter(String filterName, int filterValue) {
        this.filterName = filterName;
        this.filterValue = filterValue;
    }

    public String getName() {
        return filterName;
    }

    public int getValue() {
        return filterValue;
    }
}
