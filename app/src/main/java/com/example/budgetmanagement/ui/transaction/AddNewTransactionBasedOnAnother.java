package com.example.budgetmanagement.ui.transaction;

import static com.example.budgetmanagement.ui.utils.BundleHelper.BUNDLE_TRANSACTION_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.ui.utils.BundleHelper;
import java.math.BigDecimal;

public class AddNewTransactionBasedOnAnother extends AddNewTransaction {

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
        Transaction transactionAsBase = BundleHelper.getTransactionFromBundle(getArguments(), this);
        if (transactionAsBase == null) {
            BundleHelper.showToUserErrorNotFoundInDatabase(requireActivity());
            backToPreviousFragment();
            return;
        }

        fillTextInputFields(transactionAsBase);
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
