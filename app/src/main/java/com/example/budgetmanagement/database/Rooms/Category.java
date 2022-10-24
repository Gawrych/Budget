package com.example.budgetmanagement.database.Rooms;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId", index = true)
    private int categoryId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] icon;

    @ColumnInfo(name = "budget")
    private int budget;

    @ColumnInfo(name = "addDate")
    private long addDate;

    @ColumnInfo(name = "modifiedDate")
    private long modifiedDate;

    public Category(int categoryId, @NonNull String name, @NonNull byte[] icon, int budget, long addDate, long modifiedDate) {
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

    @NonNull
    public byte[] getIcon() {
        return icon;
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
