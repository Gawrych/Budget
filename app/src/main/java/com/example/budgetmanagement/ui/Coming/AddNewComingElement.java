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
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.History.NewTransactionDataCollector;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNewComingElement extends Fragment {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateField;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView timeBetweenExecutePicker;
    private SwitchMaterial cyclicalSwitch;
    private ArrayList<Long> allDateToComingAdd = new ArrayList<>();
    private NewTransactionDataCollector newTransactionDataCollector;
    private TextInputEditText endDate;
    private long endDateSelected;
    private TextInputLayout endDateLayout;


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

        TextInputLayout timeBetweenPayLayout = rootView.findViewById(R.id.timeBetweenPayLayout);

        endDateLayout = rootView.findViewById(R.id.endDateLayout);
        endDate = rootView.findViewById(R.id.endDate);
        AutoCompleteTextView selectedCategory = rootView.findViewById(R.id.categorySelector);
        cyclicalSwitch = rootView.findViewById(R.id.isCyclical);
        TextInputEditText amount = rootView.findViewById(R.id.amount);
        Button acceptButton = rootView.findViewById(R.id.acceptButton);
        dateField = rootView.findViewById(R.id.date);
        dateField.setCursorVisible(false);

        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        setDatePickerDialog();

        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        selectedCategory.setCursorVisible(false);
        selectedCategory.setText("Różne");
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));

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

                endDateSelected = selectedDate.getTimeInMillis();
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
            newTransactionDataCollector = new NewTransactionDataCollector(rootView);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData(dateField, categoryId);
            long startDateSelected = newTransactionDataCollector.getTransaction().getAddDate();
            getAllDates(startDateSelected);

            if (cyclicalSwitch.isChecked()) {
                boolean lackDatesToCreateComing = allDateToComingAdd.size() < 2;
                if (lackDatesToCreateComing) {
                    successfullyCollectedData = false;
                    endDateLayout.setError("Nie wykona się ani razu, zmień datę lub okres");
                }
            }

            if (successfullyCollectedData) {
                submitNewComingItemToDatabase(newTransactionDataCollector);
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

    private void submitNewComingItemToDatabase(NewTransactionDataCollector newItem) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        long transactionId = transactionViewModel.insert(newItem.getTransaction());

        for (Long date : allDateToComingAdd) {
            comingViewModel.insert(new Coming(0, (int) transactionId, (byte) 0, false,
                    date, 0, Calendar.getInstance().getTimeInMillis(),
                    0));
        }
    }

    private void getAllDates(long startDate) {
        allDateToComingAdd.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        long nextDate = calendar.getTimeInMillis();
        if (cyclicalSwitch.isChecked()) {
            int addAmount = 1;
            int timeBetween = getTimeBetween();

            if (timeBetweenExecutePicker.getText().toString().equals("Co kwartał")) {
                addAmount = 3;
                timeBetween = Calendar.MONTH;
            }

            while(nextDate <= endDateSelected) {
                allDateToComingAdd.add(nextDate);
                calendar.add(timeBetween, addAmount);
                nextDate = calendar.getTimeInMillis();
            }
        } else {
            allDateToComingAdd.add(nextDate);
        }

    }

    private int getTimeBetween() {
        String pickedTimeBetween = timeBetweenExecutePicker.getText().toString();
        if ("Co dzień".equals(pickedTimeBetween)) {
            return Calendar.DAY_OF_YEAR;
        } else if ("Co tydzień".equals(pickedTimeBetween)) {
            return Calendar.WEEK_OF_YEAR;
        }else if ("Co miesiąc".equals(pickedTimeBetween)) {
            return Calendar.MONTH;
        } else if ("Co rok".equals(pickedTimeBetween)) {
            return Calendar.YEAR;
        }
        return 0;
    }

    private void selectCategory(EditText categoryEditText) {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            categoryId = categoryBottomSheetSelector.getSelectedId();
            categoryEditText.setText(categoryBottomSheetSelector.getSelectedName());
        });
    }
}