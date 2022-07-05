package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.ui.History.HistoryBottomSheetEntity;

import java.util.List;

public class HistoryRepository {

    private BudgetRoomDatabase database;
    private HistoryDao historyDao;
    private LiveData<List<History>> allHistory;
    private LiveData<List<HistoryBottomSheetEntity>> historyBottomSheetEntity;
    private List<HistoryBottomSheetEntity> historyBottomSheetEntityList;
    private List<HistoryAndTransaction> historyAndTransactionInDateOrderList;

    public HistoryRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        historyDao = database.historyDao();
        allHistory = historyDao.getAllHistory();
        historyAndTransactionInDateOrderList = historyDao.getAllHistoryAndTransactionInDateOrderList();
        CategoryDao categoryDao = database.categoryDao();
        historyBottomSheetEntity = categoryDao.getHistoryBottomSheetEntity();
        historyBottomSheetEntityList = categoryDao.getHistoryBottomSheetEntityList();
    }

    public LiveData<List<HistoryBottomSheetEntity>> getHistoryBottomSheetEntity() {
        return historyBottomSheetEntity;
    }

    public List<HistoryBottomSheetEntity> getHistoryBottomSheetEntityList() {
        return historyBottomSheetEntityList;
    }

    public List<HistoryAndTransaction> getAllHistoryAndTransactionInDateOrderList() {
        return historyAndTransactionInDateOrderList;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionInDateOrder() {
        return historyDao.getAllHistoryAndTransactionInDateOrder();
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId) {
        return historyDao.getAllHistoryAndTransactionByCategory(categoryId);
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

    public void delete(int historyId) {
        database.databaseWriteExecutor.execute(() -> {
            historyDao.delete(historyId);
        });
    }
}
