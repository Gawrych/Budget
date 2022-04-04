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

    public HistoryViewModel(@NonNull Application app) {
        super(app);
        historyRepository = new HistoryRepository(app);
        allHistoryAndTransaction = historyRepository.getAllHistoryAndTransactionInDateOrder();
        historyBottomSheetEntity = historyRepository.getHistoryBottomSheetEntity();
    }

    public LiveData<List<HistoryBottomSheetEntity>> getHistoryBottomSheetEntity() {
        return historyBottomSheetEntity;
    }

    public HistoryAndTransaction getAllHistoryAndTransaction(int position) {
        return Objects.requireNonNull(allHistoryAndTransaction.getValue()).get(position);
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionInDateOrder() {
        return allHistoryAndTransaction;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId) {
        return historyRepository.getAllHistoryAndTransactionByCategory(categoryId);
    }
}