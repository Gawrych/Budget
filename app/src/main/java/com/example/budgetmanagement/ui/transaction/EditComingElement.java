package com.example.budgetmanagement.ui.transaction;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.NewTransactionDataCollector;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.TransactionFormService;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.Calendar;

public class EditComingElement extends TransactionFormService {

//    public static final String BUNDLE_COMING_ID = "comingId";
//    private ComingViewModel comingViewModel;
//    int comingId;
//    private Transaction transaction;
//    private TransactionViewModel transactionViewModel;
//
//    public static EditComingElement newInstance(int comingId) {
//        Bundle bundle = new Bundle();
//        bundle.putInt(BUNDLE_COMING_ID, comingId);
//        EditComingElement editComingElement = new EditComingElement();
//        editComingElement.setArguments(bundle);
//        return editComingElement;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(rootView, savedInstanceState);
//        this.comingId = getArguments() != null ? getArguments().getInt(BUNDLE_COMING_ID, -1) : -1;
//        this.comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
//        transaction = comingViewModel.getComingAndTransactionById(comingId);
//
//        if (transaction == null) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//            builder.setMessage(R.string.error_element_with_this_id_was_not_found)
//                    .setPositiveButton("Ok", (dialog, id) -> {}).show();
//            requireActivity().onBackPressed();
//            return;
//        }
//
//        fillFields();
//
//        Button acceptButton = rootView.findViewById(R.id.acceptButton);
//
//        acceptButton.setText(R.string.edit);
//        acceptButton.setOnClickListener(view -> {
//            NewTransactionDataCollector newTransactionDataCollector = new NewTransactionDataCollector(this);
//            boolean successfullyCollectedData = newTransactionDataCollector.collectData();
//            if (successfullyCollectedData) {
//                submitToDatabase(newTransactionDataCollector);
//                requireActivity().onBackPressed();
//            }
//        });
//    }
//
//    private void fillFields() {
//        TextInputEditText title = getTitleField();
//        TextInputEditText amount = getAmountField();
//        AutoCompleteTextView selectedCategory = getSelectedCategory();
//        SwitchMaterial profitSwitch = getProfitSwitch();
//        AutoCompleteTextView dateField = getStartDateField();
//
//        Transaction transaction = this.transaction;
//        title.setText(transaction.getTitle());
//
//        long repeatDate = this.transaction.getDeadline();
//        dateField.setText(DateProcessor.parseDate(repeatDate, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(repeatDate);
//        setDatePickerDialog(calendar);
//
//        BigDecimal amountInBigDecimal = new BigDecimal(transaction.getAmount());
//        profitSwitch.setChecked(amountInBigDecimal.signum() != -1);
//
//        String number = amountInBigDecimal.abs().stripTrailingZeros().toPlainString();
//        amount.setText(number);
//
//        selectedCategory.setText(CategoryBottomSheetSelector.getCategoryName(transaction.getCategoryId(), this));
//        setCategoryId(transaction.getCategoryId());
//
//        dateField.setText(DateProcessor.parseDate(this.transaction.getDeadline(), DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
//    }
//
//    public void submitToDatabase(NewTransactionDataCollector newItem) {
//        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
//        Transaction transaction = this.transaction;
//
//        boolean isFirstModification = transaction.getLastEditDate() == 0;
//        if (isFirstModification) {
//            int transactionId = (int) createNewTransaction(newItem);
//            assignNewTransaction(transaction, transactionId);
//        } else {
////            transactionViewModel.update(newItem.getTransaction(this.transaction.transaction.getTransactionId()));
//        }
//
//        long now = Calendar.getInstance().getTimeInMillis();
////        transaction.setLastEditDate(now);
////        transaction.setExpireDate(newItem.getTransaction().getAddDate());
//        comingViewModel.update(transaction);
//    }
//
//    private long createNewTransaction(NewTransactionDataCollector newItem) {
//         return transactionViewModel.insert(newItem.getTransaction());
//    }
//
//    private void assignNewTransaction(Transaction transaction, int transactionId) {
////        transaction.setTransactionId(transactionId);
//    }
}