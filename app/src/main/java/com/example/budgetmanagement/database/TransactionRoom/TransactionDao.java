package com.example.budgetmanagement.database.TransactionRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Query("DELETE FROM transactions WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM transactions ORDER BY add_date DESC")
    LiveData<List<Transaction>> getAllTransactions();
}
