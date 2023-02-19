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
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.ActionCategoryHandlerBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ActionCategoryItemHandler extends BottomSheetDialogFragment {

    private ActionCategoryHandlerBottomSheetBinding binding;
    private static final String BUNDLE_CATEGORY_VALUE = "coming_value";
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private int categoryId;

    public static ActionCategoryItemHandler newInstance(int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_CATEGORY_VALUE, categoryId);
        ActionCategoryItemHandler bottomCategory = new ActionCategoryItemHandler();
        bottomCategory.setArguments(bundle);
        return bottomCategory;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActionCategoryHandlerBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        this.transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        this.categoryId = getArguments() != null ? getArguments().getInt(BUNDLE_CATEGORY_VALUE, -1) : -1;

        if (categoryId == -1) {
            Toast.makeText(requireContext(), R.string.category_id_not_found, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        binding.editLayout.setOnClickListener(v -> editSelectedElement());

        binding.deleteLayout.setOnClickListener(v -> deleteItem());
    }

    private void deleteItem() {
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
        transactionViewModel.changeAllFromDeletedCategoryToDefault(this.categoryId);
        categoryViewModel.delete(this.categoryId);
    }

    private void editSelectedElement() {
        View rootView = getRootView();
        if (rootView == null) {
            dismiss();
            return;
        }

        EditCategory editCategory = EditCategory.newInstance(this.categoryId);
        Bundle bundle = editCategory.getArguments();

        Navigation.findNavController(rootView)
                .navigate(R.id.action_navigation_category_to_editCategory, bundle);
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