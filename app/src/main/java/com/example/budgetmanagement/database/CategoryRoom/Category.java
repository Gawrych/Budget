package com.example.budgetmanagement.database.CategoryRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "category_id")
    private int category_id;


}
