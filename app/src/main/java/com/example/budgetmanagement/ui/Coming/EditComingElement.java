package com.example.budgetmanagement.ui.Coming;

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

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        this.comingId = getArguments() != null ? getArguments().getInt("comingId") : 0;
        this.comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        comingAndTransaction = comingViewModel.getComingAndTransactionById(comingId);

        TextInputEditText title = getTitleField();
        TextInputEditText amount = getAmountField();
        AutoCompleteTextView selectedCategory = getSelectedCategory();
        SwitchMaterial profitSwitch = getProfitSwitch();
        TextInputEditText dateField = getStartDateField();
        Button acceptButton = rootView.findViewById(R.id.acceptButton);

        Transaction transaction = comingAndTransaction.transaction;

        title.setText(transaction.getTitle());

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
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        long transactionId = transactionViewModel.insert(newItem.getTransaction());
        Coming coming = comingAndTransaction.coming;

        if (comingAndTransaction.coming.isExecute()) {
            HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
            historyViewModel.updateTransactionIdInHistoryByComingId(coming.getComingId(), (int) transactionId);
        }

        coming.setTransactionId((int) transactionId);
        long now = Calendar.getInstance().getTimeInMillis();
        coming.setModifiedDate(now);
        comingViewModel.update(coming);
    }
}