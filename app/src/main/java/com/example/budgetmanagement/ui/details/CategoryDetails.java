package com.example.budgetmanagement.ui.details;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;

public class CategoryDetails extends DetailsUtils {

    public String name;
    public String budget;
    public Drawable budgetIcon;
    public Drawable categoryIcon;
    public String addDate;
    public String lastEditDate;
    private final Fragment fragment;
    private final Category category;

    public CategoryDetails(int categoryId, @NonNull Fragment fragment) {
        this.fragment = fragment;

        CategoryViewModel categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);
        this.category = categoryViewModel.getCategoryById(categoryId);

        setValues();
    }

    @Override
    public void setValues() {
        name = category.getName();
        budget = category.getBudget();
        categoryIcon = super.getCategoryIcon(category);
        addDate = DateProcessor.parse(category.getAddDate());
        budgetIcon = super.getAmountIconDependOfValue(category.getBudget());
        lastEditDate = super.getValueOrLabel(category.getModifiedDate(), category.getModifiedDate() != 0);
    }

    @Override
    public Fragment getFragment() {
        return this.fragment;
    }
}
