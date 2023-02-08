package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.Transaction;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GlobalStatsSummary {

    private final int numberOfTransactions;
    private final int numberOfTransactionsAfterTheTime;

    private Transaction nextIncomeTransaction;

    private Transaction nextPaymentTransaction;

    public GlobalStatsSummary(List<Transaction> allTransactions) {
//        TODO: Change this to for loop
        Calendar today = Calendar.getInstance();
        long todayInMillis = today.getTimeInMillis();
        numberOfTransactions = allTransactions.size();

        numberOfTransactionsAfterTheTime = (int) allTransactions
                .stream()
                .filter(item -> !item.isExecuted())
                .filter(item -> todayInMillis - item.getDeadline() > 0) // millisOfTransactionAfterTheTime
                .count();

        DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
        long firstMillisTodayDay = new DateTime(todayInMillis, defaultTimeZone).withTimeAtStartOfDay().getMillis();

        List<Transaction> allTransactionsWithExpireDateAboveTodayDate = allTransactions
                .stream()
                .filter(item -> item.getDeadline() >= firstMillisTodayDay)
                .filter(item -> !item.isExecuted())
                .collect(Collectors.toList());

        Optional<Transaction> nextPaymentOptional = allTransactionsWithExpireDateAboveTodayDate.stream()
                .filter(item -> {
                    BigDecimal amount = new BigDecimal(item.getAmount());
                    return amount.signum() != 1;
                })
                .min(Comparator.comparingLong(item -> Math.abs(item.getDeadline() - todayInMillis)));

        nextPaymentOptional.ifPresent(comingAndTransaction -> nextPaymentTransaction = comingAndTransaction);

        Optional<Transaction> nextIncomeOptional = allTransactionsWithExpireDateAboveTodayDate.stream()
                .filter(item -> {
                    BigDecimal amount = new BigDecimal(item.getAmount());
                    return amount.signum() == 1;
                })
                .min(Comparator.comparingLong(item -> Math.abs(item.getDeadline() - todayInMillis)));

        nextIncomeOptional.ifPresent(comingAndTransaction -> nextIncomeTransaction = comingAndTransaction);
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public int getNumberOfTransactionsAfterTheTime() {
        return numberOfTransactionsAfterTheTime;
    }

    public Transaction getNextIncomeTransaction() {
        return nextIncomeTransaction;
    }

    public Transaction getNextPaymentTransaction() {
        return nextPaymentTransaction;
    }
}
