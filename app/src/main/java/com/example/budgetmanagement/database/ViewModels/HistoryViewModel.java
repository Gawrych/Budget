package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.HistoryRepository;
import com.example.budgetmanagement.ui.History.HistoryBottomSheetEntity;

import java.util.List;
import java.util.Objects;


public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private LiveData<List<HistoryAndTransaction>> allHistoryAndTransaction;
    private LiveData<List<HistoryBottomSheetEntity>> historyBottomSheetEntity;
    private List<HistoryBottomSheetEntity> historyBottomSheetEntityList;

    public HistoryViewModel(@NonNull Application app) {
        super(app);
        historyRepository = new HistoryRepository(app);
        allHistoryAndTransaction = historyRepository.getAllHistoryAndTransactionInDateOrder();
        historyBottomSheetEntity = historyRepository.getHistoryBottomSheetEntity();
        historyBottomSheetEntityList = historyRepository.getHistoryBottomSheetEntityList();
    }

    public void delete(int historyId) {
        historyRepository.delete(historyId);
    }

    public void insert(History history) {
        historyRepository.insert(history);
    }

    public List<HistoryBottomSheetEntity> getHistoryBottomSheetEntityList() {
        return historyBottomSheetEntityList;
    }

    public LiveData<List<HistoryBottomSheetEntity>> getHistoryBottomSheetEntity() {
        return historyBottomSheetEntity;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionInDateOrder() {
        return allHistoryAndTransaction;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId) {
        return historyRepository.getAllHistoryAndTransactionByCategory(categoryId);
    }
}