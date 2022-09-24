package com.example.budgetmanagement.ui.Coming;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.History.AddNewHistoryElement;
import com.example.budgetmanagement.ui.History.NewTransactionDataCollector;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.TransactionFormService;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

public class EditComingElement extends TransactionFormService {

    private ComingViewModel comingViewModel;
    int comingId;
    ComingAndTransaction comingAndTransaction;
    private TransactionViewModel transactionViewModel;
    private TextInputEditText dateField;

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        this.comingId = getArguments() != null ? getArguments().getInt("comingId") : 0;
        this.comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        comingAndTransaction = comingViewModel.getComingAndTransactionById(comingId);

        if (comingAndTransaction == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage("Wystąpił błąd! Nie ma elementu w bazie o takim id")
                    .setPositiveButton("Ok", (dialog, id) -> {
                    }).show();
            requireActivity().onBackPressed();
            return;
        }

        TextInputEditText title = getTitleField();
        TextInputEditText amount = getAmountField();
        AutoCompleteTextView selectedCategory = getSelectedCategory();
        SwitchMaterial profitSwitch = getProfitSwitch();
        dateField = getStartDateField();
        Button acceptButton = rootView.findViewById(R.id.acceptButton);

        acceptButton.setText(R.string.edit);

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

        acceptButton.setOnClickListener(view -> {
            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(this);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData();
            if (successfullyCollectedData) {
                submitToDatabase(newTransactionDataCollector);
                requireActivity().onBackPressed();
            }
        });
    }

    public void submitToDatabase(NewTransactionDataCollector newItem) {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        Coming coming = comingAndTransaction.coming;

        boolean isFirstModification = coming.getModifiedDate() == 0;
        if (isFirstModification) {
            long transactionId = createNewTransaction(newItem);
            assignNewTransaction(coming, transactionId);
        } else {
            transactionViewModel.update(newItem.getTransaction(comingAndTransaction.transaction.getTransactionId()));
        }

        long now = Calendar.getInstance().getTimeInMillis();
        coming.setModifiedDate(now);
        coming.setRepeatDate(newItem.getTransaction().getAddDate());
        comingViewModel.update(coming);
    }

    private long createNewTransaction(NewTransactionDataCollector newItem) {
         return transactionViewModel.insert(newItem.getTransaction());
    }

    private void assignNewTransaction(Coming coming, long transactionId) {
        boolean isThisComingElementIsInHistory = comingAndTransaction.coming.isExecute();
        if (isThisComingElementIsInHistory) {
            HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
            historyViewModel.updateTransactionIdInHistoryByComingId(coming.getComingId(), (int) transactionId);
        }

        coming.setTransactionId((int) transactionId);
    }
}