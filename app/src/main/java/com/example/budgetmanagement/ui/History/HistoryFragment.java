package com.example.budgetmanagement.ui.History;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryAdapter;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.HistoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.database.ViewModels.FilterViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.databinding.HistoryFragmentBinding;
import com.example.budgetmanagement.ui.utils.SortingMarkIconManager;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;
    private CategoryViewModel categoryViewModel;

    private HistoryBottomSheetDetails historyBottomSheetDetails;
    private HistoryAdapter adapter;
    private View root;
    private int value = 0;
    private SortingMarkIconManager sortingMarkIconManager;
    private FilterViewModel filterViewModel;
    private List<HistoryAndTransaction> currentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ErrorCheck", "OnCreate");
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        historyAndTransactionList = historyViewModel.getAllHistoryAndTransactionInDateOrder();
        currentList = historyViewModel.getAllHistoryAndTransactionInDateOrderList();
        filterViewModel.setFilteredList(currentList);
        filterViewModel.setOriginalList(currentList);
        filterViewModel.setFilters(new HashMap<>());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;

        root =  inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        adapter = new HistoryAdapter(new HistoryAdapter.HistoryAndTransactionDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        filterViewModel.getFilteredList().observe(getViewLifecycleOwner(), adapter::submitList);

        historyBottomSheetDetails =
                new HistoryBottomSheetDetails(getContext(), getActivity(), historyViewModel);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        List<Category> categoryList = categoryViewModel.getCategoryList();

        ImageButton addButton = root.findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> Navigation.
                findNavController(view).navigate(R.id.action_navigation_history_to_addNewHistory));

        ImageButton categoryFilter = root.findViewById(R.id.categoryFilterButton);
//        categoryFilter.setOnClickListener(view -> fragmentTransaction.commit());

        ImageButton orderFilter = root.findViewById(R.id.orderFilterButton);
        orderFilter.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_navigation_history_to_filterFragment));

        sortingMarkIconManager = new SortingMarkIconManager(root, categoryList);
        setSortingMarkIcons(filterViewModel.getFilters());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSortingMarkIcons(HashMap<Integer, Integer> filters) {
        sortingMarkIconManager.setMarkIcons(filters);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNoteClick(int position) {
        HistoryAndTransaction historyAndTransaction = Objects.requireNonNull(historyAndTransactionList.getValue()).get(position);
        historyBottomSheetDetails.setData(historyAndTransaction);
        historyBottomSheetDetails.show();
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }
}