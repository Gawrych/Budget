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
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;

import java.time.LocalDate;

public class AddNewElementToHistory extends Fragment {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_history_element_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        EditText calendar = rootView.findViewById(R.id.dateLayout);

        EditText amount = rootView.findViewById(R.id.amountLayout);
        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        Button acceptButton = rootView.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(view -> {
            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(rootView);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData(calendar, categoryId);
            if (successfullyCollectedData) {
                submitNewHistoryItemToDatabase(newTransactionDataCollector);
                requireActivity().onBackPressed();
            }
        });

        CalendarDialogBoxDatePicker calendarDialogDatePicker = new CalendarDialogBoxDatePicker();
        calendar.setText(DateProcessor.getTodayDateInPattern());
        calendar.setOnClickListener(view -> calendarDialogDatePicker.show(getParentFragmentManager(), calendar));

        EditText selectedCategory = rootView.findViewById(R.id.categoryListLayout);
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));

        Button cancelButton = rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> requireActivity().onBackPressed());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void submitNewHistoryItemToDatabase(NewTransactionDataCollector newItem) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        long transactionId = transactionViewModel.insert(newItem.getTransaction());
        historyViewModel.insert(new History(0, 0, (int) transactionId, LocalDate.now().toEpochDay()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectCategory(EditText categoryEditText) {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            categoryId = categoryBottomSheetSelector.getSelectedId();
            categoryEditText.setText(categoryBottomSheetSelector.getSelectedName());
        });
    }
}