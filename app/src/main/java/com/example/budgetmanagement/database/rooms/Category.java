package com.example.budgetmanagement.database.rooms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId", index = true)
    private final int categoryId;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "icon")
    private final int icon;

    @ColumnInfo(name = "budget")
    private final String budget;

    @ColumnInfo(name = "addDate")
    private final long addDate;

    @ColumnInfo(name = "modifiedDate")
    private final long modifiedDate;

    public Category(int categoryId, @NonNull String name, int icon, @NonNull String budget, long addDate, long modifiedDate) {
        this.categoryId = categoryId;
        this.name = name;
        this.icon = icon;
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

    public int getIcon() {
        return icon;
    }

    public String getBudget() {
        return budget;
    }

    public long getAddDate() {
        return addDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

}
