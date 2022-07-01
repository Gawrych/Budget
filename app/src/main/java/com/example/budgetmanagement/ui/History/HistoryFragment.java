package com.example.budgetmanagement.ui.History;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryAdapter;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.HistoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.databinding.HistoryFragmentBinding;
import com.example.budgetmanagement.ui.utils.SortingMarkIconManager;

import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment implements HistoryViewHolder.OnNoteListener {

    private HistoryViewModel historyViewModel;
    private HistoryFragmentBinding binding;
    private LiveData<List<HistoryAndTransaction>> currentLiveDataHistoryAndTransaction;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionListToViewHolder;

    private HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter;
    private HistoryBottomSheetSorting historyBottomSheetSorting;
    private HistoryBottomSheetDetails historyBottomSheetDetails;
    private HistoryAdapter adapter;
    private View root;
    private SortingMarkIconManager sortingMarkIconManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;

        root =  inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        adapter = new HistoryAdapter(new HistoryAdapter.HistoryAndTransactionDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        currentLiveDataHistoryAndTransaction = historyViewModel.getAllHistoryAndTransactionInDateOrder();
        historyAndTransactionListToViewHolder = currentLiveDataHistoryAndTransaction;
        historyAndTransactionListToViewHolder.observe(getViewLifecycleOwner(), adapter::submitList);

        historyBottomSheetDetails = new HistoryBottomSheetDetails(getContext(), getActivity(), historyViewModel);

        ImageButton addButton = root.findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigation_history_to_addNewHistory));

        ImageButton categoryFilter = root.findViewById(R.id.categoryFilterButton);
        categoryFilter.setOnClickListener(view -> showBottomSheetToFilterByCategory());

        ImageButton orderFilter = root.findViewById(R.id.orderFilterButton);
        orderFilter.setOnClickListener(view -> showBottomSheetToSortList());

        sortingMarkIconManager = new SortingMarkIconManager();
        sortingMarkIconManager.setView(root);
        sortingMarkIconManager.prepareSortingIcons();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNoteClick(int position) {
        HistoryAndTransaction historyAndTransaction = Objects.requireNonNull(historyAndTransactionListToViewHolder.getValue()).get(position);
        historyBottomSheetDetails.setData(historyAndTransaction);
        historyBottomSheetDetails.show();
    }


//   TODO: Change filter by category to filter by sorting sheet
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBottomSheetToFilterByCategory() {
        if (Objects.isNull(historyBottomSheetCategoryFilter)) {
            historyBottomSheetCategoryFilter = new HistoryBottomSheetCategoryFilter(getContext(), getViewLifecycleOwner(), historyViewModel);
        }
        historyBottomSheetCategoryFilter.show();
        historyBottomSheetCategoryFilter.getBottomSheetDialog().setOnDismissListener(dialogInterface -> {
            int categoryId = historyBottomSheetCategoryFilter.getSelectedId();
            if (categoryId > 0) {
                currentLiveDataHistoryAndTransaction = historyViewModel.getAllHistoryAndTransactionByCategory(categoryId);
                historyAndTransactionListToViewHolder = currentLiveDataHistoryAndTransaction;
                historyAndTransactionListToViewHolder.observe(getViewLifecycleOwner(), adapter::submitList);
                sortingMarkIconManager.showCategoryIcon(historyBottomSheetCategoryFilter.getSelectedIconName());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBottomSheetToSortList() {
        if (Objects.isNull(historyBottomSheetSorting)) {
            historyBottomSheetSorting = new HistoryBottomSheetSorting(getContext());
        }
        historyBottomSheetSorting.setListToSort(currentLiveDataHistoryAndTransaction);
        historyBottomSheetSorting.show();
        historyBottomSheetSorting.getBottomSheetDialog().setOnDismissListener(dialogInterface -> {
            historyAndTransactionListToViewHolder = historyBottomSheetSorting.getSortedList();
            historyAndTransactionListToViewHolder.observe(getViewLifecycleOwner(), adapter::submitList);
            sortingMarkIconManager.showSortIcon(historyBottomSheetSorting.getIconResourceId());
            sortingMarkIconManager.showProfitIcon(historyBottomSheetSorting.getProfitIconResourceId());
            sortingMarkIconManager.showReverseIcon(historyBottomSheetSorting.checkIfCheckboxIsChecked());
        });
    }
}