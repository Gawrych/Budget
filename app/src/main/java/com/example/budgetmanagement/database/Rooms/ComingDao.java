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

    @Query("DELETE FROM coming WHERE comingId = :comingId")
    void delete(int comingId);

    @Query("UPDATE coming SET execute = :executeValue WHERE comingId = :comingId")
    void updateExecute(int comingId, boolean executeValue);

    @Query("SELECT * FROM coming WHERE comingId = :comingId")
    ComingAndTransaction getComingAndTransaction(int comingId);

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY deadline DESC")
    LiveData<List<ComingAndTransaction>> getAllComingAndTransaction();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY deadline ASC")
    List<ComingAndTransaction> getAllComingAndTransactionList();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming WHERE deadline >= :startYear AND deadline <= :endYear ORDER BY deadline ASC")
    List<ComingAndTransaction> getComingAndTransactionByYear(long startYear, long endYear);

    @androidx.room.Transaction
    @Query("SELECT * FROM coming WHERE deadline >= :startYear AND deadline <= :endYear ORDER BY deadline ASC")
    LiveData<List<ComingAndTransaction>> getComingAndTransactionByYearLiveData(long startYear, long endYear);
}
