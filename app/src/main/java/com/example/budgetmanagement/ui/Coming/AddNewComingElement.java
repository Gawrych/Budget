package com.example.budgetmanagement.ui.Coming;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.app.DatePickerDialog;
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
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;
import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;
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
    private Button acceptButton;
    private TextInputLayout titleLayout;
    private TextInputLayout amountLayout;

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
        cyclicalSwitch = rootView.findViewById(R.id.isCyclical);
        title = rootView.findViewById(R.id.title);
        titleLayout = rootView.findViewById(R.id.titleLayout);
        amount = rootView.findViewById(R.id.amount);
        amountLayout = rootView.findViewById(R.id.amountLayout);
        profitSwitch = rootView.findViewById(R.id.isProfit);
        acceptButton = rootView.findViewById(R.id.acceptButton);
        dateField = rootView.findViewById(R.id.startDate);
        dateField.setCursorVisible(false);

        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        setDatePickerDialog();

        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        selectedCategory.setCursorVisible(false);
        selectedCategory.setText("Różne");
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));


        title.setOnClickListener(view -> titleLayout.setError(null));
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
                    adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, COUNTRIES);
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
            boolean successfullyCollectedData = newComingDataCollector.collectData();

            ArrayList<Long> dates =  newComingDataCollector.getAllDates();

            if (cyclicalSwitch.isChecked()) {
                boolean notEnoughDatesToCreateCyclicalComing = dates.size() < 2;
                if (notEnoughDatesToCreateCyclicalComing) {
                    successfullyCollectedData = false;
                    endDateLayout.setError("Nie wykona się ani razu, zmień datę lub okres");
                }
            }

            if (successfullyCollectedData) {
                submitNewComingItemToDatabase(newComingDataCollector, dates);
                requireActivity().onBackPressed();
            }
        });
    }

    private static final String[] COUNTRIES = new String[] {
            "Co dzień", "Co tydzień", "Co miesiąc", "Co kwartał", "Co rok"
    };

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
    }

    private void selectCategory(EditText categoryEditText) {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            categoryId = categoryBottomSheetSelector.getSelectedId();
            categoryEditText.setText(categoryBottomSheetSelector.getSelectedName());
        });
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
    public TextInputLayout getTitleFieldLayout() {
        return titleLayout;
    }

    @Override
    public TextInputEditText getAmountField() {
        return amount;
    }

    @Override
    public TextInputLayout getAmountFieldLayout() {
        return amountLayout;
    }
}