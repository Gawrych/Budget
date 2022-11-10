package com.example.budgetmanagement.ui.Coming;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingElementDetailsBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

public class ComingElementDetails extends Fragment {

    public static final String COMING_ID_ARG = "comingId";
    public static final int MODE_AFTER_DEADLINE = -1;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_REALIZED = 1;
    private ComingElementDetailsBinding binding;
    private Transaction transaction;
    private Coming coming;

    public static ComingElementDetails newInstance(int comingId) {
        ComingElementDetails fragment = new ComingElementDetails();
        Bundle args = new Bundle();
        args.putInt(COMING_ID_ARG, comingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ComingElementDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int comingId = getArguments() != null ? getArguments().getInt(COMING_ID_ARG, -1) : -1;

        if (comingId == -1) {
            Toast.makeText(requireContext(), R.string.not_found_id, Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        ComingAndTransaction comingAndTransaction = comingViewModel.getComingAndTransactionById(comingId);
        transaction = comingAndTransaction.transaction;
        coming = comingAndTransaction.coming;

        setFields();
        int mode = getMode();
        setFieldAttributesByMode(mode);
    }

    private int getMode() {
        boolean isExecute = coming.isExecute();
        if (isExecute) {
            return MODE_REALIZED;
        }

        boolean isBeforeDeadline = getRemainingDays(coming.getExpireDate()) > 0;
        return isBeforeDeadline ? MODE_NORMAL : MODE_AFTER_DEADLINE;
    }

    private void setFields() {
        Category category = getCategory(transaction);
        binding.transactionName.setText(transaction.getTitle());
        binding.amount.setText(transaction.getAmount());
        binding.addDateLabel.setText(DateProcessor.parseDate(coming.getAddDate()));
        binding.lastEditDate.setText(getValueFromModifiedDate(coming.getLastEditDate()));
        binding.categoryName.setText(category.getName());

        int remainingDays = getRemainingDays(coming.getExpireDate());
        binding.remainingDate.setText(DateProcessor.parseDate(coming.getExpireDate()));
        binding.remainingDays.setText(String.valueOf(Math.abs(remainingDays)));

        Drawable amountIcon = getAmountIconDependOfValue(transaction.getAmount());
        binding.amount.setCompoundDrawablesWithIntrinsicBounds(amountIcon, null, null, null);

        Drawable icon = getCategoryIcon(category);
        binding.categoryIcon.setImageDrawable(icon);
    }

    private void setFieldAttributesByMode(int mode) {
        if (mode == MODE_REALIZED) {
            setRemainingElements(R.string.realized, R.color.main_green);
            binding.staticDaysText.setVisibility(GONE);
            binding.remainingDays.setVisibility(GONE);
            binding.dateWhenWasPaid.setVisibility(VISIBLE);
            binding.dateWhenWasPaid.setText(DateProcessor.parseDate(coming.getExecutedDate()));
        }

        if (mode == MODE_NORMAL) {
            setRemainingElements(R.string.remain, R.color.font_default);
            binding.staticDaysText.setVisibility(VISIBLE);
            binding.remainingDays.setVisibility(VISIBLE);
            binding.dateWhenWasPaid.setVisibility(GONE);
        }

        if (mode == MODE_AFTER_DEADLINE) {
            setRemainingElements(R.string.after_the_deadline, R.color.mat_red);
            binding.staticDaysText.setVisibility(VISIBLE);
            binding.remainingDays.setVisibility(VISIBLE);
            binding.dateWhenWasPaid.setVisibility(GONE);
        }
    }

    private Drawable getAmountIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_down_24, R.color.mat_red);
        } else {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_up_24, R.color.mat_green);
        }
    }

    private Drawable getDrawableWithColor(int drawableResId, int colorResId) {
        Drawable drawable = ResourcesCompat.getDrawable(requireContext().getResources(), drawableResId, null);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(requireContext(), colorResId), PorterDuff.Mode.SRC_IN));
        } else {
            drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_outline_icon_not_found_24, null);
        }
        return drawable;
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    private String getValueFromModifiedDate(long modifiedDate) {
        if (modifiedDate != 0) {
            return DateProcessor.parseDate(modifiedDate);
        }
        return requireContext().getResources().getString(R.string.never);
    }

    private int getRemainingDays(long repeatDate) {
        Calendar todayDate = getTodayDate();
        Calendar deadlineDate = getCalendarWithValue(repeatDate);
        return deadlineDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
    }


    private Calendar getTodayDate() {
        return Calendar.getInstance();
    }


    private void setRemainingElements(int text, int color) {
        binding.remainingDaysDecision.setText(text);
        setColor(color);
    }

    private void setColor(int color) {
        binding.dateWhenWasPaid.setTextColor(requireContext().getColor(color));
        binding.remainingDate.setTextColor(requireContext().getColor(color));
        binding.remainingDaysDecision.setTextColor(requireContext().getColor(color));
        binding.remainingDays.setTextColor(requireContext().getColor(color));
        binding.staticDaysText.setTextColor(requireContext().getColor(color));
    }

    private Drawable getCategoryIcon(Category category) {
        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        assert iconPack != null;
        return Objects.requireNonNull(iconPack.getIcon(category.getIcon())).getDrawable();
    }

    private Category getCategory(Transaction transaction) {
        int categoryId = transaction.getCategoryId();
        CategoryViewModel categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);
        return categoryViewModel.getCategoryById(categoryId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}