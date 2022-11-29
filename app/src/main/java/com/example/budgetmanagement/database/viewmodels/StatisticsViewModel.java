package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.rooms.ComingRepository;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {

    private final ComingRepository comingRepository;

    public StatisticsViewModel(@NonNull Application app) {
        super(app);
        comingRepository = new ComingRepository(app);
    }

    public List<ComingAndTransaction> getAllComingByMonth(int monthNumber) {
        return comingRepository.getAllComingByYear(monthNumber);
    }

}