package com.example.budgetmanagement.ui.Coming;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingExpandableListAdapter;
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


public class ComingFragment extends Fragment {

    private ComingFragmentBinding binding;
    private View view;
    private List<ComingAndTransaction> globalList;
    private ArrayList<Section> sectionList = new ArrayList<>();
    private final HashMap<Short, ArrayList<Section>> sectionListsByYears = new HashMap<>();
    private final HashMap<Integer, ArrayList<ComingAndTransaction>> transactionsCollection = new HashMap<>();
    private final Calendar calendar = Calendar.getInstance();
    private ComingBottomSheetDetails details;
    private ComingViewModel comingViewModel;
    private ExpandableListView expandableListView;
    private ComingExpandableListAdapter expandableListAdapter;
    private short year = 2022;
    private long startYear = 0;
    private long endYear = 0;


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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coming_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        setYearStartAndEnd();

        expandableListView = view.findViewById(R.id.expandableListView);
        details = new ComingBottomSheetDetails(requireContext(), getActivity(), this);

        setSections(comingViewModel.getComingAndTransactionByYear(startYear, endYear));

        expandableListAdapter = new ComingExpandableListAdapter(requireContext(), sectionList);
        expandableListView.setAdapter(expandableListAdapter);

        comingViewModel.getComingAndTransactionByYearLiveData(startYear, endYear).observe(getViewLifecycleOwner(), list -> {
            sectionList.clear();
            setSections(list);
            expandableListAdapter.updateItems(sectionList);
            expandableListAdapter.notifyAdapter(expandableListView);
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ComingAndTransaction comingAndTransaction = expandableListAdapter.getChild(groupPosition, childPosition);
            details.setData(comingAndTransaction);
            details.show();
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setYearStartAndEnd() {
        Calendar c = Calendar.getInstance();
        getLastMillisOfYear(c);
        endYear = c.getTimeInMillis();
        getFirstMillisOfYear(c);
        startYear = c.getTimeInMillis();
    }

    private void getLastMillisOfYear(Calendar c) {
        c.set(Calendar.YEAR, year);
        c.add(Calendar.DAY_OF_YEAR, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
    }

    private void getFirstMillisOfYear(Calendar lastMillisOfYear) {
        lastMillisOfYear.add(Calendar.MILLISECOND, 1);
        lastMillisOfYear.add(Calendar.YEAR, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSections(List<ComingAndTransaction> list) {
        sectionList.clear();
        collectTransactionByMonthId(list);
        months.forEach((name, id) -> sectionList.add(new Section(getStringResId(name), transactionsCollection.get(id))));
    }

    private int getStringResId(String stringName) {
        return getResources().getIdentifier(stringName, "string", view.getContext().getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void collectTransactionByMonthId(List<ComingAndTransaction> list) {
        initializeEmptyTransactionsCollection();

        globalList = list;
        globalList.forEach(item -> {
            int monthNumber = getMonthNumberFromDate(item.coming.getRepeatDate());

            ArrayList<ComingAndTransaction> actualList = transactionsCollection.get(monthNumber);
            if (actualList == null) {
                Log.e("ErrorHandle", "com/example/budgetmanagement/ui/Coming/ComingFragment.java NullPointerException:'actualList' is null when 'month'=" + monthNumber);
                return;
            }
            actualList.add(item);
            transactionsCollection.put(monthNumber, actualList);
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}