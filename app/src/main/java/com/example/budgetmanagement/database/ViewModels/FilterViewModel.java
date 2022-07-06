package com.example.budgetmanagement.database.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.ui.utils.Filter;

import java.util.HashSet;
import java.util.List;

public class FilterViewModel extends ViewModel {

    private HashSet<Filter> filters =  new HashSet<>();
    private MutableLiveData<List<HistoryAndTransaction>> originalList = new MutableLiveData<List<HistoryAndTransaction>>(){};
    private MutableLiveData<List<HistoryAndTransaction>> filteredList = new MutableLiveData<List<HistoryAndTransaction>>(){};

    public void setOriginalList(List<HistoryAndTransaction> originalList) {
        this.originalList.setValue(originalList);
    }

    public MutableLiveData<List<HistoryAndTransaction>> getOriginalList() {
        return originalList;
    }

    public void setFilteredList(List<HistoryAndTransaction> filteredList) {
        if (filteredList.get(0) != null) {
            Log.d("ErrorCheck", filteredList.get(0).transaction.getTitle());
            Log.d("ErrorCheck", filteredList.get(1).transaction.getTitle());
            Log.d("ErrorCheck", filteredList.get(2).transaction.getTitle());
        }
        this.filteredList.setValue(filteredList);
    }

    public LiveData<List<HistoryAndTransaction>> getFilteredList() {
        return filteredList;
    }

    public HashSet<Filter> getFilters() {
        return filters;
    }

    public void setFilters(HashSet<Filter> filters) {
        this.filters = filters;
    }

    public void addFilter(Filter filter) {
        if (filter != null) {
            filters.add(filter);
        }
    }

    public void removeFilter(Filter filter){
        if (filter != null){
            filters.remove(filter);
        }
    }
}
