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

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;
    private LiveData<List<HistoryAndTransaction>> liveDataHistoryAndTransaction;

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
        final HistoryAdapter adapter = new HistoryAdapter(new HistoryAdapter.HistoryAndTransactionDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        liveDataHistoryAndTransaction = historyViewModel.getAllHistoryAndTransactionInAmountOrder();
        liveDataHistoryAndTransaction.observe(getViewLifecycleOwner(), adapter::submitList);

        ImageButton addButton = root.findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddNewHistory.class);
            startActivityForResult.launch(intent);
        });

        ImageButton orderFilter = root.findViewById(R.id.categoryFilterButton);
        orderFilter.setOnClickListener(view -> {
            LiveData<List<HistoryAndTransaction>> liveDataHistoryAndTransactionByCategory = historyViewModel.getAllHistoryAndTransactionByCategory(1);
            liveDataHistoryAndTransactionByCategory.observe(getViewLifecycleOwner(), adapter::submitList);
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
//        ((MainActivity) requireActivity()).turnOnProgressBar();
//        Intent intent = new Intent(getActivity(), AddNewCategoryElement.class);
//        startActivityForResult.launch(intent);
        HistoryAndTransaction historyAndTransaction = liveDataHistoryAndTransaction.getValue().get(position);
        Toast.makeText(getContext(), String.valueOf(historyAndTransaction.transaction.getCategoryId()), Toast.LENGTH_SHORT).show();
    }
}