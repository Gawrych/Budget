package com.example.budgetmanagement.ui.coming;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;

import java.util.List;

public class Section {

    private final String label;
    private final List<ComingAndTransaction> comingAndTransactionList;

    public Section(String label, List<ComingAndTransaction> comingAndTransactionList) {
        this.label = label;
        this.comingAndTransactionList = comingAndTransactionList;
    }

    public String getLabel() {
        return label;
    }

    public List<ComingAndTransaction> getComingAndTransactionList() {
        return comingAndTransactionList;
    }
}
