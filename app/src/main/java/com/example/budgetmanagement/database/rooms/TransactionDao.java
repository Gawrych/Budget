package com.example.budgetmanagement.database.rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("UPDATE transactions SET categoryId = 1 WHERE categoryId = :categoryIdToRemove")
    void changeAllFromDeletedCategoryToDefault(int categoryIdToRemove);
}
