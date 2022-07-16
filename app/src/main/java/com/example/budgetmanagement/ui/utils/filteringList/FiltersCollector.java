package com.example.budgetmanagement.ui.utils.filteringList;

import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;

import android.widget.RadioButton;

import java.util.HashMap;

public class FiltersCollector {

    private HashMap<Integer, Integer> filters = new HashMap<>();
    private FilterFragmentUiManager filterFragmentUiManager;

    public FiltersCollector(FilterFragmentUiManager filterFragmentUiManager) {
        this.filterFragmentUiManager = filterFragmentUiManager;
        this.filters.put(PROFIT_FILTER_ID, 0);
        this.filters.put(ORDER_FILTER_ID, 0);
        this.filters.put(CATEGORY_FILTER_ID, 0);
        this.filters.put(REVERSE_FILTER_ID, 0);
    }

    private void collect() {
        filters.put(PROFIT_FILTER_ID, getProfitValue());
        filters.put(ORDER_FILTER_ID, getOrderValue());
        filters.put(CATEGORY_FILTER_ID, getCategorySelectValue());
        filters.put(REVERSE_FILTER_ID, getReverseValue());
    }

    private int getProfitValue() {
        RadioButton profit = filterFragmentUiManager.getRadioProfitButton();
        RadioButton loss = filterFragmentUiManager.getRadioLossButton();
        if (profit.isChecked()) {
            return 1;
        }
        if (loss.isChecked()) {
            return 2;
        }
        return 0;
    }

    private int getOrderValue() {
        if (filterFragmentUiManager.isSelectedToSortByName()) {
            return 1;
        }
        if (filterFragmentUiManager.isSelectedToSortByAmount()) {
            return 2;
        }
        if (filterFragmentUiManager.isSelectedToSortByDate()) {
            return 3;
        }
        return 0;
    }

    private int getReverseValue() {
        return filterFragmentUiManager.getReverseCheckBox().isChecked() ? 1 : 0;
    }

    private int getCategorySelectValue() {
        return filterFragmentUiManager.getSelectedCategoryId();
    }

    public HashMap<Integer, Integer> getFilters() {
        collect();
        return filters;
    }
}
