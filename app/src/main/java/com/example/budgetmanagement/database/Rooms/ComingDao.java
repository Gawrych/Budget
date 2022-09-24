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

    @Query("SELECT * FROM coming")
    LiveData<List<Coming>> getAllComing();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY repeatDate ASC")
    LiveData<List<ComingAndTransaction>> getAllComingAndTransaction();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming ORDER BY repeatDate ASC")
    List<ComingAndTransaction> getAllComingAndTransactionList();

    @androidx.room.Transaction
    @Query("SELECT * FROM coming WHERE repeatDate >= :startYear AND repeatDate <= :endYear ORDER BY repeatDate ASC")
    List<ComingAndTransaction> getComingAndTransactionByYear(long startYear, long endYear);

    @androidx.room.Transaction
    @Query("SELECT * FROM coming WHERE repeatDate >= :startYear AND repeatDate <= :endYear ORDER BY repeatDate ASC")
    LiveData<List<ComingAndTransaction>> getComingAndTransactionByYearLiveData(long startYear, long endYear);
}
