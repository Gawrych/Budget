package com.example.budgetmanagement.ui.History;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryAdapter;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.HistoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.databinding.HistoryFragmentBinding;

import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;
    private LiveData<List<HistoryAndTransaction>> currentLiveDataHistoryAndTransactionList;
    private HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter;
    private HistoryBottomSheetOrder historyBottomSheetOrder;

    private HistoryAdapter adapter;

    ActivityResultLauncher<Intent> startActivityForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {

                            } else {
                                Log.println(Log.ERROR, "NULL", "Null as request from 'AddNewTransactionElement' class");
                            }
                        }
                    });

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        RecyclerView recyclerView;

        root =  inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        adapter = new HistoryAdapter(new HistoryAdapter.HistoryAndTransactionDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        currentLiveDataHistoryAndTransactionList = historyViewModel.getAllHistoryAndTransactionInDateOrder();
        currentLiveDataHistoryAndTransactionList.observe(getViewLifecycleOwner(), adapter::submitList);

        ImageButton addButton = root.findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddNewHistory.class);
            startActivityForResult.launch(intent);
        });

        ImageButton categoryFilter = root.findViewById(R.id.categoryFilterButton);
        categoryFilter.setOnClickListener(view -> {
            showBottomSheetToFilterByCategory();

        });

        ImageButton orderFilter = root.findViewById(R.id.orderFilterButton);
        orderFilter.setOnClickListener(view -> {
            showBottomSheetToSortList();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onNoteClick(int position) {
        HistoryAndTransaction historyAndTransaction = Objects.requireNonNull(currentLiveDataHistoryAndTransactionList.getValue()).get(position);
        Toast.makeText(getContext(), String.valueOf(historyAndTransaction.transaction.getCategoryId()), Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBottomSheetToFilterByCategory() {
        if (Objects.isNull(historyBottomSheetCategoryFilter)) {
            historyBottomSheetCategoryFilter = new HistoryBottomSheetCategoryFilter(getContext(), historyViewModel, getViewLifecycleOwner());
        }
        historyBottomSheetCategoryFilter.show();
        historyBottomSheetCategoryFilter.getBottomSheetDialog().setOnDismissListener(dialogInterface -> {
            int categoryId = historyBottomSheetCategoryFilter.getSelectedCategoryId();
            if (categoryId > 0) {
                currentLiveDataHistoryAndTransactionList = historyViewModel.getAllHistoryAndTransactionByCategory(categoryId);
                currentLiveDataHistoryAndTransactionList.observe(getViewLifecycleOwner(), adapter::submitList);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBottomSheetToSortList() {
        if (Objects.isNull(historyBottomSheetOrder)) {
            historyBottomSheetOrder = new HistoryBottomSheetOrder(getContext());
        }
        historyBottomSheetOrder.setListToSort(currentLiveDataHistoryAndTransactionList);
        historyBottomSheetOrder.show();
        historyBottomSheetOrder.getBottomSheetDialog().setOnDismissListener(dialogInterface ->
                historyBottomSheetOrder.getSortedList().observe(getViewLifecycleOwner(), adapter::submitList));
    }
}