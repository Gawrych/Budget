package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingRepository;

import java.util.List;

public class ComingViewModel extends AndroidViewModel {

    private ComingRepository comingRepository;
    private LiveData<List<Coming>> allComing;

    public ComingViewModel(@NonNull Application app) {
        super(app);
        comingRepository = new ComingRepository(app);
        allComing = comingRepository.getAllComing();
    }

    public void insert(Coming coming) {
        comingRepository.insert(coming);
    }

    public LiveData<List<Coming>> getAllComing() {
        return allComing;
    }
}