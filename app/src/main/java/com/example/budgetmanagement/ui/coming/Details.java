package com.example.budgetmanagement.ui.coming;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

public class Details {

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
    private final Activity activity;

    public Details(ComingAndTransaction comingAndTransaction, Category category, Activity activity) {
        this.activity = activity;

        Transaction transaction = comingAndTransaction.transaction;
        Coming coming = comingAndTransaction.coming;

        ComingViewModel comingViewModel = new ViewModelProvider(activity.get).get(ComingViewModel.class);

        title = transaction.getTitle();
        amount = transaction.getAmount();
        categoryName = category.getName();
        icon = getCategoryIcon(category);
        addDate = DateProcessor.parseDate(coming.getAddDate());
        lastEditDate = getValueFromModifiedDate(coming.getLastEditDate());
        int remainingDays = getRemainingDays(coming.getExpireDate());
        remainingDate = DateProcessor.parseDate(coming.getExpireDate());
        remainingDayAmount = String.valueOf(Math.abs(remainingDays));
        amountIcon = getAmountIconDependOfValue(transaction.getAmount());
        executedDate = DateProcessor.parseDate(coming.getExecutedDate());
    }

    private Drawable getCategoryIcon(Category category) {
        IconPack iconPack = ((AppIconPack) activity.getApplication()).getIconPack();
        assert iconPack != null;
        return Objects.requireNonNull(iconPack.getIcon(category.getIcon())).getDrawable();
    }

    private String getValueFromModifiedDate(long modifiedDate) {
        if (modifiedDate != 0) {
            return DateProcessor.parseDate(modifiedDate);
        }
        return activity.getApplicationContext().getString(R.string.never);
    }

    private int getRemainingDays(long repeatDate) {
        Calendar todayDate = getTodayDate();
        Calendar deadlineDate = getCalendarWithValue(repeatDate);
        return deadlineDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
    }

    private Calendar getTodayDate() {
        return Calendar.getInstance();
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    private Drawable getAmountIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_down_24, R.color.mat_red);
        } else {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_up_24, R.color.mat_green);
        }
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private Drawable getDrawableWithColor(int drawableResId, int colorResId) {
        Drawable drawable = ResourcesCompat.getDrawable(activity.getApplicationContext().getResources(), drawableResId, null);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(activity.getApplicationContext(), colorResId), PorterDuff.Mode.SRC_IN));
        } else {
            drawable = ResourcesCompat.getDrawable(activity.getApplicationContext().getResources(), R.drawable.ic_outline_icon_not_found_24, null);
        }
        return drawable;
    }
}
