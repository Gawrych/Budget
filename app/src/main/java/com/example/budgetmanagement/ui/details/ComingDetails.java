package com.example.budgetmanagement.ui.details;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class ComingDetails extends DetailsUtils {

    public static final int MODE_AFTER_DEADLINE = -1;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_REALIZED = 1;
    public final String title;
    public final String amount;
    public final Drawable amountIcon;
    public final Drawable icon;
    public final String addDate;
    public final String categoryName;
    public final String lastEditDate;
    public final String remainingDate;
    public final String remainingDayAmount;
    public final String executedDate;
    public final int mode;
    public int textColorRes;
    public int textRes;
    private final Fragment fragment;

    public ComingDetails(int comingId, @NonNull Fragment fragment, int mode) {
        this.fragment = fragment;
        this.mode = mode;

        ComingViewModel comingViewModel = new ViewModelProvider(fragment).get(ComingViewModel.class);
        CategoryViewModel categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);

        Transaction comingAndTransaction = comingViewModel.getComingAndTransactionById(comingId);
        Transaction transaction = comingAndTransaction.transaction;
        Coming coming = comingAndTransaction.coming;

        Category category = categoryViewModel.getCategoryById(transaction.getCategoryId());

        this.title = transaction.getTitle();
        this.amount = transaction.getAmount();
        this.categoryName = category.getName();
        this.icon = getCategoryIcon(category);
        this.addDate = DateProcessor.parseDate(coming.getAddDate());
        this.lastEditDate = getValueFromModifiedDate(coming.getLastEditDate());
        this.remainingDate = DateProcessor.parseDate(coming.getExpireDate());

        int remainingDays = getRemainingDays(coming.getExpireDate());
        this.remainingDayAmount = String.valueOf(Math.abs(remainingDays));
        this.amountIcon = getAmountIconDependOfValue(transaction.getAmount());
        this.executedDate = DateProcessor.parseDate(coming.getExecutedDate());
        this.setFieldAttributesByMode(mode);
    }

    private void setFieldAttributesByMode(int mode) {
        if (mode == MODE_REALIZED) {
            this.textColorRes = R.color.main_green;
            this.textRes = R.string.realized;
        }

        if (mode == MODE_NORMAL) {
            this.textColorRes = R.color.font_default;
            this.textRes = R.string.remain;
        }

        if (mode == MODE_AFTER_DEADLINE) {
            this.textColorRes = R.color.mat_red;
            this.textRes = R.string.after_the_deadline;
        }
    }

    private int getRemainingDays(long repeatDate) {
        Calendar todayDate =  Calendar.getInstance();
        Calendar deadlineDate = getCalendarWithValue(repeatDate);
        return deadlineDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    @Override
    public Fragment getFragment() {
        return this.fragment;
    }
}
