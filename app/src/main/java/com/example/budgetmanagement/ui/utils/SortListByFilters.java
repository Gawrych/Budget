package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.History.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.REVERSE_FILTER_ID;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;

import java.util.HashMap;
import java.util.List;

public class SortListByFilters {

    public static final byte SORT_BY_NAME_METHOD = 1;
    public static final byte SORT_BY_AMOUNT_METHOD = 2;
    public static final byte SORT_BY_DATE_METHOD = 3;

    private List<HistoryAndTransaction> originalList;
    private HashMap<Integer, Integer> filters;
    private ListSortingMethod listSortingMethod;

    public SortListByFilters(List<HistoryAndTransaction> originalList, HashMap<Integer, Integer> filters) {
        this.originalList = originalList;
        this.filters = filters;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<HistoryAndTransaction> sort() {
        listSortingMethod = new ListSortingMethod(originalList);
        Integer value = filters.get(PROFIT_FILTER_ID);
        if (value != null && value == 1) {
           listSortingMethod.filterByProfit();
        } else if (value != null && value == 2) {
            listSortingMethod.filterByLoss();
        }

        value = filters.get(ORDER_FILTER_ID);
        if (value != null && value == 1) {
            listSortingMethod.sortByName();
        } else if (value != null && value == 2) {
            listSortingMethod.sortByAmount();
        } else if (value != null && value == 3) {
            listSortingMethod.sortByDate();
        }

        value = filters.get(CATEGORY_FILTER_ID);
        if (value != null && value != 0) {
            listSortingMethod.sortByCategory(value);
        }

        value = filters.get(REVERSE_FILTER_ID);
        if (value != null && value == 1) {
            listSortingMethod.reverseList();
        }

        return listSortingMethod.getSortedList();
    }

}