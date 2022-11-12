package com.example.budgetmanagement.database.rooms;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ComingAndTransaction {
    @Embedded
    public Coming coming;
    @Relation(
            parentColumn = "transactionId",
            entityColumn = "transactionId"
    )
    public Transaction transaction;
}
