package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryRepository;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.Rooms.TransactionRepository;
import com.example.budgetmanagement.ui.History.HistoryBottomSheetEntity;

import java.util.List;

public class AddNewHistoryViewModel extends AndroidViewModel {


    private TransactionRepository transactionRepository;
    private HistoryRepository historyRepository;
    private LiveData<List<HistoryBottomSheetEntity>> historyBottomSheetEntity;

    public AddNewHistoryViewModel(@NonNull Application app) {
        super(app);
        transactionRepository = new TransactionRepository(app);
        historyRepository = new HistoryRepository(app);
        historyBottomSheetEntity = historyRepository.getHistoryBottomSheetEntity();
    }

    public void insert(History history) {
        historyRepository.insert(history);
    }

    public void insert(Transaction transaction) {
        transactionRepository.insert(transaction);
    }

    public LiveData<List<HistoryBottomSheetEntity>> getHistoryBottomSheetEntity() {
        return historyBottomSheetEntity;
    }
}