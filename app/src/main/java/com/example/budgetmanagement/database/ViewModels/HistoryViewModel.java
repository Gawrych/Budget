package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.HistoryRepository;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetEntity;

import java.util.List;


public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private LiveData<List<HistoryAndTransaction>> allHistoryAndTransaction;
    private LiveData<List<CategoryBottomSheetEntity>> historyBottomSheetEntity;
    private List<CategoryBottomSheetEntity> categoryBottomSheetEntityList;
    private List<HistoryAndTransaction> historyAndTransactionInDateOrderList;

    public HistoryViewModel(@NonNull Application app) {
        super(app);
        historyRepository = new HistoryRepository(app);
        allHistoryAndTransaction = historyRepository.getAllHistoryAndTransactionInDateOrder();
        historyBottomSheetEntity = historyRepository.getHistoryBottomSheetEntity();
        categoryBottomSheetEntityList = historyRepository.getHistoryBottomSheetEntityList();
        historyAndTransactionInDateOrderList = historyRepository.getAllHistoryAndTransactionInDateOrderList();
    }

    public void delete(int historyId) {
        historyRepository.delete(historyId);
    }

    public void delete(History history) {
        historyRepository.delete(history);
    }

    public void deleteByComingId(int comingId) {
        historyRepository.deleteByComingId(comingId);
    }

    public void insert(History history) {
        historyRepository.insert(history);
    }

    public void update(History history) {
        historyRepository.update(history);
    }

    public List<CategoryBottomSheetEntity> getHistoryBottomSheetEntityList() {
        return categoryBottomSheetEntityList;
    }

    public History getByComingId(int comingId) {
       return historyRepository.getByComingId(comingId);
    }

    public void updateTransactionIdInHistoryByComingId(int comingId, int newTransactionId) {
        historyRepository.updateTransactionIdInHistoryByComingId(comingId, newTransactionId);
    }

    public List<HistoryAndTransaction> getAllHistoryAndTransactionInDateOrderList() {
        return historyAndTransactionInDateOrderList;
    }

    public LiveData<List<CategoryBottomSheetEntity>> getHistoryBottomSheetEntity() {
        return historyBottomSheetEntity;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionInDateOrder() {
        return allHistoryAndTransaction;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId) {
        return historyRepository.getAllHistoryAndTransactionByCategory(categoryId);
    }
}