package com.example.budgetmanagement.database.Rooms;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetmanagement.database.utils.CategoryName;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM categories WHERE name = :name")
    Category getCategoryIdByName(String name);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    Category getCategoryById(int id);

    @Query("SELECT * FROM categories ORDER BY addDate DESC")
    LiveData<List<Category>> getAllCategory();

    @Query("SELECT name, budget FROM categories ORDER BY categoryId ASC")
    LiveData<List<CategoryName>> getAllCategoryNames();

    @androidx.room.Transaction
    @Query("SELECT * FROM categories")
    List<CategoryAndTransaction> getCategoryAndTransaction();

    @Query("SELECT categoryId, icon, name FROM categories")
    LiveData<List<CategoryBottomSheetEntity>> getHistoryBottomSheetEntity();

    @Query("SELECT categoryId, icon, name FROM categories")
    List<CategoryBottomSheetEntity> getHistoryBottomSheetEntityList();

    @Query("SELECT * FROM categories")
    List<Category> getCategoryList();

//    @androidx.room.Transaction
//    @Query("SELECT * FROM coming")
//    List<ComingWithTransactionAndCategory> getComingWithTransactionAndCategory();

//    @Query("SELECT * FROM categories WHERE categoryId = :id")
//    CategoryAndTransaction> getCategoryAndTransaction(int id);
}
