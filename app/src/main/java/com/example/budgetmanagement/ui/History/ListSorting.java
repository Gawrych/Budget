package com.example.budgetmanagement.ui.History;

import android.os.Build;
import android.widget.CheckBox;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListSorting {

    public static final int NAME_SORT_METHOD = 1;
    public static final int AMOUNT_SORT_METHOD = 2;
    public static final int DATE_SORT_METHOD = 3;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;

    public ListSorting(LiveData<List<HistoryAndTransaction>> sortedHistoryAndTransactionList) {
        this.historyAndTransactionList = sortedHistoryAndTransactionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(CheckBox profitCheckBox, CheckBox filterByProfitCheckBox, CheckBox reversedCheckBox, int sortMethod) {
        setProfitFilter(profitCheckBox.isChecked(), filterByProfitCheckBox.isChecked());

        if (sortMethod == NAME_SORT_METHOD) sortByName();
        else if (sortMethod == AMOUNT_SORT_METHOD) sortByAmount();
        else {
            assert sortMethod == DATE_SORT_METHOD;
            sortByDate();
        }

        if (reversedCheckBox.isChecked()) reversedList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setProfitFilter(boolean profit, boolean filterProfit) {

        if (profit && filterProfit) {
            historyAndTransactionList = Transformations.map(historyAndTransactionList,
                    input -> input.stream().filter(o1 -> o1.transaction.getProfit()).collect(Collectors.toList()));
        } else if (!profit && filterProfit) {
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
                        o.transaction.getAmount())).collect(Collectors.toList()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByDate() {
        historyAndTransactionList = Transformations.map(historyAndTransactionList,
                input -> input.stream().sorted(Comparator.comparingLong(o ->
                        o.history.getAddDate())).collect(Collectors.toList()));
    }

    public LiveData<List<HistoryAndTransaction>> getSortedList() {
        return historyAndTransactionList;
    }
}
