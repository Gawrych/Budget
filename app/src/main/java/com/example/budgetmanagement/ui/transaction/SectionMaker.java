package com.example.budgetmanagement.ui.transaction;

import android.content.Context;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SectionMaker {

    private final HashMap<Integer, ArrayList<Transaction>> transactionsCollection = new HashMap<>();
    private final TransactionViewModel transactionViewModel;
    public final Map<String, Integer> months = new LinkedHashMap<>();
    private final ArrayList<Section> sectionList = new ArrayList<>();
    private final Context context;
    private int year;


    public SectionMaker(TransactionViewModel transactionViewModel, Context context, int year) {
        this.transactionViewModel = transactionViewModel;
        this.context = context;
        this.year = year;
        prepareMonthsMap();
    }

    private void prepareMonthsMap() {
        String[] monthsNames = context.getResources().getStringArray(R.array.months);
        for(int i=0; i < monthsNames.length; i++) {
            months.put(monthsNames[i], i);
        }
    }

    public ArrayList<Section> prepareSections() {
        sectionList.clear();
        collectTransactionByMonthId();
        months.forEach((name, id) -> sectionList.add(new Section(name, transactionsCollection.get(id))));
        return sectionList;
    }

    private void collectTransactionByMonthId() {
        initializeEmptyTransactionsCollectionForEachMonth();
        List<Transaction> allComingFromSelectedYear =
                transactionViewModel.getAllTransactionsByYearInList(year);

        allComingFromSelectedYear.forEach(item -> {
            int monthNumber = getMonthNumberFromDate(item.getDeadline());
            ArrayList<Transaction> currentList = transactionsCollection.get(monthNumber);
            if (currentList != null) currentList.add(item);
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
