package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.HistoryRepository;

import java.util.List;
import java.util.Objects;


public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private LiveData<List<History>> allHistory;
    private LiveData<List<HistoryAndTransaction>> allHistoryAndTransaction;

    public HistoryViewModel(@NonNull Application app) {
        super(app);
        historyRepository = new HistoryRepository(app);
        allHistory = historyRepository.getAllHistory();
        allHistoryAndTransaction = historyRepository.getAllHistoryAndTransactionInAmountOrder();
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public HistoryAndTransaction getAllHistoryAndTransaction(int id) {
//        return historyRepository.getHistoryAndTransaction(id);
//    }

    public HistoryAndTransaction getAllHistoryAndTransaction(int position) {
        return Objects.requireNonNull(allHistoryAndTransaction.getValue()).get(position);
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionInAmountOrder() {
        return historyRepository.getAllHistoryAndTransactionInAmountOrder();
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId) {
        return historyRepository.getAllHistoryAndTransactionByCategory(categoryId);
    }
}