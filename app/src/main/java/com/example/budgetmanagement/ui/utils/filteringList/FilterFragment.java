package com.example.budgetmanagement.ui.utils.filteringList;

import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_NAME_METHOD;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewModels.FilterViewModel;

import java.util.HashMap;
import java.util.List;

public class FilterFragment extends Fragment {

    public static final int PROFIT_FILTER_ID = 1;
    public static final int ORDER_FILTER_ID = 2;
    public static final int CATEGORY_FILTER_ID = 3;
    public static final int REVERSE_FILTER_ID = 4;

    private FilterViewModel filterViewModel;
    private HashMap<Integer, Integer> filters;
    private FilterFragmentUiManager filterFragmentUiManager;
    private FiltersCollector filtersCollector;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        filterFragmentUiManager = new FilterFragmentUiManager(view, this);

        filtersCollector = new FiltersCollector(filterFragmentUiManager);

        filters = filterViewModel.getFilters();

        filterFragmentUiManager.markUiFilters(filters);

        Button resetButton = filterFragmentUiManager.getResetButton();
        Button filterButton = filterFragmentUiManager.getFilterButton();
        ConstraintLayout sortByNameLayout = filterFragmentUiManager.getSortByNameLayout();
        ConstraintLayout sortByAmountLayout = filterFragmentUiManager.getSortByAmountLayout();
        ConstraintLayout sortByDateLayout = filterFragmentUiManager.getSortByDateLayout();
        CheckBox reverseCheckBox = filterFragmentUiManager.getReversedCheckBox();
        TextView categorySelector = filterFragmentUiManager.getCategorySelector();

        sortByNameLayout.setOnClickListener(v ->
                filterFragmentUiManager.selectOrderSorting(SORT_BY_NAME_METHOD));

        sortByAmountLayout.setOnClickListener(v ->
                filterFragmentUiManager.selectOrderSorting(SORT_BY_AMOUNT_METHOD));

        sortByDateLayout.setOnClickListener(v ->
                filterFragmentUiManager.selectOrderSorting(SORT_BY_DATE_METHOD));

        reverseCheckBox.setOnClickListener(v ->
                filterFragmentUiManager.changeReversedCheckboxTitle());

        categorySelector.setOnClickListener(v ->
                filterFragmentUiManager.showBottomSheetToSelectCategory());

        resetButton.setOnClickListener(root ->
                filterFragmentUiManager.setDefaultValues());

        filterButton.setOnClickListener(root -> {
            filters = getFiltersFromCollector();
            List<HistoryAndTransaction> sortedList = filterList(filters);
            updateFilterViewModel(sortedList);
            backToPreviousFragment();
        });

    }

    public HashMap<Integer, Integer> getFiltersFromCollector() {
        return filtersCollector.getFilters();
    }

    private void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }

    private void updateFilterViewModel(List<HistoryAndTransaction> sortedList) {
        filterViewModel.setFilters(filters);
        filterViewModel.setFilteredList(sortedList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<HistoryAndTransaction> filterList(HashMap<Integer, Integer> map) {
        SortListByFilters sortListByFilters =
                new SortListByFilters(filterViewModel.getOriginalList(), map);
       return sortListByFilters.sort();
    }
}