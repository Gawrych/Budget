package com.example.budgetmanagement.ui.History;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;

    private HistoryBottomSheetDetails historyBottomSheetDetails;
    private HistoryAdapter adapter;
    private SortingMarkIconManager sortingMarkIconManager;
    private FilterViewModel filterViewModel;
    private List<HistoryAndTransaction> currentList;

    private final MediatorLiveData<List<HistoryAndTransaction>> mediator = new MediatorLiveData<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        filterViewModel.resetFilters();

        LiveData<List<HistoryAndTransaction>> originalList = historyViewModel.getAllHistoryAndTransactionInDateOrder();
        LiveData<List<HistoryAndTransaction>> filteredList = filterViewModel.getFilteredList();
        mediator.addSource(filteredList, mediator::setValue);
        mediator.addSource(originalList, mediator::setValue);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("ErrorCheck", "onCreateView");
        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createAdapter();
        initializeRecyclerView(view);
        
        historyViewModel.getAllHistoryAndTransactionInDateOrder().observe(getViewLifecycleOwner(), list -> {
            filterViewModel.setOriginalList(list);
            currentList = list;
        });

        mediator.observe(getViewLifecycleOwner(), adapter::submitList);

        historyBottomSheetDetails =
                new HistoryBottomSheetDetails(getContext(), getActivity(), historyViewModel);

        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(root -> {
            removeFilters();
            Navigation
                    .findNavController(root)
                    .navigate(R.id.action_navigation_history_to_addNewHistory);
        });

        ImageButton orderFilter = view.findViewById(R.id.orderFilterButton);
        orderFilter.setOnClickListener(root -> {
            Navigation
                    .findNavController(root)
                    .navigate(R.id.action_navigation_history_to_filterFragment);
        });

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        List<Category> categoryList = categoryViewModel.getCategoryList();

        sortingMarkIconManager = new SortingMarkIconManager(view, categoryList);
        setSortingMarkIcons(filterViewModel.getFilters());
    }

    private void createAdapter() {
        adapter = new HistoryAdapter(new HistoryAdapter.HistoryAndTransactionDiff(), this);
    }

    private void initializeRecyclerView(@NonNull View view) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void removeFilters() {
        changeListToBeforeAddFilters();
        clearSortingMarkIcons();
        resetFiltersInFilterViewModel();
    }

    private void changeListToBeforeAddFilters() {
        mediator.setValue(currentList);
    }

    private void clearSortingMarkIcons() {
        sortingMarkIconManager.removeAllMarkIcons();
    }

    private void setSortingMarkIcons(HashMap<Integer, Integer> filters) {
        sortingMarkIconManager.setMarkIcons(filters);
    }

    private void resetFiltersInFilterViewModel() {
        filterViewModel.resetFilters();
    }

    @Override
    public void onNoteClick(int position) {
        HistoryAndTransaction historyAndTransaction = adapter.getCurrentList().get(position);
        historyBottomSheetDetails.setData(historyAndTransaction);
        historyBottomSheetDetails.show();
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }
}