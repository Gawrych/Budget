package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.HistoryRepository;

import java.util.List;


public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;

    public HistoryViewModel(@NonNull Application app) {
        super(app);
        historyRepository = new HistoryRepository(app);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryAndTransaction getHistoryAndTransaction(int id) {
        return historyRepository.getHistoryAndTransaction(id);
    }

    public LiveData<List<HistoryAndTransaction>> getHistoryAndTransaction() {
        return historyRepository.getHistoryAndTransaction();
    }
}