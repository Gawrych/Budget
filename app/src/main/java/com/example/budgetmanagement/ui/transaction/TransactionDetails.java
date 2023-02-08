package com.example.budgetmanagement.ui.transaction;

import static com.example.budgetmanagement.ui.details.TransactionDetails.MODE_AFTER_DEADLINE;
import static com.example.budgetmanagement.ui.details.TransactionDetails.MODE_NORMAL;
import static com.example.budgetmanagement.ui.details.TransactionDetails.MODE_REALIZED;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.TransactionDetailsBinding;

import java.util.Calendar;

public class TransactionDetails extends Fragment {

    public static final String TRANSACTION_ID_ARG = "transactionId";
    private TransactionDetailsBinding binding;
    private Transaction transaction;

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

        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        this.transaction = transactionViewModel.getTransactionById(transactionId);

        int mode = getMode();
        com.example.budgetmanagement.ui.details.TransactionDetails transactionDetails = new com.example.budgetmanagement.ui.details.TransactionDetails(transactionId, this, mode);
        binding.setTransactionDetails(transactionDetails);
    }

    private int getMode() {
        boolean isExecute = transaction.isExecuted();
        if (isExecute) {
            return MODE_REALIZED;
        }

        boolean isBeforeDeadline = getRemainingDays(transaction.getDeadline()) > 0;
        return isBeforeDeadline ? MODE_NORMAL : MODE_AFTER_DEADLINE;
    }

    private int getRemainingDays(long repeatDate) {
        Calendar todayDate = Calendar.getInstance();
        Calendar deadlineDate = getCalendarWithValue(repeatDate);
        return deadlineDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}