package com.example.budgetmanagement.ui.transaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.math.BigDecimal;

public class AddNewTransactionBasedOnAnother extends AddNewTransaction {

    public static final String BUNDLE_TRANSACTION_ID = "transactionId";

    public AddNewTransactionBasedOnAnother() {}

    public static AddNewTransactionBasedOnAnother newInstance(int transactionId) {
        AddNewTransactionBasedOnAnother fragment = new AddNewTransactionBasedOnAnother();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_TRANSACTION_ID, transactionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Transaction transactionAsBase = getTransactionAsBaseFromBundle();
        if (transactionAsBase == null) {
            showErrorToUser();
            backToPreviousFragment();
            return;
        }

        fillTextInputFields(transactionAsBase);
    }

    private Transaction getTransactionAsBaseFromBundle() {
        int transactionId = (getArguments() != null) ? getArguments().getInt(BUNDLE_TRANSACTION_ID, -1) : -1;
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        return transactionViewModel.getTransactionById(transactionId);
    }

    private void showErrorToUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                .setPositiveButton("Ok", (dialog, id) -> {}).show();
    }

    private void fillTextInputFields(Transaction transaction) {
        TransactionValuesForBinding transactionValues = new TransactionValuesForBinding(
                transaction.getTitle(),
                transaction.getAmount(),
                transaction.getDeadline(),
                new BigDecimal(transaction.getAmount()).signum() > 0);
        super.getBinding().setTransactionValues(transactionValues);

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        Category category = categoryViewModel.getCategoryById(transaction.getCategoryId());
        super.setCategoryIcon(category.getIcon());
        super.setCategoryName(category.getName());
    }

    private void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }
}
