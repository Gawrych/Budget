package com.example.budgetmanagement.ui.category;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.ui.utils.CategoryIconHelper;

import java.math.BigDecimal;

public class CategoryValuesHandler implements CategoryIconHelper {

    public String name;
    public BigDecimal budget;
    public Drawable categoryIcon;
    public long addDate;
    public long lastEditDate;
    private final Fragment fragment;
    private final Category category;
    public Drawable categoryIconBackground;

    public CategoryValuesHandler(int categoryId, @NonNull Fragment fragment) {
        this.fragment = fragment;

        CategoryViewModel categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);
        this.category = categoryViewModel.getCategoryById(categoryId);

        setValues();
    }

    public void setValues() {
        this.name = category.getName();
        this.budget = new BigDecimal(category.getBudget());
        this.categoryIcon = CategoryIconHelper.getCategoryIcon(category.getIcon(), this.fragment.requireActivity());
        this.categoryIconBackground = CategoryIconHelper.getIconBackground(this.fragment.requireContext(), this.category.getColor(), R.drawable.icon_background);
        this.addDate = category.getAddDate();
        this.lastEditDate = category.getModifiedDate();
    }
}
