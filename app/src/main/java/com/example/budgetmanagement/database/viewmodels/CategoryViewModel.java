package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.rooms.CategoryAndTransaction;
import com.example.budgetmanagement.database.rooms.CategoryRepository;
import com.example.budgetmanagement.database.rooms.Category;

import java.util.List;
import java.util.Objects;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategory;
    private List<CategoryAndTransaction> allCategoryAndTransaction;
    private List<Category> categoryList;
    private LiveData<List<Category>> categoryLiveData;

    public CategoryViewModel(@NonNull Application app) {
        super(app);
        categoryRepository = new CategoryRepository(app);
        allCategory = categoryRepository.getAllCategories();
        allCategoryAndTransaction = categoryRepository.getCategoryAndTransaction();
        categoryList = categoryRepository.getCategoryList();
        categoryLiveData = categoryRepository.getAllCategory();
    }

    public void insert(Category category) {
        categoryRepository.insert(category);
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public void delete(int categoryId) {
        categoryRepository.delete(categoryId);
    }

    public void update(Category category) {
        categoryRepository.update(category);
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

    public Category getCategoryById(int id) {
        return categoryRepository.getCategoryById(id);
    }
}