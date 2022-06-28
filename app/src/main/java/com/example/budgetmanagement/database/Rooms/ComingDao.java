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
public interface ComingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Coming coming);

    @Update
    void update(Coming coming);

    @Delete
    void delete(Coming coming);

    @Query("SELECT * FROM coming")
    LiveData<List<Coming>> getAllComing();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY addDate ASC")
    LiveData<List<ComingAndTransaction>> getAllComingAndTransaction();
}
