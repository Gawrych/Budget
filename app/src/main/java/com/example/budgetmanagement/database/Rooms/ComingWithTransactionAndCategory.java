package com.example.budgetmanagement.database.Rooms;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ComingWithTransactionAndCategory {
    @Embedded
    public Coming coming;
    @Relation(
            entity = Category.class,
            parentColumn = "comingId",
            entityColumn = "comingCreationId"
    )
    public List<ComingWithTransaction> comingWithTransactions;
}
