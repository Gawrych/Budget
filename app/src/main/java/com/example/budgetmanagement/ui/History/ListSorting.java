package com.example.budgetmanagement.ui.History;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListSorting {

    public static final int SORT_BY_NAME_METHOD = 1;
    public static final int SORT_BY_AMOUNT_METHOD = 2;
    public static final int SORT_BY_DATE_METHOD = 3;

    private final int selectedMethod;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;

    public ListSorting(LiveData<List<HistoryAndTransaction>> sortedHistoryAndTransactionList, int selectedMethod) {
        this.historyAndTransactionList = sortedHistoryAndTransactionList;
        this.selectedMethod = selectedMethod;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(int profit, boolean reverseSorting) {
        setProfitFilter(profit);

        if (checkUserSelectedThisMethod(SORT_BY_NAME_METHOD)) sortByName();
        if (checkUserSelectedThisMethod(SORT_BY_AMOUNT_METHOD)) sortByAmount();
        if (checkUserSelectedThisMethod(SORT_BY_DATE_METHOD)) sortByDate();

        if (reverseSorting) reversedList();
    }

    private boolean checkUserSelectedThisMethod(int method) {
        return selectedMethod == method;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setProfitFilter(int profitOrLoss) {
        boolean showOnlyProfitTransaction = profitOrLoss == 1;
        boolean showOnlyLossTransaction = profitOrLoss == -1;

        if (showOnlyProfitTransaction) {
            historyAndTransactionList = Transformations.map(historyAndTransactionList,
                    input -> input.stream().filter(o1 -> o1.transaction.getProfit()).collect(Collectors.toList()));
        } else if (showOnlyLossTransaction) {
            historyAndTransactionList = Transformations.map(historyAndTransactionList,
                    input -> input.stream().filter(o1 -> !o1.transaction.getProfit()).collect(Collectors.toList()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void reversedList() {
        historyAndTransactionList = Transformations.map(historyAndTransactionList,
                input -> input.stream().collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
                    Collections.reverse(l); return l;})));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByName() {
        historyAndTransactionList = Transformations.map(historyAndTransactionList,
                input -> input.stream().sorted(Comparator.comparing(o ->
                        o.transaction.getTitle())).collect(Collectors.toList()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByAmount() {
        historyAndTransactionList = Transformations.map(historyAndTransactionList,
                input -> input.stream().sorted(Comparator.comparingDouble(o ->
                        new BigDecimal(o.transaction.getAmount()).doubleValue()))
                        .collect(Collectors.toList()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByDate() {
        historyAndTransactionList = Transformations.map(historyAndTransactionList,
                input -> input.stream().sorted(Comparator.comparingLong(o ->
                        o.transaction.getAddDate())).collect(Collectors.toList()));
    }

    public LiveData<List<HistoryAndTransaction>> getSortedList() {
        return historyAndTransactionList;
    }
}
