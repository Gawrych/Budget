package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.utils.CategoryName;

import java.util.List;

public class CategoryRepository {

    private BudgetRoomDatabase database;
//    Rest tables...
    private List<CategoryAndTransaction> allTransactions;

    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private Category firstCategory;
    private LiveData<List<CategoryName>> categoryNames;

    public CategoryRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
//        allTransactions = categoryDao.getAllTransactions();
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

//    public List<CategoryAndTransaction> getAllTransactions() {
//        return allTransactions;
//    }

    public List<CategoryAndTransaction> getCategoryAndTransaction() {
        return categoryDao.getCategoryAndTransaction();
    }

    public Category getCategoryById(int id) {
        return categoryDao.getCategoryById(id);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public Category getCategoryIdByName(String name) {
        return categoryDao.getCategoryIdByName(name);
    }

    public LiveData<List<CategoryName>> getCategoryNames() {
        return categoryDao.getAllCategoryNames();
    }

    public void insert(Category category) {
        database.databaseWriteExecutor.execute(() -> {
            categoryDao.insert(category);
        });
    }

    public void update(Category category) {
        database.databaseWriteExecutor.execute(() -> {
            categoryDao.update(category);
        });
    }

    public void delete(Category category) {
        database.databaseWriteExecutor.execute(() -> {
            categoryDao.delete(category);
        });
    }
}
