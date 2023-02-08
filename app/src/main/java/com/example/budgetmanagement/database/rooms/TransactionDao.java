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
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("UPDATE transactions SET categoryId = 1 WHERE categoryId = :categoryIdToRemove")
    void changeAllFromDeletedCategoryToDefault(int categoryIdToRemove);

    @Query("DELETE FROM transactions WHERE transactionId = :transactionId")
    void delete(int transactionId);

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    Transaction getTransactionById(int transactionId);

    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    Transaction getTransaction(int transactionId);

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions ORDER BY deadline ASC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE deadlineYear = :year ORDER BY deadline ASC")
    LiveData<List<Transaction>> getAllTransactionsByYear(int year);

    @Query("SELECT * FROM transactions WHERE deadlineYear = :year ORDER BY deadline ASC")
    List<Transaction> getAllTransactionsByYearInList(int year);

    @Query("SELECT DISTINCT deadlineYear FROM transactions ORDER BY deadlineYear ASC")
    int[] getAllYears();
}
