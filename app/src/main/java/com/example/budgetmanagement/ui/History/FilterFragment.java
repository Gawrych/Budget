package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.utils.SortListByFilters.LOSS_TRANSACTION;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.PROFIT_TRANSACTION;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewModels.FilterViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.ui.utils.Filter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashSet;
import java.util.List;

public class FilterFragment extends Fragment {

    public static final String PROFIT_FILTER_NAME = "profit";
    public static final String ORDER_FILTER_NAME = "order";
    public static final String CATEGORY_FILTER_NAME = "category";
    public static final String REVERSE_FILTER_NAME = "reverse";

    private View root;
    private ConstraintLayout sortByNameLayout;
    private ConstraintLayout sortByAmountLayout;
    private ConstraintLayout sortByDateLayout;

    private ImageView sortByNameIconImageView;
    private ImageView sortByAmountIconImageView;
    private ImageView sortByDateIconImageView;

    private int orderSortingId = -1;
    private int selectedCategoryId = -1;

    private FilterViewModel filterViewModel;
    private HistoryViewModel historyViewModel;

    private String selectedCategoryName;

    private Button resetButton;
    private Button filterButton;
    private MutableLiveData<List<HistoryAndTransaction>> listMutableLiveData;
    private List<HistoryAndTransaction> list;

    private RadioGroup radioGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterViewModel = new ViewModelProvider(requireParentFragment()).get(FilterViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_filter, container, false);

//        listMutableLiveData = filterViewModel.getOriginalList();

        listMutableLiveData = filterViewModel.getOriginalList();
        List<HistoryAndTransaction> historyAndTransactions = listMutableLiveData.getValue();
        if (historyAndTransactions == null) {
            Log.d("NullChecker", "Null");
        }
//        String title = list.get(0).transaction.getTitle();
//        Log.d("ErrorCheck", title);
//        MutableLiveData<List<HistoryAndTransaction>> list = filterViewModel.getOriginalList();
//        Log.d("ErrorCheck", Objects.requireNonNull(list.getValue()).get(0).transaction.getTitle());

        sortByNameLayout = root.findViewById(R.id.sortByNameLayout);
        sortByAmountLayout = root.findViewById(R.id.sortByAmountLayout);
        sortByDateLayout = root.findViewById(R.id.sortByDateLayout);

        sortByNameIconImageView = root.findViewById(R.id.sortByNameIcon);
        sortByAmountIconImageView = root.findViewById(R.id.sortByAmountIcon);
        sortByDateIconImageView = root.findViewById(R.id.sortByDateIcon);

        resetButton = root.findViewById(R.id.resetFilters);
        filterButton = root.findViewById(R.id.filterList);

        sortByNameLayout.setOnClickListener(v -> {
            unselectAll();
            if (orderSortingId != 0) {
                orderSortingId = 0;
                sortByNameIconImageView.setImageResource(R.drawable.block_color);
                sortByNameLayout.setBackgroundColor(getResources().getColor(R.color.very_light_gray));
//                addFilter(PROFIT_FILTER_NAME, SORT_BY_NAME_METHOD);
            }
        });

        sortByAmountLayout.setOnClickListener(v -> {
            unselectAll();
            if (orderSortingId != 1) {
                orderSortingId = 1;
                sortByAmountIconImageView.setImageResource(R.drawable.dollar_coin_color);
                sortByAmountLayout.setBackgroundColor(getResources().getColor(R.color.very_light_gray));
//                addFilter(PROFIT_FILTER_NAME, SORT_BY_AMOUNT_METHOD);
            }
        });

        sortByDateLayout.setOnClickListener(v -> {
            unselectAll();
            if (orderSortingId != 2) {
                orderSortingId = 2;
                sortByDateIconImageView.setImageResource(R.drawable.calendar_color);
                sortByDateLayout.setBackgroundColor(getResources().getColor(R.color.very_light_gray));
//                addFilter(PROFIT_FILTER_NAME, SORT_BY_DATE_METHOD);
            }
        });

        radioGroup = root.findViewById(R.id.profitFilter);

        Button categoryFilter = root.findViewById(R.id.showCategoryBottomSheetSelector);
        categoryFilter.setOnClickListener(view -> {
            showBottomSheetToFilterByCategory();
        });


        resetButton.setOnClickListener(view -> {
            resetFilters();
        });

        filterButton.setOnClickListener(view -> {
            addFilters();
            filterList();
            requireActivity().onBackPressed();
        });

        return root;
    }

    private void addFilters() {
        if (getProfitValue() != 0) {
            addFilter(PROFIT_FILTER_NAME, getProfitValue());
        }
        if (getCheckboxValue() != 0) {
            addFilter(REVERSE_FILTER_NAME, getCheckboxValue());
        }
        if (getCategoryId() != -1) {
            addFilter(CATEGORY_FILTER_NAME, getCategoryId());
        }
        if (getOrderValue() != -1) {
            addFilter(ORDER_FILTER_NAME, getOrderValue());
        }
    }

    private void addFilter(String name, int value) {
        filterViewModel.addFilter(new Filter(name, value));
    }

    private int getCheckboxValue() {
        CheckBox reverseCheckBox = root.findViewById(R.id.reversedCheck);
        return reverseCheckBox.isChecked() ? 1 : 0;
    }

    private int getCategoryId() {
        return selectedCategoryId;
    }

    private int getProfitValue() {
        int radioCheckedButtonId = radioGroup.getCheckedRadioButtonId();
        int incomeStatement = 0;
        if (radioCheckedButtonId == R.id.profitIcon) {
            incomeStatement = PROFIT_TRANSACTION;
        } else if (radioCheckedButtonId == R.id.lossIcon) {
            incomeStatement = LOSS_TRANSACTION;
        }
        return incomeStatement;
    }

    private int getOrderValue() {
        return orderSortingId;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterList() {
//        SortListByFilters sortListByFilters =
//                new SortListByFilters(listMutableLiveData, filterViewModel.getFilters());
//        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();
//        filterViewModel.setFilteredList(sortedList);

    }

    private void resetFilters() {
        filterViewModel.setFilters(new HashSet<>());
    }

    private void unselectAll() {
        sortByNameIconImageView.setImageResource(R.drawable.block);
        sortByAmountIconImageView.setImageResource(R.drawable.dollar_coin);
        sortByDateIconImageView.setImageResource(R.drawable.calendar);

        sortByNameLayout.setBackgroundColor(getResources().getColor(R.color.white));
        sortByAmountLayout.setBackgroundColor(getResources().getColor(R.color.white));
        sortByDateLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBottomSheetToFilterByCategory() {
        HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter = new HistoryBottomSheetCategoryFilter(getContext(), getViewLifecycleOwner(), historyViewModel);
        historyBottomSheetCategoryFilter.show();
        BottomSheetDialog bottomSheetDialog = historyBottomSheetCategoryFilter.getBottomSheetDialog();
        bottomSheetDialog.setOnCancelListener(v -> {
            selectedCategoryName = historyBottomSheetCategoryFilter.getSelectedName();
            selectedCategoryId = historyBottomSheetCategoryFilter.getSelectedId();
            Toast.makeText(getContext(), selectedCategoryName, Toast.LENGTH_SHORT).show();
        });

    }
}