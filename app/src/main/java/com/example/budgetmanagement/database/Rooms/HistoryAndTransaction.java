package com.example.budgetmanagement.database.Rooms;

import androidx.room.Embedded;
import androidx.room.Relation;

public class HistoryAndTransaction {

    @Embedded
    public History history;
    @Relation(
            parentColumn = "transactionId",
            entityColumn = "transactionId"
    )

    public Transaction transaction;
}
