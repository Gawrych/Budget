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
import androidx.lifecycle.MediatorLiveData;
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
import java.util.Map;
import java.util.Objects;

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;
    private CategoryViewModel categoryViewModel;

    private HistoryBottomSheetDetails historyBottomSheetDetails;
    private HistoryAdapter adapter;
    private int value = 0;
    private SortingMarkIconManager sortingMarkIconManager;
    private FilterViewModel filterViewModel;
    private List<HistoryAndTransaction> currentList;
    private LiveData<List<HistoryAndTransaction>> originalList;
    private List<HistoryAndTransaction> listToCheck;
    private LiveData<List<HistoryAndTransaction>> filteredList;

    private MediatorLiveData<List<HistoryAndTransaction>> mediator = new MediatorLiveData<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ErrorCheck", "OnCreate");
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        historyAndTransactionList = historyViewModel.getAllHistoryAndTransactionInDateOrder();
        currentList = historyViewModel.getAllHistoryAndTransactionInDateOrderList();

        filterViewModel.setFilters(new HashMap<>());

        originalList = historyViewModel.getAllHistoryAndTransactionInDateOrder();
        filteredList = filterViewModel.getFilteredList();

        mediator.addSource(originalList, mediator::setValue);
        mediator.addSource(filteredList, mediator::setValue);

        historyViewModel.getAllHistoryAndTransactionInDateOrder().observe(getViewLifecycleOwner(), s -> {
            setListToCheck(s);
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("ErrorCheck", "onCreateView");
        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("ErrorCheck", "onViewCreated");
        RecyclerView recyclerView;

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new HistoryAdapter(new HistoryAdapter.HistoryAndTransactionDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        filterViewModel.setOriginalList(historyViewModel.getAllHistoryAndTransactionInDateOrderList());

        historyViewModel.getAllHistoryAndTransactionInDateOrder().observe(getViewLifecycleOwner(), s -> {
            if (listToCheck != s) {
                setListToCheck(s);
                adapter.submitList(s);
                filterViewModel.setFilters(new HashMap<>());
                Log.d("ErrorCheck", "resetFiltersInObserver");
            }
        });

        if (isFiltersSet()) {
            filteredList.observe(getViewLifecycleOwner(), adapter::submitList);
            Log.d("ErrorCheck", "isFiltersSet");
        } else {
            originalList.observe(getViewLifecycleOwner(), adapter::submitList);
            Log.d("ErrorCheck", "isntFiltersSet");
        }

//        mediator.observe(getViewLifecycleOwner(), adapter::submitList);


        historyBottomSheetDetails =
                new HistoryBottomSheetDetails(getContext(), getActivity(), historyViewModel);

        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(root -> Navigation.
                findNavController(root).navigate(R.id.action_navigation_history_to_addNewHistory));

        ImageButton categoryFilter = view.findViewById(R.id.categoryFilterButton);
//        categoryFilter.setOnClickListener(root -> fragmentTransaction.commit());

        ImageButton orderFilter = view.findViewById(R.id.orderFilterButton);
        orderFilter.setOnClickListener(root -> Navigation.findNavController(root)
                .navigate(R.id.action_navigation_history_to_filterFragment));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        List<Category> categoryList = categoryViewModel.getCategoryList();

        sortingMarkIconManager = new SortingMarkIconManager(view, categoryList);
        setSortingMarkIcons(filterViewModel.getFilters());
    }

    private void setListToCheck(List<HistoryAndTransaction> list) {
        listToCheck = list;
    }

    private boolean isFiltersSet() {
        HashMap<Integer, Integer> map = filterViewModel.getFilters();
        for (Map.Entry<Integer, Integer> filter : map.entrySet()) {
            if (filter.getValue() != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
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