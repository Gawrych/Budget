package com.example.budgetmanagement.ui.Coming;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingExpandableListAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;
import com.example.budgetmanagement.ui.Category.AppIconPack;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComingFragment extends Fragment {

    private ComingFragmentBinding binding;
    private View view;
    private List<ComingAndTransaction> globalList;
    private ArrayList<Section> sectionList = new ArrayList<>();
    private final HashMap<Integer, ArrayList<ComingAndTransaction>> transactionsCollection = new HashMap<>();
    private ComingBottomSheetDetails details;
    private ComingViewModel comingViewModel;
    private ExpandableListView expandableListView;
    private ComingExpandableListAdapter expandableListAdapter;
    private int year = 2022;
    private long startYear = 0;
    private long endYear = 0;
    private HashMap<Integer, ArrayList<Section>> savedLists = new HashMap<>();
    private DatePickerDialog datePickerDialog;
    private TextView pickedYear;
    private List<ComingAndTransaction> actualList;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coming_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        setYearStartAndEnd();

        ImageButton yearPicker = view.findViewById(R.id.yearSelector);
        pickedYear = view.findViewById(R.id.pickedYear);
        pickedYear.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();

        expandableListView = view.findViewById(R.id.expandableListView);
        details = new ComingBottomSheetDetails(requireContext(), iconPack, this, view);

        expandableListAdapter = new ComingExpandableListAdapter(requireContext(), sectionList, this, iconPack);
        expandableListView.setAdapter(expandableListAdapter);

        comingViewModel.getAllComingAndTransaction().observe(getViewLifecycleOwner(), list -> {
            setSections(list, true);
//            int actualPositionToScroll = getActualPositionToScroll();
            // TODO put this to callAfterLoadList();
//            expandableListView.smoothScrollToPositionFromTop(actualPositionToScroll, 0);
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ComingAndTransaction comingAndTransaction = expandableListAdapter.getChild(groupPosition, childPosition);
            details.setData(comingAndTransaction);
            details.show();
            return true;
        });

        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(root -> {
            Navigation.findNavController(root)
                    .navigate(R.id.action_navigation_incoming_to_addNewComingElement);
        });

        ImageButton categories = view.findViewById(R.id.categoriesButton);
        categories.setOnClickListener(root -> {
            Navigation.findNavController(root)
                    .navigate(R.id.action_navigation_incoming_to_categoryList);
        });

        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> true);
        yearPicker.setOnClickListener(v -> selectYear());
    }

    private void selectYear() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
         datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {}, this.year, mMonth, mDay);

        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().getTouchables().get(1).setVisibility(View.GONE);
        datePickerDialog.getDatePicker()
                .setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                    this.year = year;
                    pickedYear.setText(String.valueOf(year));
                    setYearStartAndEnd();
                    setSections(actualList, false);
                    datePickerDialog.cancel();
        });
        datePickerDialog.show();
    }

    private int getActualPositionToScroll() {
        int monthNumber = getMonthNumberFromDate(Calendar.getInstance().getTimeInMillis());
        int endPosition = monthNumber;
        for (int i=0; i<=monthNumber; i++) {
            endPosition = endPosition + sectionList.get(i).getComingAndTransactionList().size();
        }
        return endPosition;
    }

    private void setYearStartAndEnd() {
        Calendar c = Calendar.getInstance();
        getLastMillisOfYear(c);
        endYear = c.getTimeInMillis();
        getFirstMillisOfYear(c);
        startYear = c.getTimeInMillis();
    }

    private void getLastMillisOfYear(Calendar c) {
        c.set(Calendar.YEAR, this.year);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
    }

    private void getFirstMillisOfYear(Calendar lastMillisOfYear) {
        lastMillisOfYear.add(Calendar.MILLISECOND, 1);
        lastMillisOfYear.add(Calendar.YEAR, -1);
    }

    private void setSections(List<ComingAndTransaction> list, boolean resetSavedLists) {
        if (resetSavedLists) {
            actualList = list;
            savedLists.clear();
        }

        if (savedLists.containsKey(this.year)) {
            sectionList = savedLists.get(this.year);
        } else {
            sectionList.clear();
            collectTransactionByMonthId(list);
            months.forEach((name, id) -> sectionList.add(new Section(getStringResId(name), transactionsCollection.get(id))));
            savedLists.put(this.year, new ArrayList<>(sectionList));
        }
        notifyUpdatedList();
    }

    private void notifyUpdatedList() {
        expandableListAdapter.updateItems(new ArrayList<>(sectionList));
        expandableListAdapter.notifyAdapter(expandableListView);
    }

    private int getStringResId(String stringName) {
        return getResources().getIdentifier(stringName, "string", view.getContext().getPackageName());
    }

    private void collectTransactionByMonthId(List<ComingAndTransaction> list) {
        initializeEmptyTransactionsCollection();

        globalList = list.stream().filter(element -> {
            long repeatDateMillis = element.coming.getExpireDate();
            return repeatDateMillis >= startYear && repeatDateMillis <= endYear;
        }).collect(Collectors.toList());

        globalList.forEach(item -> {
            int monthNumber = getMonthNumberFromDate(item.coming.getExpireDate());

            ArrayList<ComingAndTransaction> actualList = transactionsCollection.get(monthNumber);
            assert actualList != null;
            actualList.add(item);
            transactionsCollection.put(monthNumber, actualList);
        });
    }

    private int getMonthNumberFromDate(long dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        return date.get(Calendar.MONTH);
    }

    private void initializeEmptyTransactionsCollection() {
        months.forEach((name, id) -> transactionsCollection.put(id, new ArrayList<>()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}