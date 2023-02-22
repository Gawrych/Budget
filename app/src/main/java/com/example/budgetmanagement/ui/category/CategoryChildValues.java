package com.example.budgetmanagement.ui.category;

import android.graphics.drawable.Drawable;

public class CategoryChildValues {

    public String title;
    public Drawable categoryIcon;
    public Drawable categoryIconBackground;
    public Drawable budgetIconWithColor;
    public int cardLayoutBackgroundColor;

    public CategoryChildValues(String title, Drawable categoryIcon, Drawable categoryIconBackground, Drawable budgetIconWithColor, int cardLayoutBackgroundColor) {
        this.title = title;
        this.categoryIcon = categoryIcon;
        this.categoryIconBackground = categoryIconBackground;
        this.budgetIconWithColor = budgetIconWithColor;
        this.cardLayoutBackgroundColor = cardLayoutBackgroundColor;
    }
}
