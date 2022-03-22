package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.HistoryRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
        return allHistoryAndTransaction;
    }

    public LiveData<List<HistoryAndTransaction>> getAllHistoryAndTransactionByCategory(int categoryId) {
        return historyRepository.getAllHistoryAndTransactionByCategory(categoryId);
    }
}