package com.example.budgetmanagement.ui.History;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
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
    private List<HistoryBottomSheetEntity> historyBottomSheetEntityList;
    private int selectedId;
    private String selectedName;

    public HistoryBottomSheetCategoryFilter(Context context, Fragment parentFragment) {
        HistoryViewModel historyViewModel = new ViewModelProvider(parentFragment).get(HistoryViewModel.class);
        historyBottomSheetEntity = historyViewModel.getHistoryBottomSheetEntity();

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.history_bottom_sheet_dialog);

        final HistoryBottomSheetAdapter historyBottomSheetAdapter = new HistoryBottomSheetAdapter(new HistoryBottomSheetAdapter.HistoryBottomSheetEntityDiff(), this::onNoteClick);

        historyBottomSheetEntityList = historyBottomSheetEntity.getValue();
        historyBottomSheetEntity.observe(parentFragment.getViewLifecycleOwner(), historyBottomSheetAdapter::submitList);

        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(historyBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void show() {
        bottomSheetDialog.show();
        resetSelectedId();
        resetSelectedName();
    }

    @Override
    public void onNoteClick(int position) {
        List<HistoryBottomSheetEntity> listOfEntity = historyBottomSheetEntity.getValue();
        if (listOfEntity != null) {
            this.selectedId = listOfEntity.get(position).getId();
            this.selectedName = listOfEntity.get(position).getName();
        }
        bottomSheetDialog.cancel();
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return this.bottomSheetDialog;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public String getCategoryNameById(Integer id) {
        if (historyBottomSheetEntityList != null && id != null) {
            for (HistoryBottomSheetEntity element : historyBottomSheetEntityList) {
                if (element.getId() == id) {
                    return element.getName();
                }
            }
        }
        return null;
    }

    public void resetSelectedName() {
        this.selectedName = null;
    }

    public void resetSelectedId() {
        this.selectedId = 0;
    }

}
