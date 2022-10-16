package com.example.budgetmanagement.ui.Coming;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.History.NewTransactionDataCollector;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.TransactionFormService;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.Calendar;

public class EditComingElement extends TransactionFormService {

    private ComingViewModel comingViewModel;
    int comingId;
    private ComingAndTransaction comingAndTransaction;
    private TransactionViewModel transactionViewModel;

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        this.comingId = getArguments() != null ? getArguments().getInt("comingId") : 0;
        this.comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        comingAndTransaction = comingViewModel.getComingAndTransactionById(comingId);

        if (comingAndTransaction == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                    .setPositiveButton("Ok", (dialog, id) -> {}).show();
            requireActivity().onBackPressed();
            return;
        }

        fillFields();

        Button acceptButton = rootView.findViewById(R.id.acceptButton);

        acceptButton.setText(R.string.edit);
        acceptButton.setOnClickListener(view -> {
            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(this);
            boolean successfullyCollectedData = newTransactionDataCollector.collectData();
            if (successfullyCollectedData) {
                submitToDatabase(newTransactionDataCollector);
                requireActivity().onBackPressed();
            }
        });
    }

    private void fillFields() {
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

        String number = amountInBigDecimal.abs().stripTrailingZeros().toPlainString();
        amount.setText(number);

        selectedCategory.setText(CategoryBottomSheetSelector.getCategoryName(transaction.getCategoryId(), this));
        setCategoryId(transaction.getCategoryId());

        dateField.setText(DateProcessor.parseDate(comingAndTransaction.coming.getRepeatDate(), DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
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