package com.example.budgetmanagement.ui.History;

import android.content.Context;

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
    int position;

    public HistoryBottomSheetCategoryFilter(Context context, LifecycleOwner lifeCycleOwner, HistoryViewModel historyViewModel) {
        historyBottomSheetEntity = historyViewModel.getHistoryBottomSheetEntity();
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.history_bottom_sheet_dialog);

        final HistoryBottomSheetAdapter historyBottomSheetAdapter = new HistoryBottomSheetAdapter(new HistoryBottomSheetAdapter.HistoryBottomSheetEntityDiff(), this::onNoteClick);

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
        this.position = position;
        bottomSheetDialog.cancel();
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return this.bottomSheetDialog;
    }

    public int getSelectedId() {
        return Objects.requireNonNull(historyBottomSheetEntity.getValue()).get(position).getId();
    }

    public String getSelectedName() {
        return Objects.requireNonNull(historyBottomSheetEntity.getValue()).get(position).getName();
    }
}
