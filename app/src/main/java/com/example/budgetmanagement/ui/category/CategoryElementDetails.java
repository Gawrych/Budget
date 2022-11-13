package com.example.budgetmanagement.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.CategoryElementDetailsBinding;
import com.example.budgetmanagement.ui.details.CategoryDetails;

public class CategoryElementDetails extends Fragment {

    private CategoryElementDetailsBinding binding;
    public static final String CATEGORY_ID_ARG = "categoryId";

    public static CategoryElementDetails newInstance(int categoryId) {
        CategoryElementDetails fragment = new CategoryElementDetails();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_ID_ARG, categoryId);
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

        int categoryId = getArguments() != null ? getArguments().getInt(CATEGORY_ID_ARG, -1) : -1;

        if (categoryId == -1) {
            Toast.makeText(requireContext(), R.string.not_found_id, Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        CategoryDetails categoryDetails = new CategoryDetails(categoryId, this);
        binding.setCategoryDetails(categoryDetails);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}