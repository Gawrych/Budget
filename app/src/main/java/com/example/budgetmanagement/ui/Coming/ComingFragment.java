package com.example.budgetmanagement.ui.Coming;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ComingFragment extends Fragment implements ParentOnNoteListener {

    private ComingViewModel comingViewModel;
    private ComingFragmentBinding binding;
    private View view;
    private ComingAdapter adapter;
    private List<ComingAndTransaction> globalList;
    private final List<Section> sectionList = new ArrayList<>();
    private final HashMap<Integer, ArrayList<ComingAndTransaction>> transactionCollectByMonthsId = new HashMap<>();

    public static final Map<String, Integer> months;
    static {
        Map<String, Integer> items = new LinkedHashMap<>();
        items.put("january", 0);
        items.put("february", 1);
        items.put("march", 2);
        items.put("april", 3);
        items.put("may", 4);
        items.put("june", 5);
        items.put("july", 6);
        items.put("august", 7);
        items.put("september", 8);
        items.put("october", 9);
        items.put("november", 10);
        items.put("december", 11);
        months = Collections.unmodifiableMap(items);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coming_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        adapter = new ComingAdapter(new ComingAdapter.ComingDiff(), this, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        LiveData<List<ComingAndTransaction>> coming = comingViewModel.getAllComingAndTransaction();
        coming.observe(getViewLifecycleOwner(), list -> {
            setSections(list);
            adapter.submitList(sectionList);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSections(List<ComingAndTransaction> list) {
        globalList = list;
//        Remove all transaction from globalList in updatedList, to left only new transaction
        collectTransactionByMonthId();
        months.forEach((name, id) -> sectionList.add(new Section(getStringResId(name), transactionCollectByMonthsId.get(id))));
    }

    private int getStringResId(String stringName) {
        return getResources().getIdentifier(stringName, "string", view.getContext().getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void collectTransactionByMonthId() {
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 12; i++) {
            transactionCollectByMonthsId.put(i, new ArrayList<>());
        }

        globalList.forEach(comingAndTransactionItem -> {
            calendar.setTimeInMillis(comingAndTransactionItem.coming.getRepeatDate());
            int month = calendar.get(Calendar.MONTH);

            ArrayList<ComingAndTransaction> actualList = transactionCollectByMonthsId.get(month);
            if (actualList == null) {
                Log.e("ErrorHandle", "com/example/budgetmanagement/ui/Coming/ComingFragment.java NullPointerException:'actualList' is null when 'month'=" + month);
                return;
            }
            actualList.add(comingAndTransactionItem);
            transactionCollectByMonthsId.put(month, actualList);
        });
    }

    @Override
    public void onItemClick(int parentPosition, int childPosition) {
        ComingAndTransaction coming = adapter.getCurrentList().get(parentPosition).getComingAndTransactionList().get(childPosition);
        Toast.makeText(requireContext(), "Title: " + coming.transaction.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}