package com.example.budgetmanagement.database.rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionRepository {

    private final TransactionDao transactionDao;
    private final LiveData<List<Transaction>> allTransactions;

    public TransactionRepository(Application app) {
        BudgetRoomDatabase database = BudgetRoomDatabase.getDatabase(app);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    public long insert(Transaction transaction) {
        return transactionDao.insert(transaction);
    }

    public void update(Transaction transaction) {
        transactionDao.update(transaction);
    }

    public void delete(Transaction transaction) {
        transactionDao.delete(transaction);
    }

    public void changeAllFromDeletedCategoryToDefault(int categoryIdToRemoveFromTransactions) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.changeAllFromDeletedCategoryToDefault(categoryIdToRemoveFromTransactions);
        });
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public Transaction getTransactionById(int comingId) {
        return transactionDao.getTransaction(comingId);
    }

    public List<Transaction> getAllTransactionsByYearInList(int year) {
        return transactionDao.getAllTransactionsByYearInList(year);
    }

    public LiveData<List<Transaction>> getAllTransactionsByYear(int year) {
        return transactionDao.getAllTransactionsByYear(year);
    }
    public int[] getAllYears() {
        return transactionDao.getAllYears();
    }

    public void delete(int transactionId) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> transactionDao.delete(transactionId));
    }
}
