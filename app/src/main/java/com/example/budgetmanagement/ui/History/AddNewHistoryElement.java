package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.budgetmanagement.ui.Coming.GetViewComingFields;
import com.example.budgetmanagement.ui.Coming.NewComingFragmentDataCollector;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;
import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNewHistoryElement extends Fragment implements GetViewTransactionFields {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateField;
    private TextInputEditText title;
    private TextInputEditText amount;
    private TextInputLayout titleLayout;
    private TextInputLayout amountLayout;
    private SwitchMaterial profitSwitch;
    private AutoCompleteTextView selectedCategory;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_history_element_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        selectedCategory = rootView.findViewById(R.id.categorySelector);
        title = rootView.findViewById(R.id.title);
        titleLayout = rootView.findViewById(R.id.titleLayout);
        amount = rootView.findViewById(R.id.amount);
        amountLayout = rootView.findViewById(R.id.amountLayout);
        profitSwitch = rootView.findViewById(R.id.profitSwitch);
        Button acceptButton = rootView.findViewById(R.id.acceptButton);
        dateField = rootView.findViewById(R.id.startDate);
        dateField.setCursorVisible(false);

        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        setDatePickerDialog();

        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        selectedCategory.setCursorVisible(false);
        selectedCategory.setText(rootView.getResources().getString(R.string.category_example_various));
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));

        // TODO Repair clearing error
        title.setOnClickListener(view -> title.setError(null));
        amount.setOnClickListener(view -> amountLayout.setError(null));
        titleLayout.setOnClickListener(view -> titleLayout.setError(null));
        amountLayout.setOnClickListener(view -> amountLayout.setError(null));

        Calendar selectedDate = Calendar.getInstance();
        dateField.setText(DateProcessor.getTodayDateInPattern(MONTH_NAME_YEAR_DATE_FORMAT));
        dateField.setOnClickListener(view -> {
            datePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
                selectedDate.set(year, monthOfYear, dayOfMonth);
                dateField.setText(
                        DateProcessor.parseDate((selectedDate.getTimeInMillis()), MONTH_NAME_YEAR_DATE_FORMAT));
            });
            datePickerDialog.show();
        });

        acceptButton.setOnClickListener(view -> {
            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(this);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData();
            if (successfullyCollectedData) {
                submitNewHistoryItemToDatabase(newTransactionDataCollector);
                requireActivity().onBackPressed();
            }
        });
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

    @Override
    public Context getFragmentContext() {
        return requireContext();
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public TextInputEditText getStartDateField() {
        return dateField;
    }

    @Override
    public SwitchMaterial getProfitSwitch() {
        return profitSwitch;
    }

    @Override
    public TextInputEditText getTitleField() {
        return title;
    }

    @Override
    public TextInputLayout getTitleLayoutField() {
        return titleLayout;
    }

    @Override
    public TextInputEditText getAmountField() {
        return amount;
    }

    @Override
    public TextInputLayout getAmountLayoutField() {
        return amountLayout;
    }
}