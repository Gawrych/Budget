package com.example.budgetmanagement.ui.History;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.CategoryAdapter;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class HistoryBottomSheetCategoryFilter implements CategoryViewHolder.OnNoteListener {

    private BottomSheetDialog bottomSheetDialog;
    private final CategoryViewModel categoryViewModel;
    private int selectedCategory;

    public HistoryBottomSheetCategoryFilter(Context context, BottomSheetDialog bottomSheet, ViewModelStoreOwner owner, LifecycleOwner lifeCycleOwner) {
        bottomSheetDialog = bottomSheet;
        final CategoryAdapter categoryAdapter = new CategoryAdapter(new CategoryAdapter.CategoryDiff(), this, "history_bottom_sheet_recycler_view");
        categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(lifeCycleOwner, categoryAdapter::submitList);
        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(categoryAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void show() {
        selectedCategory = 0;
        bottomSheetDialog.show();
    }

    @Override
    public void onNoteClick(int position) {
        selectedCategory = categoryViewModel.getCategory(position).getCategoryId();
        bottomSheetDialog.cancel();
    }

    public int getSelectedCategoryId() {
        return selectedCategory;
    }
}
