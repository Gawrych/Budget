package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.History.FilterFragment.CATEGORY_FILTER_NAME;
import static com.example.budgetmanagement.ui.History.FilterFragment.ORDER_FILTER_NAME;
import static com.example.budgetmanagement.ui.History.FilterFragment.PROFIT_FILTER_NAME;
import static com.example.budgetmanagement.ui.History.FilterFragment.REVERSE_FILTER_NAME;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;

import java.util.HashSet;
import java.util.List;

public class SortListByFilters {

    public static final byte SORT_BY_NAME_METHOD = 0;
    public static final byte SORT_BY_AMOUNT_METHOD = 1;
    public static final byte SORT_BY_DATE_METHOD = 2;
    public static final byte PROFIT_TRANSACTION = 1;
    public static final byte LOSS_TRANSACTION = -1;
    public static final byte REVERSE_LIST = 1;

    private List<HistoryAndTransaction> originalList;
    private HashSet<Filter> filters;
    private LiveDataListSortingMethod liveDataListSortingMethod;

    public SortListByFilters(List<HistoryAndTransaction> originalList, HashSet<Filter> filters) {
        this.originalList = originalList;
        this.filters = filters;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<HistoryAndTransaction> sort() {
        liveDataListSortingMethod = new LiveDataListSortingMethod(originalList);
        for (Filter filter : filters) {
            sortBy(filter);
        }
         List<HistoryAndTransaction> list = liveDataListSortingMethod.getSortedList();
//        if (list.get(0) != null) {
//
//            Log.d("ErrorCheck", list.get(0).transaction.getTitle());
//            Log.d("ErrorCheck", list.get(1).transaction.getTitle());
//            Log.d("ErrorCheck", list.get(2).transaction.getTitle());
//        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortBy(Filter filter) {
        if (filter.getName().equals(CATEGORY_FILTER_NAME)) {
            Log.d("ErrorCheck", CATEGORY_FILTER_NAME);
            sortByCategory(filter);
        }

        if (filter.getName().equals(PROFIT_FILTER_NAME)) {
            Log.d("ErrorCheck", PROFIT_FILTER_NAME);
            sortByProfit(filter);
        }

        if (filter.getName().equals(ORDER_FILTER_NAME)) {
            Log.d("ErrorCheck", ORDER_FILTER_NAME);
            sortByOrder(filter);
        }

        if (filter.getName().equals(REVERSE_FILTER_NAME)) {
            Log.d("ErrorCheck", REVERSE_FILTER_NAME);
            reverseList(filter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByCategory(Filter filter) {
        Log.d("ErrorCheck", CATEGORY_FILTER_NAME);
        if (filter.getValue() != -1) {
            Log.d("ErrorCheck", CATEGORY_FILTER_NAME);
            liveDataListSortingMethod.sortByCategory(filter.getValue());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByProfit(Filter filter) {
        boolean showOnlyProfitTransaction = filter.getValue() == PROFIT_TRANSACTION;
        boolean showOnlyLossTransaction = filter.getValue() == LOSS_TRANSACTION;
        if (showOnlyProfitTransaction) {
            liveDataListSortingMethod.filterByProfit();
        } else if (showOnlyLossTransaction){
            liveDataListSortingMethod.filterByLoss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByOrder(Filter filter) {
        int value = filter.getValue();
        if (value == SORT_BY_NAME_METHOD) {
            liveDataListSortingMethod.sortByName();
        } else if (value == SORT_BY_AMOUNT_METHOD) {
            liveDataListSortingMethod.sortByAmount();
        } else if (value == SORT_BY_DATE_METHOD) {
            liveDataListSortingMethod.sortByDate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void reverseList(Filter filter) {
        if (filter.getValue() == REVERSE_LIST) {
            liveDataListSortingMethod.reverseList();
        }
    }
}