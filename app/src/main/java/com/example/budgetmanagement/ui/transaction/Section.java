package com.example.budgetmanagement.ui.transaction;

import com.example.budgetmanagement.database.rooms.Transaction;

import java.util.List;

public class Section {

    private final String label;
    private final List<Transaction> transactionList;

    public Section(String label, List<Transaction> transactionList) {
        this.label = label;
        this.transactionList = transactionList;
    }

    public String getLabel() {
        return label;
    }

    public List<Transaction> getComingAndTransactionList() {
        return transactionList;
    }
}
