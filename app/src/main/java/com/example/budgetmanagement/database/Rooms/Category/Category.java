package com.example.budgetmanagement.database.Rooms.Category;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.NonNull;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "category_id")
    private int category_id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "icon_name")
    private String icon_name;

    @ColumnInfo(name = "budget")
    private int budget;

    @ColumnInfo(name = "add_date")
    private long add_date;

    @ColumnInfo(name = "modified_date")
    private long modified_date;

    public Category(int category_id, String name, String icon_name, int budget, long add_date, long modified_date) {
        this.category_id = category_id;
        this.name = name;
        this.icon_name = icon_name;
        this.budget = budget;
        this.add_date = add_date;
        this.modified_date = modified_date;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public int getBudget() {
        return budget;
    }

    public long getAdd_date() {
        return add_date;
    }

    public long getModified_date() {
        return modified_date;
    }
}
