package com.example.budgetmanagement.database.rooms;

import static com.example.budgetmanagement.ui.utils.DateProcessor.DEFAULT_DATE_FORMAT;

import android.content.Context;
import android.util.ArrayMap;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class TransactionQuery {

    private final Context context;
    private final TransactionViewModel transactionViewModel;
    private final CategoryViewModel categoryViewModel;
    private Transaction transaction;

    public TransactionQuery(Context context, ViewModelStoreOwner owner) {
        this.context = context;
        this.transactionViewModel = new ViewModelProvider(owner).get(TransactionViewModel.class);
        this.categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);
    }

    public void createNewTransaction(String title, String amount, String categoryName, String startDateInPattern) {
        Calendar startDate = DateProcessor
                .getCalendarFromDateInStringPattern(startDateInPattern, DEFAULT_DATE_FORMAT);

        this.transaction = new Transaction(
                0,
                getCategoryIdFromName(categoryName),
                title,
                amount,
                System.currentTimeMillis(),
                0,
                false,
                startDate.getTimeInMillis(),
                startDate.get(Calendar.YEAR),
                0);
    }

    private int getCategoryIdFromName(String name) {
        Category category = this.categoryViewModel.getCategoryByName(name);
        return category.getCategoryId();
    }

    public void submitCyclical(String endDateInPattern, String periodName) {
        Calendar endDate = DateProcessor.getCalendarFromDateInStringPattern(endDateInPattern, DEFAULT_DATE_FORMAT);
        long endDateInMillis = endDate.getTimeInMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.transaction.getDeadline());
        int period = getPeriodId(context, periodName);

        while (calendar.getTimeInMillis() <= endDateInMillis) {
            submit();
            calendar.add(period, 1);
            this.transaction.setDeadline(calendar.getTimeInMillis());
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

    public void createTransactionToUpdate(Transaction transactionToEdit, String title, String amount,
                                          String categoryName, String startDateInPattern) {
        Calendar startDate = DateProcessor
                .getCalendarFromDateInStringPattern(startDateInPattern, DEFAULT_DATE_FORMAT);

        this.transaction = new Transaction(
                transactionToEdit.getTransactionId(),
                getCategoryIdFromName(categoryName),
                title,
                amount,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                transactionToEdit.isExecuted(),
                startDate.getTimeInMillis(),
                startDate.get(Calendar.YEAR),
                transactionToEdit.getExecutedDate());
    }

    public void submit() {
        transactionViewModel.insert(this.transaction);
    }

    public void update() {
        transactionViewModel.update(this.transaction);
    }
}
