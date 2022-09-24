package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.ComingRepository;
import com.example.budgetmanagement.ui.Coming.Section;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ComingViewModel extends AndroidViewModel {

    private static final int DEFAULT_YEAR = 2022;
    private LiveData<List<ComingAndTransaction>> allComingAndTransactionByYear;
    private ComingRepository comingRepository;
    private LiveData<List<ComingAndTransaction>> allComingAndTransaction;
    private List<ComingAndTransaction> allComingAndTransactionList;
    private HashMap<Integer, ArrayList<ComingAndTransaction>> transactionCollectByMonthsId = new HashMap<>();
    private ArrayList<Section> sectionList = new ArrayList<>();
    private long startYear = 0;
    private long endYear = 0;


    public ComingViewModel(@NonNull Application app) {
        super(app);
        comingRepository = new ComingRepository(app);
        allComingAndTransaction = comingRepository.getAllComingAndTransaction();
        allComingAndTransactionList = comingRepository.allComingAndTransactionList();
        setYearStartAndEnd();
        allComingAndTransactionByYear = comingRepository.getComingAndTransactionByYearLiveData(startYear, endYear);
    }

    private void setYearStartAndEnd() {
        Calendar c = Calendar.getInstance();
        getLastMillisOfYear(c);
        endYear = c.getTimeInMillis();
        getFirstMillisOfYear(c);
        startYear = c.getTimeInMillis();
    }

    private void getLastMillisOfYear(Calendar c) {
        c.set(Calendar.YEAR, DEFAULT_YEAR);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
    }

    private void getFirstMillisOfYear(Calendar lastMillisOfYear) {
        lastMillisOfYear.add(Calendar.MILLISECOND, 1);
        lastMillisOfYear.add(Calendar.YEAR, -1);
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

    public void updateExecute(int comingId, boolean executeValue) {
        comingRepository.updateExecute(comingId, executeValue);
    }


    public LiveData<List<ComingAndTransaction>> getAllComingAndTransaction() {
        return allComingAndTransaction;
    }

    public List<ComingAndTransaction> getComingAndTransactionByYear(long startYear, long endYear) {
        return comingRepository.getComingAndTransactionByYear(startYear, endYear);
    }

    public void setComingAndTransactionByYearLiveData(long startYear, long endYear) {
        Log.d("ErrorHandle", "SetComing");
        allComingAndTransactionByYear = comingRepository.getComingAndTransactionByYearLiveData(startYear, endYear);
    }

    public LiveData<List<ComingAndTransaction>> getComingAndTransactionByYearLiveData() {
        return allComingAndTransactionByYear;
    }

    public ComingAndTransaction getComingAndTransactionById(int comingId) {
        return comingRepository.getComingAndTransactionById(comingId);
    }

    public List<ComingAndTransaction> getAllComingAndTransactionList() {
        return allComingAndTransactionList;
    }

    public HashMap<Integer, ArrayList<ComingAndTransaction>> getTransactionCollectByMonthsId() {
        return transactionCollectByMonthsId;
    }

    public void setTransactionCollect(HashMap<Integer, ArrayList<ComingAndTransaction>> transactionCollectByMonthsId) {
        this.transactionCollectByMonthsId = transactionCollectByMonthsId;
    }

    public ArrayList<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(ArrayList<Section> sectionList) {
        this.sectionList = sectionList;
    }
}