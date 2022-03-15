package com.example.budgetmanagement.database.Rooms;

import androidx.room.Entity;

@Entity(primaryKeys = {"categoryId", "transactionId"})
public class CategoryTransactionCrossRef {

    public long categoryId;
    public long transactionId;
}
