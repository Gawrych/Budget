package com.example.budgetmanagement.database.ViewModels;

import static com.example.budgetmanagement.ui.History.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.REVERSE_FILTER_ID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;

import java.util.HashMap;
import java.util.List;

public class FilterViewModel extends ViewModel {

    private HashMap<Integer, Integer> filters =  new HashMap<>();
    private List<HistoryAndTransaction> originalList;
    private MutableLiveData<List<HistoryAndTransaction>> filteredList = new MutableLiveData<List<HistoryAndTransaction>>(){};

    public FilterViewModel() {
        this.filters.put(PROFIT_FILTER_ID, 0);
        this.filters.put(ORDER_FILTER_ID, 0);
        this.filters.put(CATEGORY_FILTER_ID, 0);
        this.filters.put(REVERSE_FILTER_ID, 0);
    }

    public void setOriginalList(List<HistoryAndTransaction> originalList) {
        this.originalList = originalList;
    }

    public void setFilteredList(List<HistoryAndTransaction> filteredList) {
        this.filteredList.setValue(filteredList);
    }

    public void setFilters(HashMap<Integer, Integer> filters) {
        this.filters = filters;
    }

    public List<HistoryAndTransaction> getOriginalList() {
        return originalList;
    }

    public LiveData<List<HistoryAndTransaction>> getFilteredList() {
        return filteredList;
    }

    public HashMap<Integer, Integer> getFilters() {
        return filters;
    }
}
