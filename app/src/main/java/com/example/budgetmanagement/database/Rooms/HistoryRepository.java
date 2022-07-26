package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.ui.utils.CategoryBottomSheetEntity;

import java.util.List;

public class HistoryRepository {

    private final HistoryDao historyDao;
    private final LiveData<List<History>> allHistory;
    private final LiveData<List<CategoryBottomSheetEntity>> historyBottomSheetEntity;
    private final List<CategoryBottomSheetEntity> categoryBottomSheetEntityList;
    private final List<HistoryAndTransaction> historyAndTransactionInDateOrderList;

    public HistoryRepository(Application app) {
        BudgetRoomDatabase database = BudgetRoomDatabase.getDatabase(app);
        historyDao = database.historyDao();
        allHistory = historyDao.getAllHistory();
        historyAndTransactionInDateOrderList = historyDao.getAllHistoryAndTransactionInDateOrderList();
        CategoryDao categoryDao = database.categoryDao();
        historyBottomSheetEntity = categoryDao.getHistoryBottomSheetEntity();
        categoryBottomSheetEntityList = categoryDao.getHistoryBottomSheetEntityList();
    }

    public LiveData<List<CategoryBottomSheetEntity>> getHistoryBottomSheetEntity() {
        return historyBottomSheetEntity;
    }

    public List<CategoryBottomSheetEntity> getHistoryBottomSheetEntityList() {
        return categoryBottomSheetEntityList;
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
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> historyDao.insert(history));
    }

    public void update(History history) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> historyDao.update(history));
    }

    public void delete(History history) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> historyDao.delete(history));
    }

    public void delete(int historyId) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> historyDao.delete(historyId));
    }
}
