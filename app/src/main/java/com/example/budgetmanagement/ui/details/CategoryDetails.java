package com.example.budgetmanagement.ui.details;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;

public class CategoryDetails extends DetailsUtils {

    public final String name;
    public final String budget;
    public final Drawable budgetIcon;
    public final Drawable categoryIcon;
    public final String addDate;
    public final String lastEditDate;
    private final Fragment fragment;

    public CategoryDetails(int categoryId, @NonNull Fragment fragment) {
        this.fragment = fragment;

        CategoryViewModel categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);
        Category category = categoryViewModel.getCategoryById(categoryId);

        name = category.getName();
        budget = category.getBudget();
        categoryIcon = getCategoryIcon(category);
        addDate = DateProcessor.parseDate(category.getAddDate());
        budgetIcon = getAmountIconDependOfValue(category.getBudget());
        lastEditDate = getValueFromModifiedDate(category.getModifiedDate());
    }

    @Override
    public Fragment getFragment() {
        return this.fragment;
    }
}
