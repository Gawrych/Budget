package com.example.budgetmanagement.ui.History;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryBottomSheetAdapter;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class HistoryBottomSheetOrder extends Fragment implements CategoryViewHolder.OnNoteListener {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;
    MutableLiveData<List<HistoryBottomSheetEntity>> historyBottomSheetEntity;
//    private LiveData<List<HistoryAndTransaction>> sortedList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryBottomSheetOrder(BottomSheetDialog bottomSheetDialog,
                                   LiveData<List<HistoryAndTransaction>> historyAndTransactionList, LifecycleOwner lifeCycleOwner) {
        this.bottomSheetDialog = bottomSheetDialog;
        this.historyAndTransactionList = historyAndTransactionList;

        final HistoryBottomSheetAdapter historyBottomSheetAdapter = new HistoryBottomSheetAdapter(
                new HistoryBottomSheetAdapter.HistoryBottomSheetEntityDiff(), this::onNoteClick);

        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(historyBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<HistoryBottomSheetEntity> historyBottomSheetEntityList = new ArrayList<>();
        historyBottomSheetEntityList.add(new HistoryBottomSheetEntity(1, "amount", "Po Cenie"));
        historyBottomSheetEntityList.add(new HistoryBottomSheetEntity(2, "name", "Alfabetycznie"));
        historyBottomSheetEntityList.add(new HistoryBottomSheetEntity(3, "date", "Po Dacie"));

        historyBottomSheetEntity = new MutableLiveData<>();
        historyBottomSheetEntity.setValue(historyBottomSheetEntityList);

        historyBottomSheetEntity.observe(lifeCycleOwner, historyBottomSheetAdapter::submitList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void orderByAmount() {
        historyAndTransactionList = Transformations.map(historyAndTransactionList,
                input -> input.stream().sorted(Comparator.comparingDouble(o ->
                        o.transaction.getAmount())).collect(Collectors.toList()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onNoteClick(int position) {
        int selectedId = Objects.requireNonNull(historyBottomSheetEntity.getValue()).get(position).getId();
        if (selectedId == 0) {
            orderByAmount();
        }
        bottomSheetDialog.cancel();
    }
}
