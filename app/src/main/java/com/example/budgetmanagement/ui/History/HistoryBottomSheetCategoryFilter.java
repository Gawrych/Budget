package com.example.budgetmanagement.ui.History;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryAdapter;
import com.example.budgetmanagement.database.Adapters.HistoryBottomSheetAdapter;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Objects;

public class HistoryBottomSheetCategoryFilter implements CategoryViewHolder.OnNoteListener {

    private BottomSheetDialog bottomSheetDialog;
    private final HistoryViewModel historyViewModel;
    private LiveData<List<HistoryBottomSheetEntity>> historyBottomSheetEntity;
    private int selectedCategory;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryBottomSheetCategoryFilter(Context context, BottomSheetDialog bottomSheet, ViewModelStoreOwner owner, LifecycleOwner lifeCycleOwner, LiveData<List<HistoryAndTransaction>> list, HistoryAdapter TheseAdapter) {
        bottomSheetDialog = bottomSheet;
        final HistoryBottomSheetAdapter historyBottomSheetAdapter = new HistoryBottomSheetAdapter(new HistoryBottomSheetAdapter.HistoryBottomSheetEntityDiff(), this::onNoteClick);
        historyViewModel = new ViewModelProvider(owner).get(HistoryViewModel.class);
        historyBottomSheetEntity = historyViewModel.getHistoryBottomSheetEntity();
        historyBottomSheetEntity.observe(lifeCycleOwner, historyBottomSheetAdapter::submitList);
        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(historyBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void show() {
        bottomSheetDialog.show();
    }

    @Override
    public void onNoteClick(int position) {
        selectedCategory = Objects.requireNonNull(historyBottomSheetEntity.getValue()).get(position).getId();
        bottomSheetDialog.cancel();
    }

    public int getSelectedCategoryId() {
        return selectedCategory;
    }
}
