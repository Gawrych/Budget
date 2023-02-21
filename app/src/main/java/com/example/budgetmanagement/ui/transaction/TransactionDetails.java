package com.example.budgetmanagement.ui.transaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.TransactionDetailsBinding;
import com.example.budgetmanagement.ui.details.TransactionConverterForBinding;

public class TransactionDetails extends Fragment {

    public static final String TRANSACTION_ID_ARG = "transactionId";
    private TransactionDetailsBinding binding;

    public static TransactionDetails newInstance(int comingId) {
        TransactionDetails fragment = new TransactionDetails();
        Bundle args = new Bundle();
        args.putInt(TRANSACTION_ID_ARG, comingId);
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
        int transactionId = getArguments() != null ? getArguments().getInt(TRANSACTION_ID_ARG, -1) : -1;

        if (transactionId == -1) {
            Toast.makeText(requireContext(), R.string.not_found_id, Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        TransactionConverterForBinding TransactionConverterForBinding = new TransactionConverterForBinding(transactionId, this);
        binding.setTransactionConverterForBinding(TransactionConverterForBinding);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}