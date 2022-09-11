package com.example.budgetmanagement.ui.Coming;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.CategoryBottomSheetAdapter;
import com.example.budgetmanagement.database.Adapters.SimpleBottomSheetAdapter;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.History.NewTransactionDataCollector;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.DecimalDigitsInputFilter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddNewComingElement extends Fragment implements OnNoteListener {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateField;
    private ArrayList<String> names = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private TextInputEditText timeBetweenPay;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_coming_element_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        TextInputLayout timeBetweenPayLayout = rootView.findViewById(R.id.timeBetweenPayLayout);
        timeBetweenPay = rootView.findViewById(R.id.timeBetweenPay);
        TextInputLayout howManyTimesLayout = rootView.findViewById(R.id.howManyTimesLayout);
        TextInputEditText howManyTimes = rootView.findViewById(R.id.howManyTimes);
        TextInputLayout endDateLayout = rootView.findViewById(R.id.endDateLayout);
        TextInputEditText endDate = rootView.findViewById(R.id.endDate);
        TextInputEditText selectedCategory = rootView.findViewById(R.id.categoryList);
        SwitchMaterial cyclicalSwitch = rootView.findViewById(R.id.isCyclical);
        TextInputEditText amount = rootView.findViewById(R.id.amount);
        Button acceptButton = rootView.findViewById(R.id.acceptButton);

        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        dateField = rootView.findViewById(R.id.date);
        dateField.setCursorVisible(false);
        setDatePickerDialog();

        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        acceptButton.setOnClickListener(view -> {
            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(rootView);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData(dateField, categoryId);
            if (successfullyCollectedData) {
                submitNewHistoryItemToDatabase(newTransactionDataCollector);
                requireActivity().onBackPressed();
            }
        });

        selectedCategory.setCursorVisible(false);
        selectedCategory.setText("Różne");
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));


        dateField.setText(DateProcessor.getTodayDateInPattern());
        dateField.setOnClickListener(view -> {
            datePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                dateField.setText(
                        DateProcessor.parseDate((selectedDate.getTimeInMillis())));
            });
            datePickerDialog.show();
        });

        endDate.setCursorVisible(false);
        endDate.setOnClickListener(view -> {
            datePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(
                        DateProcessor.parseDate((selectedDate.getTimeInMillis())));
            });
            datePickerDialog.show();
        });

        timeBetweenPay.setText("Co miesiąc");

        cyclicalSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cyclicalSwitch.isChecked()) {
                howManyTimesLayout.setVisibility(View.VISIBLE);
                timeBetweenPayLayout.setVisibility(View.VISIBLE);
                endDateLayout.setVisibility(View.VISIBLE);
            } else {
                howManyTimesLayout.setVisibility(View.GONE);
                timeBetweenPayLayout.setVisibility(View.GONE);
                endDateLayout.setVisibility(View.GONE);
            }
        });

        names.add("Co dzień");
        names.add("Co miesiąc");
        names.add("Co kwartał");
        names.add("Co rok");

        final SimpleBottomSheetAdapter simpleBottomSheetAdapter =
                new SimpleBottomSheetAdapter(names, this);

        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(simpleBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        timeBetweenPay.setCursorVisible(false);
        timeBetweenPay.setOnClickListener(view -> {
            bottomSheetDialog.show();
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
    public void onNoteClick(int childPosition) {
        timeBetweenPay.setText(names.get(childPosition));
        bottomSheetDialog.cancel();
    }
}