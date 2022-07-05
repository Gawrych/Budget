package com.example.budgetmanagement.ui.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LiveDataListSortingMethod {

    private List<HistoryAndTransaction> historyAndTransactionList;

    public LiveDataListSortingMethod(List<HistoryAndTransaction> historyAndTransactionList) {
        this.historyAndTransactionList = historyAndTransactionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterByProfit() {
        historyAndTransactionList = historyAndTransactionList.stream().filter(o1 -> o1.transaction.getProfit()).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterByLoss() {
        historyAndTransactionList = historyAndTransactionList.stream().filter(o1 -> !o1.transaction.getProfit()).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reverseList() {
        historyAndTransactionList = historyAndTransactionList.stream().collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
                    Collections.reverse(l); return l;}));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByName() {
        historyAndTransactionList = historyAndTransactionList.stream().sorted(Comparator.comparing(o ->
                        o.transaction.getTitle())).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByAmount() {
        historyAndTransactionList = historyAndTransactionList.stream().sorted(Comparator.comparingDouble(o ->
                        new BigDecimal(o.transaction.getAmount()).doubleValue()))
                        .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByDate() {
        historyAndTransactionList = historyAndTransactionList.stream().sorted(Comparator.comparingLong(o ->
                        o.transaction.getAddDate())).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByCategory(int categoryId) {
        historyAndTransactionList = historyAndTransactionList.stream()
                        .filter(o1 -> o1.transaction.getCategoryId() == categoryId)
                        .collect(Collectors.toList());

    }

    public List<HistoryAndTransaction> getSortedList() {
        return historyAndTransactionList;
    }
}
