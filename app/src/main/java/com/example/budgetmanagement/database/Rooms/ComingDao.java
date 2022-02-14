package com.example.budgetmanagement.database.Rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface ComingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Coming coming);

    @Update
    void update(Coming coming);

    @Delete
    void delete(Coming coming);
}
