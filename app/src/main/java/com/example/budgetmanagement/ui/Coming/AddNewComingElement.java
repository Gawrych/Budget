package com.example.budgetmanagement.ui.Coming;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.TransactionFormService;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNewComingElement extends TransactionFormService implements GetViewComingFields {

    private DatePickerDialog datePickerDialog;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView timeBetweenExecutePicker;
    private SwitchMaterial cyclicalSwitch;
    private NewComingFragmentDataCollector newComingDataCollector;

    private TextInputEditText endDate;
    private TextInputLayout endDateLayout;
    private TextInputLayout timeBetweenPayLayout;
    private boolean successfullyCollectedData;
    private ArrayList<Long> dates = new ArrayList<>();
    private int comingId;
    private ComingViewModel comingViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coming_form_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        endDateLayout = rootView.findViewById(R.id.endDateLayout);
        endDate = rootView.findViewById(R.id.endDate);
        cyclicalSwitch = rootView.findViewById(R.id.cyclicalSwitch);
        timeBetweenPayLayout = rootView.findViewById(R.id.timeBetweenPayLayout);
        timeBetweenExecutePicker = rootView.findViewById(R.id.timeBetweenPay);
        Button acceptButton = rootView.findViewById(R.id.acceptButton);

        datePickerDialog = setDatePickerDialog(Calendar.getInstance());

        clearErrorWhenTextChanged(endDate, endDateLayout);
        clearErrorWhenTextChanged(timeBetweenExecutePicker, timeBetweenPayLayout);
        clearErrorWhenTextChanged(timeBetweenExecutePicker, endDateLayout);

        endDate.setCursorVisible(false);
        endDate.setOnClickListener(view -> {
            serviceDatePickerDialog();
            datePickerDialog.show();
        });

        cyclicalSwitch.setOnCheckedChangeListener((compoundButton, b) -> adaptCyclicalFieldsVisibility());

        acceptButton.setOnClickListener(view -> {
            successfullyCollectedData = collectData();
            if (successfullyCollectedData) {
                selectSubmitMethodAndRun();
                close();
            }
        });

        ComingAndTransaction comingAndTransaction = getComingByIdFromBundle();
        if (comingAndTransaction != null) {
            fillFields(comingAndTransaction);
        }
    }

    private void serviceDatePickerDialog() {
        Calendar selectedDate = Calendar.getInstance();
        datePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
            endDateLayout.setError(null);
            selectedDate.set(year, monthOfYear, dayOfMonth);
            endDate.setText(
                    DateProcessor.parseDate((selectedDate.getTimeInMillis()), MONTH_NAME_YEAR_DATE_FORMAT));
        });
    }

    private void close() {
        requireActivity().onBackPressed();
    }

    private boolean collectData() {
        newComingDataCollector = new NewComingFragmentDataCollector(this);
        return newComingDataCollector.collectData();
    }

    private void selectSubmitMethodAndRun() {
        dates = newComingDataCollector.getNextDates();
        int amountOfNewDates = dates.size();

        if (cyclicalSwitch.isChecked()) {
            submitCyclical(amountOfNewDates);
        } else {
            submitNewComingItemToDatabase(newComingDataCollector, dates);
        }
    }

    private void adaptCyclicalFieldsVisibility() {
        if (cyclicalSwitch.isChecked()) {
            initializeFieldsIfFirstTime();

            timeBetweenPayLayout.setVisibility(View.VISIBLE);
            endDateLayout.setVisibility(View.VISIBLE);
        } else {
            timeBetweenPayLayout.setVisibility(View.GONE);
            endDateLayout.setVisibility(View.GONE);
        }
    }

    private void initializeFieldsIfFirstTime() {
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
            timeBetweenExecutePicker.setAdapter(adapter);
        }
    }

    private ComingAndTransaction getComingByIdFromBundle() {
        this.comingId = getArguments() != null ? getArguments().getInt("comingId") : 0;
        this.comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        return comingViewModel.getComingAndTransactionById(comingId);
    }

    private void fillFields(ComingAndTransaction comingAndTransaction) {
        TextInputEditText title = getTitleField();
        TextInputEditText amount = getAmountField();
        AutoCompleteTextView selectedCategory = getSelectedCategory();
        SwitchMaterial profitSwitch = getProfitSwitch();
        TextInputEditText dateField = getStartDateField();

        Transaction transaction = comingAndTransaction.transaction;
        title.setText(transaction.getTitle());

        long repeatDate = comingAndTransaction.coming.getRepeatDate();
        dateField.setText(DateProcessor.parseDate(repeatDate, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(repeatDate);
        setDatePickerDialog(calendar);

        BigDecimal amountInBigDecimal = new BigDecimal(transaction.getAmount());
        profitSwitch.setChecked(amountInBigDecimal.signum() != -1);

        String number = amountInBigDecimal.abs().toString();
        amount.setText(number);

        String categoryName = CategoryBottomSheetSelector.getCategoryName(transaction.getCategoryId(), this);
        selectedCategory.setText(categoryName);

        dateField.setText(DateProcessor.parseDate(comingAndTransaction.coming.getRepeatDate(), DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
    }

    private void submitCyclical(int amountOfNewDates) {
        int MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING = 2;
        int MAX_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING_WITHOUT_ALERT = 25;
        if (amountOfNewDates < MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING) {
            endDateLayout.setError(getString(R.string.not_enough_to_generate_cyclical_change_endDate_or_timeBetween));

        }
        // TODO: Fix this, error: 'Can't access ViewModels from detached fragment'
        // else if (amountOfNewDates > MAX_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING_WITHOUT_ALERT) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//            builder.setMessage("Czy na pewno chcesz dodać wszystkie daty? Jest ich " + amountOfNewDates)
//                    .setNegativeButton(R.string.cancel, (dialog, id) -> {})
//                    .setPositiveButton("Dodaj", (dialog, id) -> {
//                        submitNewComingItemToDatabase(newComingDataCollector, dates);
//                    }).show();
        // }
         else if (amountOfNewDates > MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING) {
            submitNewComingItemToDatabase(newComingDataCollector, dates);
        }

        String howMuchAdded = "Dodano " + amountOfNewDates + " transakcje";
        Toast.makeText(requireContext(), howMuchAdded, Toast.LENGTH_SHORT).show();
    }

    private void submitNewComingItemToDatabase(NewComingFragmentDataCollector newComing, ArrayList<Long> dates) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        long transactionId = transactionViewModel.insert(newComing.getTransaction());

        for (Long date : dates) {
            comingViewModel.insert(newComing.getComing(transactionId, date));
        }
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
    public TextInputEditText getEndDate() {
        return endDate;
    }

    @Override
    public TextInputLayout getTimeBetweenExecutePickerLayout() {
        return timeBetweenPayLayout;
    }

    @Override
    public TextInputLayout getEndDateLayout() {
        return endDateLayout;
    }
}