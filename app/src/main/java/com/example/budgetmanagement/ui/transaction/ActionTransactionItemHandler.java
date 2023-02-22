package com.example.budgetmanagement.ui.transaction;

import static com.example.budgetmanagement.ui.utils.BundleHelper.BUNDLE_TRANSACTION_ID;

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
import com.example.budgetmanagement.ui.utils.BundleHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class ActionTransactionItemHandler extends BottomSheetDialogFragment {

    private ActionTransactionHandlerBottomSheetBinding binding;
    private Transaction transaction;
    private TransactionViewModel transactionViewModel;

    public static ActionTransactionItemHandler newInstance(int transactionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TRANSACTION_ID, transactionId);
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
        this.transaction = BundleHelper.getTransactionFromBundle(getArguments(), this);
        if (transaction == null) {
            BundleHelper.showToUserErrorNotFoundInDatabase(requireActivity());
            dismiss();
            return;
        }

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        this.binding.setActionTransactionItemHandler(this);
        this.binding.setIsExecuted(transaction.isExecuted());
    }

    public void executeActionOnClick(boolean isExecuted) {
        this.transaction.setExecuted(!isExecuted);
        this.transaction.setExecutedDate(System.currentTimeMillis());
        transactionViewModel.update(this.transaction);
        dismiss();
    }

    public void deleteActionOnClick() {
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

    public void editActionOnClick() {
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

    public void duplicateActionOnClick() {
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
        if (parentFragment == null) return null;
        return parentFragment.getView();
    }
}