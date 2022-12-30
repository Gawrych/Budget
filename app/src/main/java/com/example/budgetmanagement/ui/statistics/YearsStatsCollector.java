package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class YearsStatsCollector {

    private final ComingViewModel comingViewModel;
    private int[] allYears;

    public YearsStatsCollector(ComingViewModel comingViewModel) {
        this.comingViewModel = comingViewModel;
    }

    public HashMap<Integer, PeriodSummary> getStats() {
        allYears = comingViewModel.getAllYears();
        List<List<ComingAndTransaction>> yearsTransaction = new ArrayList<>();

        for (int allYear : allYears) {
            yearsTransaction.add(comingViewModel.getAllComingByYear(allYear));
        }

        HashMap<Integer, PeriodSummary> yearsSummary = new HashMap<>(allYears.length);

        for (int allYear : allYears) {
            yearsSummary.put(allYear, new PeriodSummary());
        }

        for (int i = 0; i < yearsTransaction.size(); i++) {
            for (ComingAndTransaction element : yearsTransaction.get(i)) {
                yearsSummary.get(allYears[i]).add(element);
            }
        }

        return yearsSummary;
    }

    public int[] getYears() {
        if (allYears != null) {
            return allYears;
        }
        return new int[0];
    }
}
