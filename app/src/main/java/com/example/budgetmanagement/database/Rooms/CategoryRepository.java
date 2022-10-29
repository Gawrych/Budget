package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.utils.CategoryName;

import java.util.List;

public class CategoryRepository {

    private final CategoryDao categoryDao;
    private final LiveData<List<Category>> allCategories;
    private final List<Category> categoryList;
    private final LiveData<List<Category>> categoryLiveData;

    public CategoryRepository(Application app) {
        BudgetRoomDatabase database = BudgetRoomDatabase.getDatabase(app);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
        categoryList = categoryDao.getCategoryList();
        categoryLiveData = categoryDao.getAllCategory();
    }

    public List<CategoryAndTransaction> getCategoryAndTransaction() {
        return categoryDao.getCategoryAndTransaction();
    }

    public LiveData<List<Category>> getAllCategory() {
        return categoryLiveData;
    }

    public Category getCategoryById(int id) {
        return categoryDao.getCategoryById(id);
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<CategoryName>> getCategoryNames() {
        return categoryDao.getAllCategoryNames();
    }

    public void insert(Category category) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> categoryDao.insert(category));
    }

    public void update(Category category) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        int mandatoryCategoryId = 1;
        if (category.getCategoryId() != mandatoryCategoryId) {
            BudgetRoomDatabase.databaseWriteExecutor.execute(() -> categoryDao.delete(category));
        }
    }
}
