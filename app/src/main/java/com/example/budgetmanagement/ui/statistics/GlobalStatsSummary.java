package com.example.budgetmanagement.ui.statistics;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GlobalStatsSummary {

    private final int numberOfTransactions;
    private final int numberOfTransactionsAfterTheTime;

    private ComingAndTransaction nextIncomeTransaction;

    private ComingAndTransaction nextPaymentTransaction;

    public GlobalStatsSummary(List<ComingAndTransaction> allTransactions) {
//        TODO: Change this to for loop
        Calendar today = Calendar.getInstance();
        long todayInMillis = today.getTimeInMillis();
        numberOfTransactions = allTransactions.size();

        numberOfTransactionsAfterTheTime = (int) allTransactions
                .stream()
                .filter(item -> !item.coming.isExecuted())
                .filter(item -> todayInMillis - item.coming.getExpireDate() > 0) // millisOfTransactionAfterTheTime
                .count();

        DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
        long firstMillisTodayDay = new DateTime(todayInMillis, defaultTimeZone).withTimeAtStartOfDay().getMillis();

        List<ComingAndTransaction> allTransactionsWithExpireDateAboveTodayDate = allTransactions
                .stream()
                .filter(item -> item.coming.getExpireDate() >= firstMillisTodayDay)
                .filter(item -> !item.coming.isExecuted())
                .collect(Collectors.toList());

        Optional<ComingAndTransaction> nextPaymentOptional = allTransactionsWithExpireDateAboveTodayDate.stream()
                .filter(item -> {
                    BigDecimal amount = new BigDecimal(item.transaction.getAmount());
                    return amount.signum() != 1;
                })
                .min(Comparator.comparingLong(item -> Math.abs(item.coming.getExpireDate() - todayInMillis)));

        nextPaymentOptional.ifPresent(comingAndTransaction -> nextPaymentTransaction = comingAndTransaction);

        Optional<ComingAndTransaction> nextIncomeOptional = allTransactionsWithExpireDateAboveTodayDate.stream()
                .filter(item -> {
                    BigDecimal amount = new BigDecimal(item.transaction.getAmount());
                    return amount.signum() == 1;
                })
                .min(Comparator.comparingLong(item -> Math.abs(item.coming.getExpireDate() - todayInMillis)));

        nextIncomeOptional.ifPresent(comingAndTransaction -> nextIncomeTransaction = comingAndTransaction);
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public int getNumberOfTransactionsAfterTheTime() {
        return numberOfTransactionsAfterTheTime;
    }

    public ComingAndTransaction getNextIncomeTransaction() {
        return nextIncomeTransaction;
    }

    public ComingAndTransaction getNextPaymentTransaction() {
        return nextPaymentTransaction;
    }
}
