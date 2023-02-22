package com.example.budgetmanagement.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;

public interface BundleHelper {

    String BUNDLE_CATEGORY_ID = "categoryIdTag";
    String BUNDLE_TRANSACTION_ID = "transactionIdTag";


    static Category getCategoryFromBundle(Bundle bundle, ViewModelStoreOwner owner) {
        int categoryId = getItemIdFromBundle(bundle, BUNDLE_CATEGORY_ID);
        if (categoryId == -1) return null;
        CategoryViewModel categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);
        return categoryViewModel.getCategoryById(categoryId);
    }

    static Transaction getTransactionFromBundle(Bundle bundle, ViewModelStoreOwner owner) {
        int transactionId = getItemIdFromBundle(bundle, BUNDLE_TRANSACTION_ID);
        TransactionViewModel transactionViewModel = new ViewModelProvider(owner).get(TransactionViewModel.class);
        return transactionViewModel.getTransactionById(transactionId);
    }

    static int getItemIdFromBundle(Bundle bundle, String tag) {
        return (bundle != null) ? bundle.getInt(tag, -1) : -1;
    }

    static void showToUserErrorNotFoundInDatabase(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                .setPositiveButton("Ok", (dialog, id) -> {}).show();
    }
}
