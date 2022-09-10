package com.example.budgetmanagement.ui.History;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.Calendar;

public class AddNewElementToHistory extends Fragment {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateField;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_history_element_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        dateField = rootView.findViewById(R.id.date);
        dateField.setFocusable(false);
        dateField.setCursorVisible(false);
        setDatePickerDialog();

        TextInputEditText amount = rootView.findViewById(R.id.amount);
        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        Button acceptButton = rootView.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(view -> {
            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(rootView);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData(dateField, categoryId);
            if (successfullyCollectedData) {
                submitNewHistoryItemToDatabase(newTransactionDataCollector);
                requireActivity().onBackPressed();
            }
        });

        dateField.setText(DateProcessor.getTodayDateInPattern());
        dateField.setOnClickListener(view -> datePickerDialog.show());

        TextInputEditText selectedCategory = rootView.findViewById(R.id.categoryList);
        selectedCategory.setFocusable(false);
        selectedCategory.setCursorVisible(false);
        selectedCategory.setText("Różne");
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));

        Button cancelButton = rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> requireActivity().onBackPressed());
    }

    public void setDatePickerDialog() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mYear = calendarInstance.get(Calendar.YEAR);
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    dateField.setText(DateProcessor.parseDate((selectedDate.getTimeInMillis())));
                }, mYear, mMonth, mDay);
    }

    private void submitNewHistoryItemToDatabase(NewTransactionDataCollector newItem) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        long transactionId = transactionViewModel.insert(newItem.getTransaction());
        historyViewModel.insert(new History(0, 0, (int) transactionId, LocalDate.now().toEpochDay()));
    }

    private void selectCategory(EditText categoryEditText) {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            categoryId = categoryBottomSheetSelector.getSelectedId();
            categoryEditText.setText(categoryBottomSheetSelector.getSelectedName());
        });
    }
}