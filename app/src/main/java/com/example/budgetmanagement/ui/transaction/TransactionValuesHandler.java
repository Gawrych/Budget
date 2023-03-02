package com.example.budgetmanagement.ui.transaction;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.CategoryIconHelper;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class TransactionValuesHandler implements CategoryIconHelper {

    private final Transaction transaction;
    private final Category category;
    public String title;
    public BigDecimal amount;
    public Drawable icon;
    public Drawable iconBackground;
    public long addDate;
    public String categoryName;
    public long lastEditDate;
    public long remainingDate;
    public boolean isExecuted;
    public String remainingDays;
    public int remainingColor;
    public long executedDate;
    private final Fragment fragment;

    public TransactionValuesHandler(int transactionId, @NonNull Fragment fragment) {
        this.fragment = fragment;

        TransactionViewModel transactionViewModel = new ViewModelProvider(fragment).get(TransactionViewModel.class);
        transaction = transactionViewModel.getTransactionById(transactionId);

        CategoryViewModel categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);
        category = categoryViewModel.getCategoryById(transaction.getCategoryId());

        setValues();
    }

    public void setValues() {
        title = transaction.getTitle();
        amount = new BigDecimal(transaction.getAmount());
        categoryName = category.getName();
        isExecuted = transaction.isExecuted();
        lastEditDate = transaction.getLastEditDate();
        executedDate = transaction.getExecutedDate();
        icon = CategoryIconHelper.getCategoryIcon(category.getIcon(), fragment.requireActivity());
        iconBackground = CategoryIconHelper.getIconBackground(fragment.requireContext(), category.getColor(), R.drawable.background_oval);
        remainingDate = transaction.getDeadline();
        addDate = transaction.getAddDate();
        setRemainingDaysWithPrefix(transaction);
    }

    private void setRemainingDaysWithPrefix(Transaction transaction) {
        int days = DateProcessor.getRemainingDays(transaction.getDeadline());
        int textColor;
        String remainingDaysText;

        Context context = fragment.requireContext();

        if (isExecuted) {
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

        remainingDays = remainingDaysText;
        remainingColor = context.getColor(textColor);
    }
}
