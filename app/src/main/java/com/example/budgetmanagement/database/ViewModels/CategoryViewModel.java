package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.CategoryAndTransaction;
import com.example.budgetmanagement.database.utils.CategoryName;
import com.example.budgetmanagement.database.Rooms.CategoryRepository;
import com.example.budgetmanagement.database.Rooms.Category;

import java.util.List;
import java.util.Objects;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategory;
    private LiveData<List<CategoryName>> allCategoryNames;
    private List<CategoryAndTransaction> allCategoryAndTransaction;
    private List<Category> categoryList;
    private LiveData<List<Category>> categoryLiveData;

    public CategoryViewModel(@NonNull Application app) {
        super(app);
        categoryRepository = new CategoryRepository(app);
        allCategory = categoryRepository.getAllCategories();
        allCategoryNames = categoryRepository.getCategoryNames();
//        transactionAndCategory = budgetRepository.getAllTransactions();
        allCategoryAndTransaction = categoryRepository.getCategoryAndTransaction();
        categoryList = categoryRepository.getCategoryList();
        categoryLiveData = categoryRepository.getAllCategory();
    }

    public void insert(Category category) {
        categoryRepository.insert(category);
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List<CategoryAndTransaction> getCategoryAndTransaction() {
        return allCategoryAndTransaction;
    }

    public Category getCategory(int position) {
        return Objects.requireNonNull(allCategory.getValue()).get(position);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategory;
    }

    public LiveData<List<Category>> getAllCategory() {
        return categoryLiveData;
    }

    public LiveData<List<CategoryName>> getAllCategoryNames() {
        return allCategoryNames;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Category getCategoryById(int id) {
        return categoryRepository.getCategoryById(id);
    }
}