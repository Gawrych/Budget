package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;

import java.util.Calendar;
import java.util.List;

public class MonthsStats {

    private static final int NUMBER_OF_MONTHS = 12;
    private final PeriodSummary[] periodSummary = new PeriodSummary[NUMBER_OF_MONTHS];
    private final ComingViewModel comingViewModel;

    public MonthsStats(ComingViewModel comingViewModel) {
        this.comingViewModel = comingViewModel;
    }

    public PeriodSummary[] getMonthsStatsFromYear(int year) {
        List<ComingAndTransaction> allComingFromCurrentYear =
                comingViewModel.getAllComingByYear(year);

        for (int i = 0; i < periodSummary.length; i++) {
            periodSummary[i] = new PeriodSummary();
        }

        Calendar calendar = Calendar.getInstance();
        for (ComingAndTransaction element : allComingFromCurrentYear) {
            long expireDate = element.coming.getExpireDate();
            calendar.setTimeInMillis(expireDate);
            periodSummary[calendar.get(Calendar.MONTH)].add(element);
        }

        return periodSummary;
    }
}
