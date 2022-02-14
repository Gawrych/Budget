package com.example.budgetmanagement.database.Rooms;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionAndCategory {

    @Embedded
    public Transaction transaction;
    @Relation(
            parentColumn = "transactionId",
            entityColumn = "categoryId"
    )
    public Category category;
}
