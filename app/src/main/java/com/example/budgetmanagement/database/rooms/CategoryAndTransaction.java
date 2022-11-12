package com.example.budgetmanagement.database.rooms;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryAndTransaction {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "categoryId",
            entityColumn = "categoryId"
    )
    public List<Transaction> transactionList;
}

