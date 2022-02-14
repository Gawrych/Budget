package com.example.budgetmanagement.database.Rooms.Transaction;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.budgetmanagement.database.Rooms.Category.Category;

public class TransactionAndCategory {

    @Embedded
    public Transaction transaction;
    @Relation(
            parentColumn = "transactionId",
            entityColumn = "categoryId"
    )
    public Category category;
}
