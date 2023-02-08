package com.example.budgetmanagement.database.rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ComingRepository {

    private final ComingDao comingDao;
    private final LiveData<List<Transaction>> allComingAndTransaction;

    public ComingRepository(Application app) {
        BudgetRoomDatabase database = BudgetRoomDatabase.getDatabase(app);
        comingDao = database.comingDao();
        allComingAndTransaction = comingDao.getAllComingAndTransaction();
    }

    public LiveData<List<Transaction>> getAllComingAndTransaction() {
        return allComingAndTransaction;
    }

    public Transaction getComingAndTransactionById(int comingId) {
        return comingDao.getComingAndTransaction(comingId);
    }

    public Coming getComingById(int id) {
        return comingDao.getComingById(id);
    }

    public List<Transaction> getAllComingByYear(int year) {
        return comingDao.getAllComingByYear(year);
    }

    public LiveData<List<Transaction>> getAllComingAndTransactionByYear(int year) {
        return comingDao.getAllComingAndTransactionByYear(year);
    }

    public int[] getAllYears() {
        return comingDao.getAllYears();
    }

    public void insert(Coming coming) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.insert(coming));
    }

    public void update(Coming coming) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.update(coming));
    }

    public void delete(Coming coming) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.delete(coming));
    }

    public void delete(int comingId) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.delete(comingId));
    }

}
