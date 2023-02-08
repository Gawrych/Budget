package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;

import java.util.Calendar;
import java.util.List;

public class MonthsStatsCollector {

    private static final int NUMBER_OF_MONTHS = 12;
    private final TransactionViewModel transactionViewModel;

    public MonthsStatsCollector(TransactionViewModel transactionViewModel) {
        this.transactionViewModel = transactionViewModel;
    }

    public PeriodSummary[] getStats(int year) {
        PeriodSummary[] periodSummary = new PeriodSummary[NUMBER_OF_MONTHS];
        List<Transaction> allComingFromYear =
                transactionViewModel.getAllTransactionsByYearInList(year);

        for (int i = 0; i < periodSummary.length; i++) {
            periodSummary[i] = new PeriodSummary();
        }

        Calendar calendar = Calendar.getInstance();
        for (Transaction element : allComingFromYear) {
            long expireDate = element.getDeadline();
            calendar.setTimeInMillis(expireDate);
            periodSummary[calendar.get(Calendar.MONTH)].add(element);
        }

        return periodSummary;
    }
}
