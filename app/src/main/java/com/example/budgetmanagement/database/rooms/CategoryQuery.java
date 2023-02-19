package com.example.budgetmanagement.database.rooms;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;


public class CategoryQuery {

    private final CategoryViewModel categoryViewModel;
    private Category category;

    public CategoryQuery(ViewModelStoreOwner owner) {
        this.categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);
    }

    public void createCategory(int categoryId, long addDate, String name, String budget, int iconId, int colorRes) {
        this.category = new Category(
                categoryId,
                name,
                iconId,
                colorRes,
                budget,
                addDate,
                System.currentTimeMillis());
    }

    public void submit() {
        categoryViewModel.insert(this.category);
    }

    public void update() {
        categoryViewModel.update(this.category);
    }
}
