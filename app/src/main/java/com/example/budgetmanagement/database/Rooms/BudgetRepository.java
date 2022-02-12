package com.example.budgetmanagement.database.Rooms;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetmanagement.database.Category.utils.CategoryName;
import com.example.budgetmanagement.database.Rooms.Category.Category;
import com.example.budgetmanagement.database.Rooms.Category.CategoryDao;
import com.example.budgetmanagement.database.Rooms.Transaction.Transaction;
import com.example.budgetmanagement.database.Rooms.Transaction.TransactionDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class BudgetRepository {

    private BudgetRoomDatabase database;
//    Rest tables...
    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;

    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private Category firstCategory;
    private LiveData<List<CategoryName>> categoryNames;
    private Category categoryId1;

    public BudgetRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
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

    public void insert(Transaction transaction) {

        database.databaseWriteExecutor.execute(() -> {
            transactionDao.insert(transaction);
        });
    }

    public void update(Transaction transaction) {
        database.databaseWriteExecutor.execute(() -> {
            transactionDao.update(transaction);
        });
    }

    public void delete(Transaction transaction) {
        database.databaseWriteExecutor.execute(() -> {
            transactionDao.delete(transaction);
        });
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
