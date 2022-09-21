package com.example.budgetmanagement.ui.Coming;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNewComingElement extends Fragment implements GetViewComingFields {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateField;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView timeBetweenExecutePicker;
    private SwitchMaterial profitSwitch;
    private SwitchMaterial cyclicalSwitch;
    private NewComingFragmentDataCollector newComingDataCollector;
    private TextInputEditText title;
    private TextInputEditText endDate;
    private TextInputLayout endDateLayout;
    private TextInputLayout timeBetweenPayLayout;
    private AutoCompleteTextView selectedCategory;
    private TextInputEditText amount;
    private TextInputLayout titleLayout;
    private TextInputLayout amountLayout;
    private boolean successfullyCollectedData;
    private final int MINIMAL_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_coming_element_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        timeBetweenPayLayout = rootView.findViewById(R.id.timeBetweenPayLayout);
        endDateLayout = rootView.findViewById(R.id.endDateLayout);
        endDate = rootView.findViewById(R.id.endDate);
        selectedCategory = rootView.findViewById(R.id.categorySelector);
        cyclicalSwitch = rootView.findViewById(R.id.cyclicalSwitch);
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
        selectedCategory.setText(R.string.category_example_various);
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));

        clearErrorWhenTextChanged(title, titleLayout);
        clearErrorWhenTextChanged(amount, amountLayout);

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

        endDate.setCursorVisible(false);
        endDate.setOnClickListener(view -> {
            datePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
                endDateLayout.setError(null);
                selectedDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(
                        DateProcessor.parseDate((selectedDate.getTimeInMillis()), MONTH_NAME_YEAR_DATE_FORMAT));
            });
            datePickerDialog.show();
        });

        cyclicalSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cyclicalSwitch.isChecked()) {
                if (adapter == null) {
                    final String[] TIME_BETWEEN = new String[] {
                            getString(R.string.each_day),
                            getString(R.string.each_week),
                            getString(R.string.each_month),
                            getString(R.string.each_quarter),
                            getString(R.string.each_year)
                    };

                    adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, TIME_BETWEEN);
                    timeBetweenExecutePicker = rootView.findViewById(R.id.timeBetweenPay);
                    timeBetweenExecutePicker.setAdapter(adapter);
                }
                timeBetweenPayLayout.setVisibility(View.VISIBLE);
                endDateLayout.setVisibility(View.VISIBLE);
            } else {
                timeBetweenPayLayout.setVisibility(View.GONE);
                endDateLayout.setVisibility(View.GONE);
            }
        });

        acceptButton.setOnClickListener(view -> {
            newComingDataCollector = new NewComingFragmentDataCollector(this);
            successfullyCollectedData = newComingDataCollector.collectData();

            if (successfullyCollectedData) {
                ArrayList<Long> dates = newComingDataCollector.getNextDates();
                int amountOfNewDates = dates.size();

//            TODO Rewrite this to better look
                if (cyclicalSwitch.isChecked()) {
                    if (amountOfNewDates > 25) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                        builder.setMessage("Czy na pewno chcesz dodać wszystkie daty? Jest ich " + amountOfNewDates)
                                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                                })
                                .setPositiveButton("Dodaj", (dialog, id) -> {
                                    submitNewComingItemToDatabase(newComingDataCollector, dates);
                                }).show();
                    }

                    if (amountOfNewDates < MINIMAL_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING) {
                        endDateLayout.setError("Nie wykona się ani razu, zmień datę lub okres");
                    }
                } else {
                    submitNewComingItemToDatabase(newComingDataCollector, dates);
                }
            }
        });
    }

    private void setDatePickerDialog() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mYear = calendarInstance.get(Calendar.YEAR);
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {}, mYear, mMonth, mDay);
    }

    private void submitNewComingItemToDatabase(NewComingFragmentDataCollector newComing, ArrayList<Long> dates) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        long transactionId = transactionViewModel.insert(newComing.getTransaction());

        for (Long date : dates) {
            comingViewModel.insert(newComing.getComing(transactionId, date));
        }
        requireActivity().onBackPressed();
    }

    private void selectCategory(EditText categoryEditText) {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            categoryId = categoryBottomSheetSelector.getSelectedId();
            categoryEditText.setText(categoryBottomSheetSelector.getSelectedName());
        });
    }

    private void clearErrorWhenTextChanged(TextInputEditText fieldToListenTextChange, TextInputLayout fieldToBeCleared) {
        fieldToListenTextChange.addTextChangedListener(getTextWatcher(fieldToBeCleared));
    }

    private TextWatcher getTextWatcher(TextInputLayout fieldToBeCleared) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                fieldToBeCleared.setError(null);
            }
        };
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
    public AutoCompleteTextView getTimeBetweenExecutePicker() {
        return timeBetweenExecutePicker;
    }

    @Override
    public SwitchMaterial getCyclicalSwitch() {
        return cyclicalSwitch;
    }

    @Override
    public SwitchMaterial getProfitSwitch() {
        return profitSwitch;
    }

    @Override
    public TextInputEditText getEndDate() {
        return endDate;
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
    public Context getFragmentContext() {
        return requireContext();
    }

    @Override
    public TextInputLayout getAmountLayoutField() {
        return amountLayout;
    }
}