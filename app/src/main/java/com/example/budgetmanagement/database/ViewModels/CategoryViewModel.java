package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.utils.CategoryName;
import com.example.budgetmanagement.database.Rooms.BudgetRepository;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.TransactionAndCategory;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private BudgetRepository budgetRepository;
    private LiveData<List<Category>> allCategory;
    private LiveData<List<TransactionAndCategory>> transactionAndCategory;
    private LiveData<List<CategoryName>> allCategoryNames;

    public CategoryViewModel(@NonNull Application app) {
        super(app);
        budgetRepository = new BudgetRepository(app);
        allCategory = budgetRepository.getAllCategories();
        allCategoryNames = budgetRepository.getCategoryNames();
        transactionAndCategory = budgetRepository.getAllTransactions();
    }

    public void insert(Category category) {
        budgetRepository.insert(category);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategory;
    }

    public LiveData<List<TransactionAndCategory>> getTransactionAndCategory() {
        return transactionAndCategory;
    }

    public LiveData<List<CategoryName>> getAllCategoryNames() {
        return allCategoryNames;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Category getCategoryById(int id) {
        return budgetRepository.getCategoryById(id);
    }
}