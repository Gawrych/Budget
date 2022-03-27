package com.example.budgetmanagement.ui.History;

import androidx.room.ColumnInfo;

public class HistoryBottomSheetEntity {

    @ColumnInfo(name = "categoryId")
    private int id;

    @ColumnInfo(name = "iconName")
    private String IconName;

    @ColumnInfo(name = "name")
    private String Name;

    public void setId(int id) {
        this.id = id;
    }

    public void setIconName(String iconName) {
        IconName = iconName;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public String getIconName() {
        return IconName;
    }

    public String getName() {
        return Name;
    }
}
