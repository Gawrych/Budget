package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MonthsStatsCollector {

    private static final int NUMBER_OF_MONTHS = 12;
    private final ComingViewModel comingViewModel;

    public MonthsStatsCollector(ComingViewModel comingViewModel) {
        this.comingViewModel = comingViewModel;
    }

    public PeriodSummary[] getStats(int year) {
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
}
