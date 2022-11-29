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
    private final LiveData<List<ComingAndTransaction>> allComingAndTransaction;

    public ComingViewModel(@NonNull Application app) {
        super(app);
        comingRepository = new ComingRepository(app);
        allComingAndTransaction = comingRepository.getAllComingAndTransaction();
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

    public List<ComingAndTransaction> getAllComingByYear(int year) {
        return comingRepository.getAllComingByYear(year);
    }

    public ComingAndTransaction getComingAndTransactionById(int comingId) {
        return comingRepository.getComingAndTransactionById(comingId);
    }

    public LiveData<List<ComingAndTransaction>> getAllComingAndTransaction() {
        return allComingAndTransaction;
    }
}