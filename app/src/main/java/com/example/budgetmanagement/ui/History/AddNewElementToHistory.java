package com.example.budgetmanagement.ui.History;

import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;

import java.util.Objects;

public class AddNewElementToHistory extends Fragment {

    private HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter;
    private int categoryId = 1;
    private View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.root =  inflater.inflate(R.layout.add_new_history_element_fragment, container, false);

        EditText calendar = root.findViewById(R.id.date);
        HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        EditText amount = root.findViewById(R.id.amount);
        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        Button acceptButton = root.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(view -> {
            TransactionDataCollectorFromUser transactionDataFromUserCollector = new TransactionDataCollectorFromUser(root);
            boolean correctlyCollectedData = transactionDataFromUserCollector.collectData(calendar, categoryId);
            if (correctlyCollectedData) {
                int transactionId = SubmitQuery.submitTransactionInsertQuery(this, transactionDataFromUserCollector);
                SubmitQuery.submitHistoryInsertQuery(historyViewModel, transactionId);
                requireActivity().onBackPressed();
            }
        });

        CalendarDialogBoxDatePicker calendarDialogDatePicker = new CalendarDialogBoxDatePicker();
        calendar.setText(DateProcessor.getTodayDate());
        calendar.setOnClickListener(view -> calendarDialogDatePicker.show(getParentFragmentManager(), calendar));

        EditText selectedCategory = root.findViewById(R.id.categoryList);
        selectedCategory.setOnClickListener(view -> selectCategory(historyViewModel, selectedCategory));

        Button cancelButton = root.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> requireActivity().onBackPressed());

        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectCategory(HistoryViewModel historyViewModel, EditText categoryEditText) {
        if (Objects.isNull(historyBottomSheetCategoryFilter)) {
            historyBottomSheetCategoryFilter = new HistoryBottomSheetCategoryFilter(getContext(), this);
        }
        historyBottomSheetCategoryFilter.show();
        historyBottomSheetCategoryFilter.getBottomSheetDialog().setOnDismissListener(dialogInterface -> {
            categoryId = historyBottomSheetCategoryFilter.getSelectedId();
            categoryEditText.setText(historyBottomSheetCategoryFilter.getSelectedName());
        });
    }
}