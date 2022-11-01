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
import android.widget.ScrollView;
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

public class AddNewComingElement extends TransactionFormService implements ComingFields {

    private DatePickerDialog endDatePickerDialog;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView timeBetweenExecutePicker;
    private SwitchMaterial cyclicalSwitch;
    private NewComingDataCollector newComingDataCollector;

    private AutoCompleteTextView endDate;
    private TextInputLayout endDateLayout;
    private TextInputLayout timeBetweenExecuteLayout;
    private ArrayList<Long> dates = new ArrayList<>();
    private TextView showAllNextDates;
    private ScrollView mainScrollView;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        endDateLayout = view.findViewById(R.id.endDateLayout);
        endDate = view.findViewById(R.id.endDate);
        mainScrollView = view.findViewById(R.id.mainScrollView);
        cyclicalSwitch = view.findViewById(R.id.cyclicalSwitch);
        timeBetweenExecuteLayout = view.findViewById(R.id.timeBetweenPayLayout);
        timeBetweenExecutePicker = view.findViewById(R.id.timeBetweenPay);
        showAllNextDates = view.findViewById(R.id.showAllDates);
        Button acceptButton = view.findViewById(R.id.acceptButton);

        newComingDataCollector = new NewComingDataCollector(this);

        getDatePickerDialog().setOnDismissListener(v -> collectDatesForDialogBox());

        endDatePickerDialog = setDatePickerDialog(Calendar.getInstance());

        clearErrorWhenTextChanged(endDate, endDateLayout);
        clearErrorWhenTextChanged(timeBetweenExecutePicker, timeBetweenExecuteLayout);
        clearErrorWhenTextChanged(timeBetweenExecutePicker, endDateLayout);

        endDate.setCursorVisible(false);
        Calendar today = Calendar.getInstance();
        endDate.setText(
                DateProcessor.parseDate((today.getTimeInMillis()), MONTH_NAME_YEAR_DATE_FORMAT));
        endDate.setOnClickListener(v -> {
            serviceEndDatePickerDialog();
            endDatePickerDialog.show();
        });

        timeBetweenExecutePicker.setOnItemClickListener((parent, v, position, id) -> {
            collectDatesForDialogBox();
            showAllNextDates.setVisibility(View.VISIBLE);
        });

        showAllNextDates.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.new_transactions_dates);
            StringBuilder datesInString = new StringBuilder();
            for (Long date : dates) {
                datesInString.append(DateProcessor.parseDate(date, MONTH_NAME_YEAR_DATE_FORMAT));
                datesInString.append(System.getProperty("line.separator"));
            }
            builder.setMessage(datesInString.toString())
                    .setPositiveButton(R.string.ok, (dialog, id) -> {}).show();
        });

        cyclicalSwitch.setOnCheckedChangeListener((compoundButton, b) -> adaptCyclicalFieldsVisibility());

        acceptButton.setOnClickListener(v -> {
            boolean successfullyCollectedData = newComingDataCollector.collectData();
            if (successfullyCollectedData) {
                selectSubmitMethodAndRun();
            } else {
                scrollToUpToShowErrors();
            }
        });

        ComingAndTransaction comingAndTransaction = getComingByIdFromBundle();
        if (comingAndTransaction != null) {
            fillFields(comingAndTransaction);
        }
    }

    private void scrollToUpToShowErrors() {
        mainScrollView.fullScroll(View.FOCUS_UP);
    }

    private void serviceEndDatePickerDialog() {
        Calendar selectedDate = Calendar.getInstance();
        endDatePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
            endDateLayout.setError(null);
            selectedDate.set(year, monthOfYear, dayOfMonth);
            endDate.setText(
                    DateProcessor.parseDate((selectedDate.getTimeInMillis()), MONTH_NAME_YEAR_DATE_FORMAT));
            collectDatesForDialogBox();
            showAllNextDates.setVisibility(View.VISIBLE);
        });
    }

    private void collectDatesForDialogBox() {
        newComingDataCollector.collectData();
        dates = newComingDataCollector.getNextDates();
    }

    private void close() {
        requireActivity().onBackPressed();
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

            timeBetweenExecuteLayout.setVisibility(View.VISIBLE);
            endDateLayout.setVisibility(View.VISIBLE);
        } else {
            timeBetweenExecuteLayout.setVisibility(View.GONE);
            endDateLayout.setVisibility(View.GONE);
            showAllNextDates.setVisibility(View.GONE);
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
            timeBetweenExecutePicker.setText(R.string.each_month);
            timeBetweenExecutePicker.setAdapter(adapter);
        }
    }

    private ComingAndTransaction getComingByIdFromBundle() {
        int comingId = getArguments() != null ? getArguments().getInt("comingId") : 0;
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        return comingViewModel.getComingAndTransactionById(comingId);
    }

    private void fillFields(ComingAndTransaction comingAndTransaction) {
        TextInputEditText title = getTitleField();
        TextInputEditText amount = getAmountField();
        AutoCompleteTextView selectedCategory = getSelectedCategory();
        SwitchMaterial profitSwitch = getProfitSwitch();
        AutoCompleteTextView dateField = getStartDateField();

        Transaction transaction = comingAndTransaction.transaction;
        title.setText(transaction.getTitle());

        long repeatDate = comingAndTransaction.coming.getExpireDate();
        dateField.setText(DateProcessor.parseDate(repeatDate, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(repeatDate);
        setDatePickerDialog(calendar);

        BigDecimal amountInBigDecimal = new BigDecimal(transaction.getAmount());
        profitSwitch.setChecked(amountInBigDecimal.signum() != -1);

        String number = amountInBigDecimal.abs().stripTrailingZeros().toPlainString();
        amount.setText(number);

        selectedCategory.setText(CategoryBottomSheetSelector.getCategoryName(transaction.getCategoryId(), this));
        setCategoryId(transaction.getCategoryId());

        dateField.setText(DateProcessor.parseDate(comingAndTransaction.coming.getExpireDate(), DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
    }

    private void submitCyclical(int amountOfNewDates) {
        int MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING = 2;
        if (amountOfNewDates < MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING) {
            endDateLayout.setError(getString(R.string.not_enough_to_generate_cyclical_change_endDate_or_timeBetween));
            return;
        }

        submitNewComingItemToDatabase(newComingDataCollector, dates);

        String howMuchAdded = "Dodano " + amountOfNewDates + " transakcje";
        Toast.makeText(requireContext(), howMuchAdded, Toast.LENGTH_SHORT).show();
    }

    private void submitNewComingItemToDatabase(NewComingDataCollector newComing, ArrayList<Long> dates) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        long transactionId = transactionViewModel.insert(newComing.getTransaction());

        for (Long date : dates) {
            comingViewModel.insert(newComing.getComing(transactionId, date));
        }
        close();
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
    public AutoCompleteTextView getEndDate() {
        return endDate;
    }

    @Override
    public TextInputLayout getTimeBetweenExecutePickerLayout() {
        return timeBetweenExecuteLayout;
    }

    @Override
    public TextInputLayout getEndDateLayout() {
        return endDateLayout;
    }
}