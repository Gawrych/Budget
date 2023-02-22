package com.example.budgetmanagement.ui.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.databinding.CategoryElementDetailsBinding;
import com.example.budgetmanagement.ui.details.CategoryDetails;

public class CategoryElementDetails extends Fragment {

    private CategoryElementDetailsBinding binding;
    public static final String BUNDLE_CATEGORY_ID = "categoryId";

    public static CategoryElementDetails newInstance(int categoryId) {
        CategoryElementDetails fragment = new CategoryElementDetails();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CategoryElementDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int categoryId = getCategoryIdFromBundle();
        if (categoryId == -1) {
            showToUserErrorNotFoundInDatabase();
            backToPreviousFragment();
            return;
        }

        CategoryDetails categoryDetails = new CategoryDetails(categoryId, this);
        binding.setCategoryDetails(categoryDetails);
    }

    private void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }

    private int getCategoryIdFromBundle() {
        return (getArguments() != null) ? getArguments().getInt(BUNDLE_CATEGORY_ID, -1) : -1;
    }

    private void showToUserErrorNotFoundInDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                .setPositiveButton("Ok", (dialog, id) -> {}).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}