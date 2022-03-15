package com.example.budgetmanagement.database.Rooms;

import androidx.room.Embedded;
import androidx.room.Relation;
import androidx.room.Junction;

import java.util.List;

public class CategoryWithTransaction {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "categoryId",
            entityColumn = "transactionId",
            associateBy = @Junction(CategoryTransactionCrossRef.class)
    )
    public List<Transaction> transactionList;
}
