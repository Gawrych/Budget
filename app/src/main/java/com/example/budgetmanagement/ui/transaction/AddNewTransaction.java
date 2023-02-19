package com.example.budgetmanagement.ui.transaction;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.TransactionQuery;
import com.example.budgetmanagement.databinding.AddNewTransactionFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewTransaction extends Fragment {

    private AddNewTransactionFragmentBinding binding;
    private String title;
    private String amount;
    private String period;
    private String categoryName;
    private String startDateInPattern;
    private String endDateInPattern;

    public AddNewTransaction() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = AddNewTransactionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeDatePicker(binding.startDate);
        initializeDatePicker(binding.endDate);
        initializeCategoryPicker(binding.categorySelector, binding.categorySelectorLayout);
        initializePeriodPicker(binding.periodPicker);

        InputTextCollector collector = new InputTextCollector(requireContext());
        binding.acceptButton.setOnClickListener(v -> {
            collectData(collector);
            if (collector.areCorrectlyCollected()) {
                submitToDatabase();
            }
            collector.resetCollectedStatus();
        });

        binding.cyclicalSwitch.setOnCheckedChangeListener((button, isChecked) ->
                disableOrEnabledCyclicalFields(isChecked));
    }

    private void disableOrEnabledCyclicalFields(boolean enabled) {
        binding.periodPickerLayout.setEnabled(enabled);
        binding.endDateLayout.setEnabled(enabled);
    }

    public void initializeCategoryPicker(AutoCompleteTextView categorySelector, TextInputLayout categorySelectorLayout) {
        CategoryBottomSheetSelector categoryPicker = new CategoryBottomSheetSelector(this);
        AppIconPack appIconPack = ((AppIconPack) requireActivity().getApplication());
        categoryPicker.getBottomSheetDialog().setOnDismissListener(v -> {
            setTextForField(
                    categoryPicker.getSelectedCategoryName(),
                    categorySelector);
            setIconForField(
                    appIconPack.getDrawableIconFromPack(categoryPicker.getIconId()),
                    categorySelectorLayout);
        });
        categorySelector.setOnClickListener(v -> categoryPicker.show());
    }

    public void setTextForField(String text, AutoCompleteTextView field) {
        field.setText(text);
    }

    public void setIconForField(Drawable icon, TextInputLayout field) {
        field.setEndIconDrawable(icon);
    }

    private void initializePeriodPicker(AutoCompleteTextView periodPicker) {
        String[] TIME_BETWEEN = getResources().getStringArray(R.array.periods);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, TIME_BETWEEN);
        periodPicker.setAdapter(adapter);
    }

    public void initializeDatePicker(AutoCompleteTextView field) {
        DatePickerDialog datePickerDialog = DateProcessor.getDatePickerDialog(requireContext(), field, System.currentTimeMillis());
        field.setOnClickListener(v -> datePickerDialog.show());
    }

    public void collectData(InputTextCollector collector) {
        this.title = collector.collect(binding.titleLayout);
        this.amount = collector.collectBasedOnProfitSwitch(binding.amountLayout, binding.profitSwitch);
        this.categoryName = collector.collect(binding.categorySelectorLayout);
        this.startDateInPattern = collector.collect(binding.startDateLayout);

        if (binding.cyclicalSwitch.isChecked()) {
            this.period = collector.collect(binding.periodPickerLayout);
            this.endDateInPattern = collector.collect(binding.endDateLayout);
        }
    }

    public void submitToDatabase() {
        TransactionQuery transactionQuery = new TransactionQuery(requireContext(), this);
        transactionQuery.createNewTransaction(
                this.title, this.amount, this.categoryName, this.startDateInPattern);

        if (binding.cyclicalSwitch.isChecked()) {
            transactionQuery.submitCyclical(this.endDateInPattern, this.period);
        } else {
            transactionQuery.submit();
        }
        backToPreviousFragment();
    }

    private void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }

    public AddNewTransactionFragmentBinding getBinding() {
        return binding;
    }
}