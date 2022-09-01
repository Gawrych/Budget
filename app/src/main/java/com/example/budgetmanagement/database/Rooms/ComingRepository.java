package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ComingRepository {

    private BudgetRoomDatabase database;
    private ComingDao comingDao;

    private LiveData<List<ComingAndTransaction>> allComingAndTransaction;
    private List<ComingAndTransaction> allComingAndTransactionList;

    public ComingRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        comingDao = database.comingDao();
        allComingAndTransaction = comingDao.getAllComingAndTransaction();
        allComingAndTransactionList = comingDao.getAllComingAndTransactionList();
    }

    public LiveData<List<ComingAndTransaction>> getAllComingAndTransaction() {
        return allComingAndTransaction;
    }

    public List<ComingAndTransaction> allComingAndTransactionList() {
        return allComingAndTransactionList;
    }

    public List<ComingAndTransaction> getComingAndTransactionByYear(long startYear, long endYear) {
        return comingDao.getComingAndTransactionByYear(startYear, endYear);
    }

    public LiveData<List<ComingAndTransaction>> getComingAndTransactionByYearLiveData(long startYear, long endYear) {
        return comingDao.getComingAndTransactionByYearLiveData(startYear, endYear);
    }

    public void insert(Coming coming) {

        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.insert(coming));
    }

    public void update(Coming coming) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.update(coming));
    }

    public void updateExecute(int comingId, boolean executeValue) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.updateExecute(comingId, executeValue));
    }

    public void delete(Coming coming) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.delete(coming));
    }

    public void delete(int comingId) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> comingDao.delete(comingId));
    }
}
