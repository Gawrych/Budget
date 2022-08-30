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

    @Query("DELETE FROM history WHERE historyId = :historyId")
    void delete(int historyId);

    @Query("DELETE FROM history WHERE comingId = :comingId")
    void deleteByComingId(int comingId);

    @Query("SELECT * FROM history")
    LiveData<List<History>> getAllHistory();

    @androidx.room.Transaction
    @Query("SELECT * FROM history LEFT JOIN transactions ON history.transactionId = transactions.transactionId ORDER BY transactions.addDate DESC")
    LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionInDateOrder();

    @androidx.room.Transaction
    @Query("SELECT * FROM history LEFT JOIN transactions ON history.transactionId = transactions.transactionId ORDER BY transactions.addDate DESC")
    List<HistoryAndTransaction> getAllHistoryAndTransactionInDateOrderList();


    @androidx.room.Transaction
    @Query("SELECT * FROM history LEFT JOIN transactions ON history.transactionId = transactions.transactionId WHERE categoryId = :categoryId")
    LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId);

}
