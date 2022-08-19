package com.example.budgetmanagement.ui.Coming;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private ComingFragmentBinding binding;
    private View view;
    private ComingAdapter adapter;
    private List<ComingAndTransaction> globalList;
    private final ArrayList<Section> sectionList = new ArrayList<>();
    private final HashMap<Integer, ArrayList<ComingAndTransaction>> transactionsCollection = new HashMap<>();
    private final Calendar calendar = Calendar.getInstance();
    private LiveData<List<ComingAndTransaction>> allComingTransaction;
    private ComingBottomSheetDetails details;
    private ComingViewModel comingViewModel;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        allComingTransaction = comingViewModel.getAllComingAndTransaction();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        details = new ComingBottomSheetDetails(requireContext(), getActivity(), comingViewModel);

        allComingTransaction.observe(getViewLifecycleOwner(), list -> {
            setSections(list);
            adapter.submitList(sectionList);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSections(List<ComingAndTransaction> list) {
        globalList = list;
        sectionList.clear();
//     TODO:Remove all transaction from globalList in updatedList, to left only new transaction
        collectTransactionByMonthId();
        months.forEach((name, id) -> sectionList.add(new Section(getStringResId(name), transactionsCollection.get(id))));
    }

    private int getStringResId(String stringName) {
        return getResources().getIdentifier(stringName, "string", view.getContext().getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void collectTransactionByMonthId() {
        initializeEmptyTransactionsCollection();

        globalList.forEach(item -> {
            int monthNumber = getMonthNumberFromDate(item.coming.getRepeatDate());
            int year = getYearFromDate(item.coming.getRepeatDate());

//            if (year != getYearFromDate(getTodayDateInMillis())) {
////                TODO: Implement selecting year
//            } else {
                ArrayList<ComingAndTransaction> actualList = transactionsCollection.get(monthNumber);
                if (actualList == null) {
                    Log.e("ErrorHandle", "com/example/budgetmanagement/ui/Coming/ComingFragment.java NullPointerException:'actualList' is null when 'month'=" + monthNumber);
                    return;
                }
                actualList.add(item);
                transactionsCollection.put(monthNumber, actualList);
//            }
        });
    }

    private long getTodayDateInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private int getMonthNumberFromDate(long dateInMillis) {
        this.calendar.setTimeInMillis(dateInMillis);
        return this.calendar.get(Calendar.MONTH);
    }

    private int getYearFromDate(long dateInMillis) {
        this.calendar.setTimeInMillis(dateInMillis);
        return this.calendar.get(Calendar.YEAR);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initializeEmptyTransactionsCollection() {
        months.forEach((name, id) -> transactionsCollection.put(id, new ArrayList<>()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int parentPosition, int childPosition) {
        ComingAndTransaction coming = adapter.getCurrentList().get(parentPosition).getComingAndTransactionList().get(childPosition);
        details.setData(coming);
        details.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}