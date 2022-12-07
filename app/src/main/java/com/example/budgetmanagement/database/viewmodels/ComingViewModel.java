package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.rooms.ComingRepository;

import java.util.List;

public class ComingViewModel extends AndroidViewModel {

    private final ComingRepository comingRepository;

    public ComingViewModel(@NonNull Application app) {
        super(app);
        comingRepository = new ComingRepository(app);
    }

    public void insert(Coming coming) {
        comingRepository.insert(coming);
    }

    public void delete(Coming coming) {
        comingRepository.delete(coming);
    }

    public void delete(int comingId) {
        comingRepository.delete(comingId);
    }

    public void update(Coming coming) {
        comingRepository.update(coming);
    }

    public Coming getComingById(int id) {
        return comingRepository.getComingById(id);
    }

    public int[] getAllYears() {
        return comingRepository.getAllYears();
    }

    public List<ComingAndTransaction> getAllComingByYear(int year) {
        return comingRepository.getAllComingByYear(year);
    }

    public LiveData<List<ComingAndTransaction>> getAllComingAndTransactionByYear(int year) {
        return comingRepository.getAllComingAndTransactionByYear(year);
    }

    public ComingAndTransaction getComingAndTransactionById(int comingId) {
        return comingRepository.getComingAndTransactionById(comingId);
    }
}