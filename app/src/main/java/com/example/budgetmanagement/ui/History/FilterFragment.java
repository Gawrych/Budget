package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_NAME_METHOD;

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
import com.example.budgetmanagement.ui.utils.FiltersCollector;
import com.example.budgetmanagement.ui.utils.SortListByFilters;
import com.example.budgetmanagement.ui.utils.UIFiltersManager;

import java.util.HashMap;
import java.util.List;

public class FilterFragment extends Fragment {

    public static final int PROFIT_FILTER_ID = 1;
    public static final int ORDER_FILTER_ID = 2;
    public static final int CATEGORY_FILTER_ID = 3;
    public static final int REVERSE_FILTER_ID = 4;

    private View root;
    private int orderSortingId = 0;
    private int selectedCategoryId = 0;

    private FilterViewModel filterViewModel;
    public List<HistoryAndTransaction> sortedList;
    private ConstraintLayout sortByNameLayout;
    private ConstraintLayout sortByAmountLayout;
    private ConstraintLayout sortByDateLayout;
    private Button resetButton;
    private Button filterButton;
    private TextView categorySelector;
    private CheckBox reverseCheckBox;
    private HashMap<Integer, Integer> filters;
    private HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_filter, container, false);

        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        resetButton = root.findViewById(R.id.resetFilters);
        filterButton = root.findViewById(R.id.filterList);
        sortByNameLayout = root.findViewById(R.id.sortByNameLayout);
        sortByAmountLayout = root.findViewById(R.id.sortByAmountLayout);
        sortByDateLayout = root.findViewById(R.id.sortByDateLayout);
        reverseCheckBox = root.findViewById(R.id.reversedCheck);
        categorySelector = root.findViewById(R.id.categorySelector);


        historyBottomSheetCategoryFilter = new HistoryBottomSheetCategoryFilter(root.getContext(), getParentFragment());

        UIFiltersManager uiFiltersManager = new UIFiltersManager(root, historyBottomSheetCategoryFilter);
        FiltersCollector filtersCollector = new FiltersCollector(uiFiltersManager);

        filters = filterViewModel.getFilters();
        uiFiltersManager.markUiFilters(filters);

        sortByNameLayout.setOnClickListener(v -> uiFiltersManager.selectOrderSorting(SORT_BY_NAME_METHOD));

        sortByAmountLayout.setOnClickListener(v -> uiFiltersManager.selectOrderSorting(SORT_BY_AMOUNT_METHOD));

        sortByDateLayout.setOnClickListener(v -> uiFiltersManager.selectOrderSorting(SORT_BY_DATE_METHOD));

        reverseCheckBox.setOnClickListener(v -> uiFiltersManager.changeReversedCheckboxTitle());

        categorySelector.setOnClickListener(v -> uiFiltersManager.showBottomSheetToFilterByCategory());

        resetButton.setOnClickListener(view -> uiFiltersManager.setDefaultValues());

        filterButton.setOnClickListener(view -> {
            filters = filtersCollector.getFilters();
            filterList(filters);

            filterViewModel.setFilters(filters);
            filterViewModel.setFilteredList(sortedList);
            requireActivity().onBackPressed();
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterList(HashMap<Integer, Integer> map) {
        SortListByFilters sortListByFilters =
                new SortListByFilters(filterViewModel.getOriginalList(), map);
        sortedList = sortListByFilters.sort();
    }
}