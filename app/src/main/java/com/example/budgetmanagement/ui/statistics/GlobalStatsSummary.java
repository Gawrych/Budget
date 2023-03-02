package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.Transaction;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GlobalStatsSummary {

    private final int numberOfTransactions;
    private final int numberOfTransactionsAfterTheTime;
    private final Transaction nextIncomeTransaction;
    private final Transaction nextPaymentTransaction;

    public GlobalStatsSummary(List<Transaction> allTransactions) {
        numberOfTransactions = allTransactions.size();

        List<Transaction> onlyNotExecutedTransactions = allTransactions
                .stream()
                .filter(this::isNotExecuted)
                .collect(Collectors.toList());

        numberOfTransactionsAfterTheTime = (int) onlyNotExecutedTransactions
                .stream()
                .filter(this::isOverdue)
                .count();

        List<Transaction> futureTransactions = onlyNotExecutedTransactions
                .stream()
                .filter(this::isFuture)
                .collect(Collectors.toList());

        nextPaymentTransaction = getFirstTransaction(futureTransactions, this::isNotProfit);
        nextIncomeTransaction = getFirstTransaction(futureTransactions, this::isProfit);
    }

    private Transaction getFirstTransaction(List<Transaction> list, Predicate<Transaction> condition) {
        long todayInMillis = System.currentTimeMillis();
        return list.stream()
                .filter(condition)
                .min(Comparator.comparingLong(item -> Math.abs(item.getDeadline() - todayInMillis)))
                .orElse(null);
    }

    private boolean isNotExecuted(Transaction transaction) {
        return !transaction.isExecuted();
    }

    private boolean isOverdue(Transaction transaction) {
        long todayInMillis = System.currentTimeMillis();
        return todayInMillis - transaction.getDeadline() > 0;
    }

    private boolean isFuture(Transaction transaction) {
        long todayInMillis = System.currentTimeMillis();
        long firstMillisTodayDay = new DateTime(todayInMillis, DateTimeZone.getDefault())
                .withTimeAtStartOfDay().getMillis();
        return transaction.getDeadline() >= firstMillisTodayDay;
    }

    private boolean isNotProfit(Transaction transaction) {
        return !transaction.isProfit();
    }

    private boolean isProfit(Transaction transaction) {
        return transaction.isProfit();
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
