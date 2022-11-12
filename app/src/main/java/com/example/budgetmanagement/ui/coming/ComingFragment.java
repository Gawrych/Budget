package com.example.budgetmanagement.ui.coming;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.adapters.ComingExpandableListAdapter;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
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

    private View view;
    private ComingFragmentBinding binding;
    private ArrayList<Section> sectionList = new ArrayList<>();
    private final HashMap<Integer, ArrayList<ComingAndTransaction>> transactionsCollection = new HashMap<>();
    private ComingExpandableListAdapter expandableListAdapter;
    private int selectedYear = 2022;
    private long startYear = 0;
    private long endYear = 0;
    private final HashMap<Integer, ArrayList<Section>> savedLists = new HashMap<>();
    private DatePickerDialog datePickerDialog;
    private List<ComingAndTransaction> actualList;
    public static final String COMING_BOTTOM_SHEET_TAG = "coming_bottom_sheet";

    public static final Map<Integer, Integer> months;
    static {
        Map<Integer, Integer> items = new LinkedHashMap<>();
        items.put(R.string.january, Calendar.JANUARY);
        items.put(R.string.february, Calendar.FEBRUARY);
        items.put(R.string.march, Calendar.MARCH);
        items.put(R.string.april, Calendar.APRIL);
        items.put(R.string.may, Calendar.MAY);
        items.put(R.string.june, Calendar.JUNE);
        items.put(R.string.july, Calendar.JULY);
        items.put(R.string.august, Calendar.AUGUST);
        items.put(R.string.september, Calendar.SEPTEMBER);
        items.put(R.string.october, Calendar.OCTOBER);
        items.put(R.string.november, Calendar.NOVEMBER);
        items.put(R.string.december, Calendar.DECEMBER);
        months = Collections.unmodifiableMap(items);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSelectedYearToCurrent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ComingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        binding.pickedYear.setText(String.valueOf(selectedYear));

        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        expandableListAdapter = new ComingExpandableListAdapter
                (requireContext(), sectionList, this, iconPack);

        binding.expandableListView.setAdapter(expandableListAdapter);

        ComingViewModel comingViewModel =
                new ViewModelProvider(this).get(ComingViewModel.class);
        comingViewModel.getAllComingAndTransaction().observe(getViewLifecycleOwner(), list
                -> setSections(list, true));

//        int actualPositionToScroll = getActualPositionToScroll();
//        binding.expandableListView.smoothScrollToPositionFromTop(actualPositionToScroll, 0);

        binding.expandableListView.setOnChildClickListener
                ((parent, v, groupPosition, childPosition, id)
                        -> openDetailsFragment(groupPosition, childPosition));

        binding.expandableListView.setOnItemLongClickListener
                ((parent, v, flatPosition, id) -> showBottomSheetMenu(parent, flatPosition));

        binding.addButton.setOnClickListener(root -> Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_addNewComingElement));

        binding.categoriesButton.setOnClickListener(root -> Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_categoryList));

        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> true);

        binding.yearSelector.setOnClickListener(v -> selectYear());
    }

    private void initializeSelectedYearToCurrent() {
        this.selectedYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    private boolean openDetailsFragment(int groupPosition, int childPosition) {
        ComingAndTransaction comingAndTransaction =
                expandableListAdapter.getChild(groupPosition, childPosition);
        ComingElementDetails comingElementDetails =
                ComingElementDetails.newInstance(comingAndTransaction.coming.getComingId());
        Navigation.findNavController(view).navigate(
                R.id.action_navigation_coming_to_comingElementDetails,
                comingElementDetails.getArguments());
        return true;
    }

    private boolean showBottomSheetMenu(AdapterView<?> parent, int flatPosition) {
        long packedPosition = ((ExpandableListView) parent).getExpandableListPosition(flatPosition);
        if (ExpandableListView.getPackedPositionType(packedPosition)
                == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

            ComingAndTransaction comingAndTransaction =
                    expandableListAdapter.getChild(groupPosition, childPosition);
            BottomSheetDialogComing bottomSheet =
                    BottomSheetDialogComing.newInstance(comingAndTransaction.coming.getComingId());
            bottomSheet.show(getParentFragmentManager(), COMING_BOTTOM_SHEET_TAG);
            return true;
        }
        return false;
    }

    private void selectYear() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
         datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {}, this.selectedYear, mMonth, mDay);

        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().getTouchables().get(1).setVisibility(View.GONE);
        datePickerDialog.getDatePicker()
                .setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                    this.selectedYear = year;
                    binding.pickedYear.setText(String.valueOf(year));
                    setSections(actualList, false);
                    datePickerDialog.cancel();
        });
        datePickerDialog.show();
    }

    private int getActualPositionToScroll() {
        int monthNumber = getMonthNumberFromDate(Calendar.getInstance().getTimeInMillis());
        int endPosition = monthNumber;
        for (int i=0; i<monthNumber; i++) {
            endPosition = endPosition + sectionList.get(i).getComingAndTransactionList().size();
        }
        return endPosition;
    }

    private void setYearStartAndEnd() {
        startYear = getFirstMillisOfYear(this.selectedYear).getTimeInMillis();
        endYear = getLastMillisOfYear(this.selectedYear).getTimeInMillis();
    }

    private Calendar getLastMillisOfYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c;
    }

    private Calendar getFirstMillisOfYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    private void setSections(List<ComingAndTransaction> list, boolean resetSavedLists) {
        if (resetSavedLists) {
            actualList = list;
            savedLists.clear();
        }

        if (savedLists.containsKey(this.selectedYear)) {
            sectionList = savedLists.get(this.selectedYear);
        } else {
            sectionList.clear();
            collectTransactionByMonthId(list);
            months.forEach((name, id) -> sectionList.add(new Section(name, transactionsCollection.get(id))));
            savedLists.put(this.selectedYear, new ArrayList<>(sectionList));
        }
        notifyUpdatedList();
    }

    private void notifyUpdatedList() {
        expandableListAdapter.updateItems(new ArrayList<>(sectionList));
        expandableListAdapter.notifyAdapter(binding.expandableListView);
    }

    private void collectTransactionByMonthId(List<ComingAndTransaction> list) {
        initializeEmptyTransactionsCollectionForEachMonth();

        setYearStartAndEnd();

        List<ComingAndTransaction> globalList = list.stream().filter(element -> {
            long repeatDateMillis = element.coming.getExpireDate();
            return repeatDateMillis >= startYear && repeatDateMillis <= endYear;
        }).collect(Collectors.toList());

        globalList.forEach(item -> {
            int monthNumber = getMonthNumberFromDate(item.coming.getExpireDate());

            ArrayList<ComingAndTransaction> currentList = transactionsCollection.get(monthNumber);
            assert currentList != null;
            currentList.add(item);
            transactionsCollection.put(monthNumber, currentList);
        });
    }

    private int getMonthNumberFromDate(long dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        return date.get(Calendar.MONTH);
    }

    private void initializeEmptyTransactionsCollectionForEachMonth() {
        months.forEach((name, id) -> transactionsCollection.put(id, new ArrayList<>()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}