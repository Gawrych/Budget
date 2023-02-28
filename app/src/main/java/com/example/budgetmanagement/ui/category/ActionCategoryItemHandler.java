package com.example.budgetmanagement.ui.category;

import static com.example.budgetmanagement.ui.utils.BundleHelper.BUNDLE_CATEGORY_ID;

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
import com.example.budgetmanagement.ui.utils.BundleHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ActionCategoryItemHandler extends BottomSheetDialogFragment {

    private ActionCategoryHandlerBottomSheetBinding binding;
    private int categoryId;
    private long lastOnDeleteClickTime;

    public static ActionCategoryItemHandler newInstance(int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_CATEGORY_ID, categoryId);
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
        this.categoryId = BundleHelper.getItemIdFromBundle(getArguments(), BUNDLE_CATEGORY_ID);
        if (categoryId == -1) {
            BundleHelper.showToUserErrorNotFoundInDatabase(requireActivity());
            dismiss();
            return;
        }

        this.binding.setActionCategoryItemHandler(this);
    }

    public void deleteActionOnCLick() {
        long currentTime = System.currentTimeMillis();
        if (this.lastOnDeleteClickTime < currentTime - 1000) {
            this.lastOnDeleteClickTime = currentTime;
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage(R.string.are_you_sure_to_delete)
                    .setPositiveButton(R.string.delete, (dialog, id) -> {
                        removeFromDatabase();
                        Toast.makeText(requireContext(), R.string.element_removed, Toast.LENGTH_SHORT).show();
                        dismiss();
                    })
                    .setNeutralButton(R.string.cancel, (dialog, id) -> {
                    }).show();
        }
    }

    private void removeFromDatabase() {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.changeAllFromDeletedCategoryToDefault(this.categoryId);

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.delete(this.categoryId);
    }

    public void editActionOnCLick() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) return;
        View rootView = parentFragment.getView();
        if (rootView == null) return;

        EditCategory editCategory = EditCategory.newInstance(this.categoryId);
        Bundle bundle = editCategory.getArguments();

        Navigation.findNavController(rootView)
                .navigate(R.id.action_navigation_category_to_editCategory, bundle);
        dismiss();
    }
}