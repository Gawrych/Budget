package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryRepository {

    private BudgetRoomDatabase database;
    private HistoryDao historyDao;
    private LiveData<List<History>> allHistory;

    public HistoryRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        historyDao = database.historyDao();
        allHistory = historyDao.getAllHistory();
    }

    public HistoryAndTransaction getAllHistoryAndTransaction(int id) {
        return historyDao.getHistoryAndTransaction(id);
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransaction() {
        return historyDao.getHistoryAndTransaction();
    }

    public LiveData<List<History>> getAllHistory() {
        return allHistory;
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
