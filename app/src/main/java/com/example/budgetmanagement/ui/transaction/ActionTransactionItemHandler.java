package com.example.budgetmanagement.ui.transaction;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.ActionTransactionHandlerBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class ActionTransactionItemHandler extends BottomSheetDialogFragment {

    private ActionTransactionHandlerBottomSheetBinding binding;
    private static final String BUNDLE_COMING_VALUE = "coming_value";
    private TransactionViewModel transactionViewModel;
    private Transaction transaction;

    public static ActionTransactionItemHandler newInstance(int comingId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COMING_VALUE, comingId);
        ActionTransactionItemHandler bottomComing = new ActionTransactionItemHandler();
        bottomComing.setArguments(bundle);
        return bottomComing;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActionTransactionHandlerBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        int transactionId = getArguments() != null ? getArguments().getInt(BUNDLE_COMING_VALUE, -1) : -1;

        if (transactionId == -1) {
            Toast.makeText(requireContext(), R.string.coming_id_not_found, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        this.transaction = transactionViewModel.getTransactionById(transactionId);

        if (transaction == null) {
            Toast.makeText(requireContext(), R.string.coming_not_found+transactionId, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        if (transaction.isExecuted()) {
            changeButtonText();
        }

        binding.executeLayout.setOnClickListener(v -> execute());

        binding.duplicateLayout.setOnClickListener(v -> createNewComingByThisPattern());

        binding.editLayout.setOnClickListener(v -> edit());

        binding.deleteLayout.setOnClickListener(v -> delete());
    }

    private void changeButtonText() {
        binding.executeLabel.setText(R.string.realized);
    }

    public void execute() {
        boolean negateExecute = !this.transaction.isExecuted();
        this.transaction.setExecuted(negateExecute);
        this.transaction.setExecutedDate(getTodayDate().getTimeInMillis());
        updateComingInDatabase();
        dismiss();
    }

    private Calendar getTodayDate() {
        return Calendar.getInstance();
    }

    private void updateComingInDatabase() {
        transactionViewModel.update(this.transaction);
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    removeFromDatabase();
                    dismiss();
                    Toast.makeText(requireContext(), R.string.element_removed, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> {}).show();
    }

    private void removeFromDatabase() {
        transactionViewModel.delete(this.transaction);
    }

    private void edit() {
        View rootView = getRootView();
        if (rootView == null) {
            dismiss();
            return;
        }

        EditTransaction editTransaction = EditTransaction.newInstance(transaction.getTransactionId());
        Bundle bundle = editTransaction.getArguments();

        Navigation.findNavController(rootView)
                .navigate(R.id.action_navigation_transaction_to_editTransaction, bundle);
        dismiss();
    }

    private void createNewComingByThisPattern() {
        View rootView = getRootView();
        if (rootView == null) {
            dismiss();
            return;
        }

        AddNewTransactionBasedOnAnother addNewTransactionBasedOnAnother =
                AddNewTransactionBasedOnAnother.newInstance(transaction.getTransactionId());
        Bundle bundle = addNewTransactionBasedOnAnother.getArguments();

        Navigation.findNavController(rootView)
                .navigate(R.id.action_navigation_transaction_to_newTransactionBasedOnAnother, bundle);
        dismiss();
    }

    private View getRootView() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            return null;
        }

        return  parentFragment.getView();
    }
}