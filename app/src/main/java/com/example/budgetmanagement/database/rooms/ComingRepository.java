package com.example.budgetmanagement.database.rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ComingRepository {

    private final ComingDao comingDao;
    private final LiveData<List<ComingAndTransaction>> allComingAndTransaction;

    public ComingRepository(Application app) {
        BudgetRoomDatabase database = BudgetRoomDatabase.getDatabase(app);
        comingDao = database.comingDao();
        allComingAndTransaction = comingDao.getAllComingAndTransaction();
    }

    public LiveData<List<ComingAndTransaction>> getAllComingAndTransaction() {
        return allComingAndTransaction;
    }

    public ComingAndTransaction getComingAndTransactionById(int comingId) {
        return comingDao.getComingAndTransaction(comingId);
    }

    public Coming getComingById(int id) {
        return comingDao.getComingById(id);
    }

    public List<ComingAndTransaction> getAllComingByYear(int monthNumber) {
        return comingDao.getAllComingByYear(monthNumber);
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
