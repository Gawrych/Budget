package com.example.budgetmanagement.database.Rooms;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(History history);

    @Update
    void update(History history);

    @Delete
    void delete(History history);

    @androidx.room.Transaction
    @Query("SELECT * FROM history WHERE historyId = :id")
    HistoryAndTransaction getHistoryAndTransaction(int id);

    @androidx.room.Transaction
    @Query("SELECT * FROM history")
    LiveData<List<HistoryAndTransaction>> getHistoryAndTransaction();
}
