package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatsCollector {

    private static final int NUMBER_OF_MONTHS = 12;
    private final ComingViewModel comingViewModel;
    private int[] allYears;

    public StatsCollector(ComingViewModel comingViewModel) {
        this.comingViewModel = comingViewModel;
    }

    public PeriodSummary[] getMonthsStats(int year) {
        PeriodSummary[] periodSummary = new PeriodSummary[NUMBER_OF_MONTHS];
        List<ComingAndTransaction> allComingFromYear =
                comingViewModel.getAllComingByYear(year);

        for (int i = 0; i < periodSummary.length; i++) {
            periodSummary[i] = new PeriodSummary();
        }

        Calendar calendar = Calendar.getInstance();
        for (ComingAndTransaction element : allComingFromYear) {
            long expireDate = element.coming.getExpireDate();
            calendar.setTimeInMillis(expireDate);
            periodSummary[calendar.get(Calendar.MONTH)].add(element);
        }

        return periodSummary;
    }

    public PeriodSummary[] getYearStats() {
        allYears = comingViewModel.getAllYears();
        PeriodSummary[] periodSummary = new PeriodSummary[allYears.length];
        List<List<ComingAndTransaction>> yearsTransaction = new ArrayList<>();

        for (int allYear : allYears) {
            yearsTransaction.add(comingViewModel.getAllComingByYear(allYear));
        }

        for (int i = 0; i < allYears.length; i++) {
            periodSummary[i] = new PeriodSummary();
        }

        for (int i = 0; i < yearsTransaction.size(); i++) {
            for (ComingAndTransaction element : yearsTransaction.get(i)) {
                periodSummary[i].add(element);
            }
        }

        return periodSummary;
    }

    public int[] getYears() {
        if (allYears != null) {
            return allYears;
        }
        return new int[0];
    }
}
