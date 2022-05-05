package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;

public class AddNewHistory extends Fragment {

    private int categoryId = 1;
    EditText calendar;
    RadioGroup radioGroup;
    private HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.activity_add_new_history, container, false);

        EditText titleEditText = root.findViewById(R.id.title);
        EditText amountEditText = root.findViewById(R.id.amount);

        radioGroup = root.findViewById(R.id.radioGroup);

//        LinearProgressIndicator loading = root.findViewById(R.id.loading);
//        loading.hide();
//        loading.show();

        HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        Button acceptButton = root.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(view -> {
            submitQuery(titleEditText, amountEditText, transactionViewModel, historyViewModel);
            requireActivity().onBackPressed();
        });

        Button cancelButton = root.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> requireActivity().onBackPressed());

        EditText selectedCategory = root.findViewById(R.id.categoryList);
        selectedCategory.setOnClickListener(view -> showBottomSheetCategoryList(historyViewModel, selectedCategory));

        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        calendar = root.findViewById(R.id.date);
        calendar.setOnClickListener(view -> {
            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                calendar.setText(sdf.format(selection));
            });
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void submitQuery(EditText titleEditText, EditText amountEditText, TransactionViewModel transactionViewModel, HistoryViewModel historyViewModel) {
        String title;
        float amount;
        long date;
        boolean profit;


        title = titleEditText.getText().toString();
        if (title.length() == 0) {
            title = "Brak";
        }

        String amountString = amountEditText.getText().toString();
        if (amountString.length() == 0) {
            amount = 0;
        } else {
            amount = Float.parseFloat(amountString);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate parsedDate = LocalDate.parse(calendar.getText(), formatter);
        date = parsedDate.toEpochDay();

        int profitIcon = radioGroup.getCheckedRadioButtonId();

        profit = profitIcon == R.id.profitTrue;

        int transactionId = (int) transactionViewModel.insert(new Transaction(0,
                this.categoryId, title, amount, date, LocalDate.now().toEpochDay(), profit));
        historyViewModel.insert(new History(0, transactionId, LocalDate.now().toEpochDay()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBottomSheetCategoryList(HistoryViewModel historyViewModel, EditText categoryEditText) {
        if (Objects.isNull(historyBottomSheetCategoryFilter)) {
            historyBottomSheetCategoryFilter = new HistoryBottomSheetCategoryFilter(historyViewModel);
            historyBottomSheetCategoryFilter.build(getContext(), getViewLifecycleOwner());
        }
        historyBottomSheetCategoryFilter.show();
        historyBottomSheetCategoryFilter.getBottomSheetDialog().setOnDismissListener(dialogInterface -> {
            categoryId = historyBottomSheetCategoryFilter.getSelectedId();
            categoryEditText.setText(historyBottomSheetCategoryFilter.getSelectedName());
        });
    }
}