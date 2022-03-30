package com.example.budgetmanagement.ui.History;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryBottomSheetAdapter;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Objects;

public class HistoryBottomSheetCategoryFilter extends Fragment implements CategoryViewHolder.OnNoteListener {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<HistoryBottomSheetEntity>> historyBottomSheetEntity;
    private int selectedId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryBottomSheetCategoryFilter(BottomSheetDialog bottomSheetDialog, HistoryViewModel historyViewModel, LifecycleOwner lifeCycleOwner) {
        this.bottomSheetDialog = bottomSheetDialog;
        final HistoryBottomSheetAdapter historyBottomSheetAdapter = new HistoryBottomSheetAdapter(new HistoryBottomSheetAdapter.HistoryBottomSheetEntityDiff(), this::onNoteClick);

        historyBottomSheetEntity = historyViewModel.getHistoryBottomSheetEntity();
        historyBottomSheetEntity.observe(lifeCycleOwner, historyBottomSheetAdapter::submitList);

        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(historyBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void show() {
        bottomSheetDialog.show();
    }

    @Override
    public void onNoteClick(int position) {
        selectedId = Objects.requireNonNull(historyBottomSheetEntity.getValue()).get(position).getId();
        bottomSheetDialog.cancel();
    }

    public int getSelectedCategoryId() {
        return selectedId;
    }
}
