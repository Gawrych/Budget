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
import androidx.navigation.Navigation;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;
    private LiveData<List<HistoryAndTransaction>> currentLiveDataHistoryAndTransactionList;
    private HistoryBottomSheetCategoryFilter historyBottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog;
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


    ActivityResultLauncher<Intent> startActivityForResultFilter =
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

        currentLiveDataHistoryAndTransactionList = historyViewModel.getAllHistoryAndTransactionInAmountOrder();
        currentLiveDataHistoryAndTransactionList.observe(getViewLifecycleOwner(), adapter::submitList);

        ImageButton addButton = root.findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddNewHistory.class);
            startActivityForResult.launch(intent);
        });

        ImageButton categoryFilter = root.findViewById(R.id.categoryFilterButton);
        categoryFilter.setOnClickListener(view -> {
            if (Objects.isNull(historyBottomSheetDialog)) {
                bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(R.layout.history_bottom_sheet_dialog);
                historyBottomSheetDialog = new HistoryBottomSheetCategoryFilter(bottomSheetDialog, historyViewModel, getViewLifecycleOwner());
            }
            historyBottomSheetDialog.show();

            bottomSheetDialog.setOnDismissListener(dialogInterface -> {
                getTransactionAndHistoryFromCategory();
            });
        });

        ImageButton orderFilter = root.findViewById(R.id.orderFilterButton);
        orderFilter.setOnClickListener(view -> {
//            currentLiveDataHistoryAndTransactionList = Transformations.map(currentLiveDataHistoryAndTransactionList,
//                    input -> input.stream().sorted(Comparator.comparingDouble(o -> o.transaction.getAmount()))
//                            .collect(Collectors.toList()));
//            currentLiveDataHistoryAndTransactionList.observe(getViewLifecycleOwner(), adapter::submitList);



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
        HistoryAndTransaction historyAndTransaction = currentLiveDataHistoryAndTransactionList.getValue().get(position);
        Toast.makeText(getContext(), String.valueOf(historyAndTransaction.transaction.getCategoryId()), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getTransactionAndHistoryFromCategory() {
        int categoryId = historyBottomSheetDialog.getSelectedCategoryId();
        if (categoryId > 0) {
            currentLiveDataHistoryAndTransactionList = historyViewModel.getAllHistoryAndTransactionByCategory(categoryId);
            currentLiveDataHistoryAndTransactionList.observe(getViewLifecycleOwner(), adapter::submitList);
        }
    }
}