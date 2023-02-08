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
public interface ComingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Coming coming);

    @Update
    void update(Coming coming);

    @Delete
    void delete(Coming coming);

    @Query("DELETE FROM coming WHERE comingId = :comingId")
    void delete(int comingId);

    @androidx.room.Transaction
    @Query("SELECT * FROM coming WHERE comingId = :id")
    Coming getComingById(int id);

    @Query("SELECT * FROM coming WHERE comingId = :comingId")
    Transaction getComingAndTransaction(int comingId);

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY expireDate ASC")
    LiveData<List<Transaction>> getAllComingAndTransaction();

    @Query("SELECT * FROM coming WHERE expireYear = :year ORDER BY expireDate ASC")
    LiveData<List<Transaction>> getAllComingAndTransactionByYear(int year);

    @Query("SELECT * FROM coming WHERE expireYear = :year ORDER BY expireDate ASC")
    List<Transaction> getAllComingByYear(int year);

    @Query("SELECT DISTINCT expireYear FROM coming ORDER BY expireYear ASC")
    int[] getAllYears();
}
