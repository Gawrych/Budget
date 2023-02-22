package com.example.budgetmanagement.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.CategoryQuery;
import com.example.budgetmanagement.ui.utils.BundleHelper;

import java.math.BigDecimal;

public class EditCategory extends AddNewCategory implements BundleHelper {

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

        categoryToEdit = BundleHelper.getCategoryFromBundle(getArguments(),this);
        if (categoryToEdit == null) {
            BundleHelper.showToUserErrorNotFoundInDatabase(requireActivity());
            super.backToPreviousFragment();
            return;
        }

        fillTextInputFields(categoryToEdit);
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