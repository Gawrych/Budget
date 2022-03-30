package com.example.budgetmanagement.ui.History;

import androidx.room.ColumnInfo;

public class HistoryBottomSheetEntity {

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "iconName")
    private String iconName;

    @ColumnInfo(name = "name")
    private String name;

    public HistoryBottomSheetEntity() {

    }

    public HistoryBottomSheetEntity(int id, String iconName, String name) {
        this.id = id;
        this.iconName = iconName;
        this.name = name;
    }

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
