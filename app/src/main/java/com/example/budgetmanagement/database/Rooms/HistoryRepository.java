package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryRepository {

    private BudgetRoomDatabase database;
    private HistoryDao historyDao;

    public HistoryRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        historyDao = database.historyDao();
    }

    public HistoryAndTransaction getHistoryAndTransaction(int id) {
        return historyDao.getHistoryAndTransaction(id);
    }

    public LiveData<List<HistoryAndTransaction>> getHistoryAndTransaction() {
        return historyDao.getHistoryAndTransaction();
    }

    public void insert(History history) {

        database.databaseWriteExecutor.execute(() -> {
            historyDao.insert(history);
        });
    }

    public void update(History history) {
        database.databaseWriteExecutor.execute(() -> {
            historyDao.update(history);
        });
    }

    public void delete(History history) {
        database.databaseWriteExecutor.execute(() -> {
            historyDao.delete(history);
        });
    }
}
