package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.History.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.REVERSE_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_NAME_METHOD;

import android.widget.RadioButton;

import java.util.HashMap;

public class FiltersCollector {

    private HashMap<Integer, Integer> filters = new HashMap<>();
    private UIFiltersManager uiFiltersManager;

    public FiltersCollector(UIFiltersManager uiFiltersManager) {
        this.uiFiltersManager = uiFiltersManager;
        this.filters.put(PROFIT_FILTER_ID, 0);
        this.filters.put(ORDER_FILTER_ID, 0);
        this.filters.put(CATEGORY_FILTER_ID, 0);
        this.filters.put(REVERSE_FILTER_ID, 0);
    }

    private void collect() {
        filters.put(PROFIT_FILTER_ID, getProfitValue());
        filters.put(ORDER_FILTER_ID, getOrderValue(uiFiltersManager.getSortMethod()));
        filters.put(CATEGORY_FILTER_ID, getCategorySelectValue());
        filters.put(REVERSE_FILTER_ID, getReverseValue());
    }

    private int getProfitValue() {
        RadioButton profit = uiFiltersManager.getRadioProfitButton();
        RadioButton loss = uiFiltersManager.getRadioLossButton();
        if (profit.isChecked()) {
            return 1;
        }
        if (loss.isChecked()) {
            return 2;
        }
        return 0;
    }

    private int getOrderValue(int sortMethod) {
        if (sortMethod == SORT_BY_NAME_METHOD) {
            return 1;
        }
        if (sortMethod == SORT_BY_AMOUNT_METHOD) {
            return 2;
        }
        if (sortMethod == SORT_BY_DATE_METHOD) {
            return 3;
        }
        return 0;
    }

    private int getReverseValue() {
        return uiFiltersManager.getReverseCheckBox().isChecked() ? 1 : 0;
    }

    private int getCategorySelectValue() {
        return uiFiltersManager.getSelectedCategoryId();
    }

    public HashMap<Integer, Integer> getFilters() {
        collect();
        return filters;
    }
}
