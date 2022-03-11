package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.CategoryAndTransaction;
import com.example.budgetmanagement.database.utils.CategoryName;
import com.example.budgetmanagement.database.Rooms.BudgetRepository;
import com.example.budgetmanagement.database.Rooms.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private BudgetRepository budgetRepository;
    private LiveData<List<Category>> allCategory;
    private LiveData<List<CategoryName>> allCategoryNames;
    private List<CategoryAndTransaction> allCategoryAndTransaction;

    public CategoryViewModel(@NonNull Application app) {
        super(app);
        budgetRepository = new BudgetRepository(app);
        allCategory = budgetRepository.getAllCategories();
        allCategoryNames = budgetRepository.getCategoryNames();
//        transactionAndCategory = budgetRepository.getAllTransactions();
        allCategoryAndTransaction = budgetRepository.getCategoryAndTransaction();
    }

    public void insert(Category category) {
        budgetRepository.insert(category);
    }

    public List<CategoryAndTransaction> getCategoryAndTransaction() {
        return allCategoryAndTransaction;
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategory;
    }

//    public List<CategoryAndTransaction> getTransactionAndCategory() {
//        return transactionAndCategory;
//    }

    public LiveData<List<CategoryName>> getAllCategoryNames() {
        return allCategoryNames;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Category getCategoryById(int id) {
        return budgetRepository.getCategoryById(id);
    }
}