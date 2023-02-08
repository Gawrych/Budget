package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class YearsStatsCollector {

    private final ComingViewModel comingViewModel;
    private int[] allYears;

    public YearsStatsCollector(ComingViewModel comingViewModel) {
        this.comingViewModel = comingViewModel;
    }

    public LinkedHashMap<Integer, PeriodSummary> getStats() {
        allYears = comingViewModel.getAllYears();
        List<List<Transaction>> yearsTransaction = new ArrayList<>();

        for (int allYear : allYears) {
            yearsTransaction.add(comingViewModel.getAllComingByYear(allYear));
        }

        LinkedHashMap<Integer, PeriodSummary> yearsSummary = new LinkedHashMap<>(allYears.length);

        for (int allYear : allYears) {
            yearsSummary.put(allYear, new PeriodSummary());
        }

        for (int i = 0; i < yearsTransaction.size(); i++) {
            for (Transaction element : yearsTransaction.get(i)) {
                PeriodSummary periodSummary = yearsSummary.get(allYears[i]);
                if (periodSummary != null) {
                    periodSummary.add(element);
                }
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
