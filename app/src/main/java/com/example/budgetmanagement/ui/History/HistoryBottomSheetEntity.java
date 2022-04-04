package com.example.budgetmanagement.ui.History;

import androidx.room.ColumnInfo;

public class HistoryBottomSheetEntity {

    @ColumnInfo(name = "categoryId")
    private int id;

    @ColumnInfo(name = "iconName")
    private String iconName;

    @ColumnInfo(name = "name")
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getIconName() {
        return iconName;
    }

    public String getName() {
        return name;
    }
}
