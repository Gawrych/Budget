package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.rooms.TransactionRepository;

import java.util.Calendar;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionRepository transactionRepository;

    public TransactionViewModel(@NonNull Application app) {
        super(app);
        transactionRepository = new TransactionRepository(app);
    }

    public long insert(Transaction transaction) {
       return transactionRepository.insert(transaction);
    }

    public void insertCyclical(Context context, Transaction transaction, long endDate, String periodName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(transaction.getDeadline());
        int period = getPeriodId(context, periodName);

        while (calendar.getTimeInMillis() <= endDate) {
            transactionRepository.insert(transaction);
            calendar.add(period, 1);
        }
    }

    private int getPeriodId(Context context, String period) {
        ArrayMap<String, Integer> periodsWithIds = new ArrayMap<>();
        periodsWithIds.put(context.getString(R.string.each_day), Calendar.DAY_OF_YEAR);
        periodsWithIds.put(context.getString(R.string.each_week), Calendar.WEEK_OF_YEAR);
        periodsWithIds.put(context.getString(R.string.each_month), Calendar.MONTH);
        periodsWithIds.put(context.getString(R.string.each_year), Calendar.YEAR);

        Integer selectedPeriod = periodsWithIds.get(period);

        return  selectedPeriod == null ? Calendar.MONTH : selectedPeriod;
    }

    public void update(Transaction transaction) {
        transactionRepository.update(transaction);
    }

    public void changeAllFromDeletedCategoryToDefault(int categoryIdToRemoveFromTransactions) {
        transactionRepository.changeAllFromDeletedCategoryToDefault(categoryIdToRemoveFromTransactions);
    }

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    public void delete(int transactionId) {
        transactionRepository.delete(transactionId);
    }

    public List<Transaction> getAllTransactionsByYearInList(int year) {
        return transactionRepository.getAllTransactionsByYearInList(year);
    }

    public LiveData<List<Transaction>> getAllTransactionsByYear(int year) {
        return transactionRepository.getAllTransactionsByYear(year);
    }

    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.getTransactionById(transactionId);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    public int[] getAllYears() {
        return transactionRepository.getAllYears();
    }
}