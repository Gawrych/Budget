package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.rooms.TransactionRepository;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;

    public TransactionViewModel(@NonNull Application app) {
        super(app);
        transactionRepository = new TransactionRepository(app);
    }

    public long insert(Transaction transaction) {
       return transactionRepository.insert(transaction);
    }

    public long insert(String title, String amount, long categoryId, Calendar startDate) {
        Transaction transaction = new Transaction(0, (int) categoryId, title, amount,
                System.currentTimeMillis(), 0, false,
                startDate.getTimeInMillis(), startDate.get(Calendar.YEAR), 0);
        return transactionRepository.insert(transaction);
    }

//        public long insertCyclical(String title, String amount, long categoryId, long startDate, long endDate, String period) {
//            while (nextDate <= endDate) {
//                allDatesToCreateNewComing.add(nextDate);
//                calendar.add(timeBetween, 1);
//                nextDate = calendar.getTimeInMillis();
//            }
//        Transaction transaction = new Transaction(0,
//                transactionNumbers.get(CATEGORY_FIELD_TAG),
//                transactionNames.get(TITLE_FIELD_TAG),
//                transactionNames.get(AMOUNT_FIELD_TAG),
//                System.currentTimeMillis(),
//                0,
//                true);
//        return transactionRepository.insert(transaction);
//    }

//    public ArrayList<Long> getNextDates() {
//        ArrayList<Long> allDatesToCreateNewComing = new ArrayList<>();
//        SwitchMaterial cyclicalSwitch = fieldsInterface.getCyclicalSwitch();
//
//        if (!cyclicalSwitch.isChecked()) {
//            allDatesToCreateNewComing.add(getStartDate());
//            return allDatesToCreateNewComing;
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(getStartDate());
//        long nextDate = calendar.getTimeInMillis();
//
//        Integer valueFromTimeBetweenMap = timeBetweenValues.get(timeBetweenExecutePicker.getText().toString());
//        if (valueFromTimeBetweenMap == null) {
//            return allDatesToCreateNewComing;
//        }
//
//        int timeBetween = valueFromTimeBetweenMap;
//        long endDate = getEndDate();
//
//        while (nextDate <= endDate) {
//            allDatesToCreateNewComing.add(nextDate);
//            calendar.add(timeBetween, 1);
//            nextDate = calendar.getTimeInMillis();
//        }
//
//        return allDatesToCreateNewComing;
//    }

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

    public List<Transaction> getAllTransactionByYear(int year) {
        return transactionRepository.getAllTransactionsByYearInList(year);
    }

    public LiveData<List<Transaction>> getAllTransactionsByYear(int year) {
        return transactionRepository.getAllTransactionsByYear(year);
    }

    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.getTransactionById(transactionId);
    }

    public int[] getAllYears() {
        return transactionRepository.getAllYears();
    }
}