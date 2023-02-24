package com.example.budgetmanagement.ui.category;

import android.graphics.drawable.Drawable;

import java.math.BigDecimal;

public class CategoryChildValues {

    public String title;
    public Drawable categoryIcon;
    public Drawable categoryIconBackground;
    public BigDecimal budget;
    public int cardLayoutBackgroundColor;

    public CategoryChildValues(String title, Drawable categoryIcon, Drawable categoryIconBackground, String budget, int cardLayoutBackgroundColor) {
        this.title = title;
        this.categoryIcon = categoryIcon;
        this.categoryIconBackground = categoryIconBackground;
        this.budget = new BigDecimal(budget);
        this.cardLayoutBackgroundColor = cardLayoutBackgroundColor;
    }
}
