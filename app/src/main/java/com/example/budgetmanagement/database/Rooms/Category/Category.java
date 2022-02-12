package com.example.budgetmanagement.database.Rooms.Category;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    private int categoryId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "iconName")
    private String iconName;

    @ColumnInfo(name = "budget")
    private int budget;

    @ColumnInfo(name = "add_date")
    private long addDate;

    @ColumnInfo(name = "modified_date")
    private long modifiedDate;

    public Category(int categoryId, @NonNull String name, @NonNull String iconName, int budget, long addDate, long modifiedDate) {
        this.categoryId = categoryId;
        this.name = name;
        this.iconName = iconName;
        this.budget = budget;
        this.addDate = addDate;
        this.modifiedDate = modifiedDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getIconName() {
        return iconName;
    }

    public int getBudget() {
        return budget;
    }

    public long getAddDate() {
        return addDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }
}
