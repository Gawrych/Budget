package com.example.budgetmanagement.ui.details;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class TransactionDataForBinding extends DetailsUtils {

    private final Transaction transaction;
    private final Category category;
    public String title;
    public String amount;
    public Drawable amountIcon;
    public Drawable icon;
    public String addDate;
    public String categoryName;
    public String lastEditDate;
    public String remainingDate;
    public int isExecutedViewMode;
    private boolean isExecuted;
    public String remainingDays;
    public int remainingColor;
    public String executedDate;
    private final Fragment fragment;

    public TransactionDataForBinding(int transactionId, @NonNull Fragment fragment) {
        this.fragment = fragment;

        TransactionViewModel transactionViewModel = new ViewModelProvider(fragment).get(TransactionViewModel.class);
        this.transaction = transactionViewModel.getTransactionById(transactionId);

        CategoryViewModel categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);
        this.category = categoryViewModel.getCategoryById(transaction.getCategoryId());

        setValues();
    }

    public void setValues() {
        this.title = transaction.getTitle();
        this.amount = transaction.getAmount();
        this.amountIcon = super.getAmountIconDependOfValue(transaction.getAmount());
        this.categoryName = category.getName();
        this.icon = super.getCategoryIcon(category);
        this.addDate = DateProcessor.parseDate(transaction.getAddDate());
        this.lastEditDate = super.getValueOrLabel(transaction.getLastEditDate(), transaction.getLastEditDate() != 0);
        this.remainingDate = DateProcessor.parseDate(transaction.getDeadline());
        this.isExecuted = transaction.isExecuted();
        this.isExecutedViewMode = this.isExecuted ? View.VISIBLE : View.INVISIBLE;
        this.executedDate = super.getValueOrLabel(transaction.getExecutedDate(), this.isExecuted);
        setRemainingDaysWithPrefix(transaction);
    }

    private void setRemainingDaysWithPrefix(Transaction transaction) {
        int days = getRemainingDaysNumber(transaction.getDeadline());
        int textColor;
        String remainingDaysText;

        Context context = getContext();

        if (this.isExecuted) {
            remainingDaysText = "";
            textColor = R.color.font_default;
        } else if (days == 0) {
            remainingDaysText = context.getString(R.string.today);
            textColor = R.color.mat_red;
        } else if (days < 0) {
            remainingDaysText = context.getString(R.string.for_number_days, Math.abs(days));
            textColor = R.color.mat_red;
        } else {
            remainingDaysText = context.getString(R.string.in_number_days, days);
            textColor = R.color.font_default;
        }

        this.remainingDays = remainingDaysText;
        this.remainingColor = context.getColor(textColor);
    }

    @NonNull
    private Context getContext() {
        return this.fragment.requireContext();
    }

    private int getRemainingDaysNumber(long endDateInMillis) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(endDateInMillis);

        LocalDate today = LocalDate.now();
        LocalDate deadline = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (int) ChronoUnit.DAYS.between(today, deadline);
    }

    @Override
    public Fragment getFragment() {
        return this.fragment;
    }
}
