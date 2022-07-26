package com.example.budgetmanagement.ui.utils.filteringList;

import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.ui.History.HistoryAndTransactionListSortingMethod;

import java.util.HashMap;
import java.util.List;

public class SortListByFilters {

    public static final byte SORT_BY_NAME_METHOD = 1;
    public static final byte SORT_BY_AMOUNT_METHOD = 2;
    public static final byte SORT_BY_DATE_METHOD = 3;

    private final List<HistoryAndTransaction> originalList;
    private final HashMap<Integer, Integer> filters;

    public SortListByFilters(List<HistoryAndTransaction> originalList, HashMap<Integer, Integer> filters) {
        this.originalList = originalList;
        this.filters = filters;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<HistoryAndTransaction> sort() {
        HistoryAndTransactionListSortingMethod historyAndTransactionListSortingMethod =
                new HistoryAndTransactionListSortingMethod(originalList);
        int value = getValueByFilterName(PROFIT_FILTER_ID);
        if (value == 1) {
           historyAndTransactionListSortingMethod.filterByProfit();
        } else if (value == 2) {
            historyAndTransactionListSortingMethod.filterByLoss();
        }

        value = getValueByFilterName(ORDER_FILTER_ID);
        if (value == 1) {
            historyAndTransactionListSortingMethod.sortByName();
        } else if (value == 2) {
            historyAndTransactionListSortingMethod.sortByAmount();
        } else if (value == 3) {
            historyAndTransactionListSortingMethod.sortByDate();
        }

        value = getValueByFilterName(CATEGORY_FILTER_ID);
        if (value != 0) {
            historyAndTransactionListSortingMethod.sortByCategory(value);
        }

        value = getValueByFilterName(REVERSE_FILTER_ID);
        if (value == 1) {
            historyAndTransactionListSortingMethod.reverseList();
        }

        return historyAndTransactionListSortingMethod.getSortedList();
    }

    private int getValueByFilterName(int filterName) {
        Integer value = filters.get(filterName);
        return value != null ? value : 0;
    }

}