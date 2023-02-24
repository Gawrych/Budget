package com.example.budgetmanagement.ui.transaction;

import static com.example.budgetmanagement.ui.utils.BundleHelper.BUNDLE_TRANSACTION_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.TransactionDetailsBinding;
import com.example.budgetmanagement.ui.utils.BundleHelper;

public class TransactionDetails extends Fragment {

    private TransactionDetailsBinding binding;

    public static TransactionDetails newInstance(int comingId) {
        TransactionDetails fragment = new TransactionDetails();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_TRANSACTION_ID, comingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int transactionId = BundleHelper.getItemIdFromBundle(getArguments(), BUNDLE_TRANSACTION_ID);
        if (transactionId == -1) {
            BundleHelper.showToUserErrorNotFoundInDatabase(requireActivity());
            requireActivity().onBackPressed();
            return;
        }

        TransactionValuesHandler TransactionValuesHandler = new TransactionValuesHandler(transactionId, this);
        binding.setTransactionValuesHandler(TransactionValuesHandler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}