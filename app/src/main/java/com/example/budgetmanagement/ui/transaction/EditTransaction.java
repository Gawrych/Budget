package com.example.budgetmanagement.ui.transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.rooms.TransactionQuery;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.EditTransactionFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

public class EditTransaction extends Fragment {

    public static final String BUNDLE_TRANSACTION_ID = "transactionId";
    private Transaction transactionToEdit;
    private EditTransactionFragmentBinding binding;

    private String title;
    private String amount;
    private String categoryName;
    private String startDateInPattern;
    private AppIconPack appIconPack;

    public static EditTransaction newInstance(int transactionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TRANSACTION_ID, transactionId);
        EditTransaction editComingElement = new EditTransaction();
        editComingElement.setArguments(bundle);
        return editComingElement;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = EditTransactionFragmentBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.binding.setEditTransaction(this);

        this.transactionToEdit = getTransactionToEditFromBundle();
        if (transactionToEdit == null) {
            showErrorToUser();
            backToPreviousFragment();
            return;
        }

        initializeDatePicker(binding.startDate);
        initializeCategoryPicker(binding.categorySelector);
        fillTextInputFields(transactionToEdit);
    }

    private Transaction getTransactionToEditFromBundle() {
        int transactionId = (getArguments() != null) ? getArguments().getInt(BUNDLE_TRANSACTION_ID, -1) : -1;
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        return transactionViewModel.getTransactionById(transactionId);
    }

    private void showErrorToUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                .setPositiveButton("Ok", (dialog, id) -> {}).show();
    }

    private void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }

    public void initializeDatePicker(AutoCompleteTextView field) {
        DatePickerDialog datePickerDialog = DateProcessor.getDatePickerDialog(requireContext(), field, transactionToEdit.getDeadline());
        field.setOnClickListener(v -> datePickerDialog.show());
    }

    public void initializeCategoryPicker(AutoCompleteTextView categorySelector) {
        CategoryBottomSheetSelector categoryPicker = new CategoryBottomSheetSelector(this);
        categoryPicker.getBottomSheetDialog().setOnDismissListener(v -> {
            setCategoryName(categoryPicker.getSelectedCategoryName());
            setCategoryIcon(categoryPicker.getIconId());
        });
        categorySelector.setOnClickListener(v -> categoryPicker.show());
    }

    private void fillTextInputFields(Transaction transaction) {
        TransactionValuesForBinding transactionValues = new TransactionValuesForBinding(
                transaction.getTitle(),
                transaction.getAmount(),
                transaction.getDeadline(),
                new BigDecimal(transaction.getAmount()).signum() > 0
        );
        this.binding.setTransactionValues(transactionValues);

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        Category category = categoryViewModel.getCategoryById(transaction.getCategoryId());
        setCategoryIcon(category.getIcon());
        setCategoryName(category.getName());
    }

    private void setCategoryName(String text) {
        this.binding.setCategoryName(text);
    }

    private void setCategoryIcon(int icon) {
        if (this.appIconPack == null) this.appIconPack = ((AppIconPack) requireActivity().getApplication());
        this.binding.setCategoryIcon(appIconPack.getDrawableIconFromPack(icon));
    }

    public void collectData(InputTextCollector collector) {
        this.title = collector.collect(binding.titleLayout);
        this.amount = collector.collectBasedOnProfitSwitch(binding.amountLayout, binding.profitSwitch);
        this.categoryName = collector.collect(binding.categorySelectorLayout);
        this.startDateInPattern = collector.collect(binding.startDateLayout);
    }

    public void submitToDatabase() {
        TransactionQuery transactionQuery = new TransactionQuery(requireContext(), this);
        transactionQuery.createTransactionToUpdate(
                this.transactionToEdit,
                this.title,
                this.amount,
                this.categoryName,
                this.startDateInPattern);

        transactionQuery.update();
        backToPreviousFragment();
    }

    public void acceptButtonClick() {
        InputTextCollector collector = new InputTextCollector(requireContext());
        collectData(collector);
        if (collector.areCorrectlyCollected()) {
            submitToDatabase();
        }
        collector.resetCollectedStatus();
    }
}