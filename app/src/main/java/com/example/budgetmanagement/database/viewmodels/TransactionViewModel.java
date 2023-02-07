package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.rooms.TransactionRepository;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TransactionViewModel extends AndroidViewModel {

    public static final String TITLE_FIELD_TAG = "title";
    public static final String AMOUNT_FIELD_TAG = "amount";
    public static final String CATEGORY_FIELD_TAG = "category";
    public static final String START_DATE_FIELD_TAG = "startDate";
    public static final String END_DATE_FIELD_TAG = "endDate";
    public static final String PERIOD_FIELD_TAG = "period";
    private TransactionRepository transactionRepository;

    public TransactionViewModel(@NonNull Application app) {
        super(app);
        transactionRepository = new TransactionRepository(app);
    }

    public long insert(Transaction transaction) {
       return transactionRepository.insert(transaction);
    }

    public long insert(String title, String amount, long categoryId, long startDate) {
        Transaction transaction = new Transaction(0, (int) categoryId, title, amount,
                System.currentTimeMillis(), 0, true);
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
}