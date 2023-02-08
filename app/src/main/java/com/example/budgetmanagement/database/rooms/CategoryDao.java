package com.example.budgetmanagement.database.rooms;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM categories WHERE categoryId = :categoryId")
    void delete(int categoryId);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    Category getCategoryById(int id);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategory();

    @Query("SELECT * FROM categories")
    List<Category> getCategoryList();
}
