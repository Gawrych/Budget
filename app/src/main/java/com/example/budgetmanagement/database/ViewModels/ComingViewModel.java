package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.ComingRepository;
import com.example.budgetmanagement.ui.Coming.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComingViewModel extends AndroidViewModel {

    private ComingRepository comingRepository;
    private LiveData<List<ComingAndTransaction>> allComingAndTransaction;
    private HashMap<Integer, ArrayList<ComingAndTransaction>> transactionCollectByMonthsId = new HashMap<>();
    private MutableLiveData<List<Section>> sectionList = new MutableLiveData<List<Section>>(){};

    public ComingViewModel(@NonNull Application app) {
        super(app);
        comingRepository = new ComingRepository(app);
        allComingAndTransaction = comingRepository.getAllComingAndTransaction();
    }

    public void insert(Coming coming) {
        comingRepository.insert(coming);
    }

    public LiveData<List<ComingAndTransaction>> getAllComingAndTransaction() {
        return allComingAndTransaction;
    }

    public HashMap<Integer, ArrayList<ComingAndTransaction>> getTransactionCollectByMonthsId() {
        return transactionCollectByMonthsId;
    }

    public void setTransactionCollect(HashMap<Integer, ArrayList<ComingAndTransaction>> transactionCollectByMonthsId) {
        this.transactionCollectByMonthsId = transactionCollectByMonthsId;
    }

    public LiveData<List<Section>> getSectionList() {
        return sectionList;
    }

    public void setSectionList(MutableLiveData<List<Section>> sectionList) {
        this.sectionList = sectionList;
    }
}