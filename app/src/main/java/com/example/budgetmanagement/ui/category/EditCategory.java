package com.example.budgetmanagement.ui.category;

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
import com.example.budgetmanagement.database.rooms.CategoryQuery;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.maltaisn.icondialog.IconDialog;

import java.math.BigDecimal;

public class EditCategory extends AddNewCategory {

    private static final String BUNDLE_CATEGORY_ID = "categoryId";
    private Category categoryToEdit;

    public static EditCategory newInstance(int categoryId) {
        EditCategory fragment = new EditCategory();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.getBinding().setButtonTitle(getString(R.string.edit));


        categoryToEdit = getCategoryToEditFromBundle();
        if (categoryToEdit == null) {
            showToUserErrorNotFoundInDatabase();
            super.backToPreviousFragment();
            return;
        }

        fillTextInputFields(categoryToEdit);
    }

    private Category getCategoryToEditFromBundle() {
        int categoryId = (getArguments() != null) ? getArguments().getInt(BUNDLE_CATEGORY_ID, -1) : -1;
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        return categoryViewModel.getCategoryById(categoryId);
    }

    private void showToUserErrorNotFoundInDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                .setPositiveButton("Ok", (dialog, id) -> {}).show();
    }

    private void fillTextInputFields(Category category) {
        String name = category.getName();
        BigDecimal amount = new BigDecimal(category.getBudget());
        boolean isProfit = amount.signum() > 0;

        super.setIcon(category.getIcon());
        super.setColor(category.getColor());

        CategorySimpleDataForBinding categorySimpleDataForBinding =
                new CategorySimpleDataForBinding(name, amount.abs().toPlainString(), isProfit);
        getBinding().setCategorySimpleDataForBinding(categorySimpleDataForBinding);
    }

    @Override
    protected void submitToDatabase() {
        CategoryQuery categoryQuery = new CategoryQuery(this);
        categoryQuery.createCategory(
                this.categoryToEdit.getCategoryId(),
                this.categoryToEdit.getAddDate(),
                super.getTitle(),
                super.getAmount(),
                super.getIconId(),
                super.getColorResources());
        categoryQuery.update();
        super.backToPreviousFragment();
    }
}