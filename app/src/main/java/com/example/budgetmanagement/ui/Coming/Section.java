package com.example.budgetmanagement.ui.Coming;

import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;

import java.util.List;

public class Section {

    private final int labelId;
    private final List<ComingAndTransaction> comingAndTransactionList;

    public Section(int labelId, List<ComingAndTransaction> comingAndTransactionList) {
        this.labelId = labelId;
        this.comingAndTransactionList = comingAndTransactionList;
    }

    public int getLabelId() {
        return labelId;
    }

    public List<ComingAndTransaction> getComingAndTransactionList() {
        return comingAndTransactionList;
    }
}
