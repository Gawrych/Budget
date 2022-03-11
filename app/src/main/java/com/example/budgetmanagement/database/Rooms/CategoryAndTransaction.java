package com.example.budgetmanagement.database.Rooms;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryAndTransaction {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "categoryId",
            entityColumn = "categoryCreationId"
    )
    public List<Transaction> transactionList;
}

