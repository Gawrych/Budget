package com.example.budgetmanagement.database.Rooms;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

    @Query("DELETE FROM coming WHERE comingId = :comingId")
    void delete(int comingId);

    @Query("SELECT * FROM coming")
    LiveData<List<Coming>> getAllComing();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY repeatDate ASC")
    LiveData<List<ComingAndTransaction>> getAllComingAndTransaction();
}
