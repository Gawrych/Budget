package com.example.budgetmanagement.ui.category;

import static com.example.budgetmanagement.ui.utils.BundleHelper.BUNDLE_CATEGORY_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.databinding.CategoryDetailsBinding;
import com.example.budgetmanagement.ui.utils.BundleHelper;

public class CategoryDetails extends Fragment {

    private CategoryDetailsBinding binding;

    public static CategoryDetails newInstance(int categoryId) {
        CategoryDetails fragment = new CategoryDetails();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CategoryDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int categoryId = BundleHelper.getItemIdFromBundle(getArguments(), BUNDLE_CATEGORY_ID);
        if (categoryId == -1) {
            BundleHelper.showToUserErrorNotFoundInDatabase(requireActivity());
            backToPreviousFragment();
            return;
        }

        CategoryValuesHandler categoryValuesHandler = new CategoryValuesHandler(categoryId, this);
        binding.setCategoryValuesHandler(categoryValuesHandler);
    }

    private void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}