package com.example.budgetmanagement.ui.coming;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SectionMaker {

    private final HashMap<Integer, ArrayList<ComingAndTransaction>> transactionsCollection = new HashMap<>();
    private final HashMap<Integer, ArrayList<Section>> savedLists = new HashMap<>();
    private final ComingViewModel comingViewModel;
    public final Map<Integer, Integer> months = new LinkedHashMap<>();
    private ArrayList<Section> sectionList = new ArrayList<>();
    private int year;


    public SectionMaker(ComingViewModel comingViewModel, int year) {
        this.comingViewModel = comingViewModel;
        this.year = year;
        prepareMonthsMap();
    }

    private void prepareMonthsMap() {
        months.put(R.string.january, Calendar.JANUARY);
        months.put(R.string.february, Calendar.FEBRUARY);
        months.put(R.string.march, Calendar.MARCH);
        months.put(R.string.april, Calendar.APRIL);
        months.put(R.string.may, Calendar.MAY);
        months.put(R.string.june, Calendar.JUNE);
        months.put(R.string.july, Calendar.JULY);
        months.put(R.string.august, Calendar.AUGUST);
        months.put(R.string.september, Calendar.SEPTEMBER);
        months.put(R.string.october, Calendar.OCTOBER);
        months.put(R.string.november, Calendar.NOVEMBER);
        months.put(R.string.december, Calendar.DECEMBER);
    }

    public ArrayList<Section> prepareSections(boolean resetSavedLists) {
        if (resetSavedLists) {
            savedLists.clear();
        }

        if (savedLists.containsKey(this.year)) {
            sectionList = savedLists.get(this.year);
        } else {
            sectionList.clear();
            collectTransactionByMonthId();
            months.forEach((name, id) -> sectionList.add(new Section(name, transactionsCollection.get(id))));
            savedLists.put(this.year, new ArrayList<>(sectionList));
        }
        return sectionList;
    }

    private void collectTransactionByMonthId() {
        initializeEmptyTransactionsCollectionForEachMonth();
        List<ComingAndTransaction> allComingFromSelectedYear =
                comingViewModel.getAllComingByYear(year);

        allComingFromSelectedYear.forEach(item -> {
            int monthNumber = getMonthNumberFromDate(item.coming.getExpireDate());
            ArrayList<ComingAndTransaction> currentList = transactionsCollection.get(monthNumber);
            assert currentList != null;
            currentList.add(item);
            transactionsCollection.put(monthNumber, currentList);
        });
    }

    private void initializeEmptyTransactionsCollectionForEachMonth() {
        months.forEach((name, id) -> transactionsCollection.put(id, new ArrayList<>()));
    }

    private int getMonthNumberFromDate(long dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        return date.get(Calendar.MONTH);
    }

    public void changeYear(int year) {
        this.year = year;
    }
}
