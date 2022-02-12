package com.example.budgetmanagement.database.Category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Category.utils.CategoryName;
import com.example.budgetmanagement.database.Rooms.BudgetRepository;
import com.example.budgetmanagement.database.Rooms.Category.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private BudgetRepository budgetRepository;
    private LiveData<List<Category>> allCategory;
    private LiveData<List<CategoryName>> allCategoryNames;

    public CategoryViewModel(@NonNull Application app) {
        super(app);
        budgetRepository = new BudgetRepository(app);
        allCategory = budgetRepository.getAllCategories();
        allCategoryNames = budgetRepository.getCategoryNames();
    }

    public void insert(Category category) {
        budgetRepository.insert(category);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategory;
    }

    public LiveData<List<CategoryName>> getAllCategoryNames() {
        return allCategoryNames;
    }

    public Category getCategoryById(int id) {
        return budgetRepository.getCategoryById(id);
    }
}