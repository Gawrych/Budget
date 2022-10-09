package com.example.budgetmanagement.ui.Coming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.util.Calendar;

public class ComingBottomSheetDetails extends Fragment {

    private final Activity activity;
    private final Context context;
    private final BottomSheetDialog bottomSheetDialog;
    private final HistoryViewModel historyViewModel;
    private final ComingViewModel comingViewModel;
    private final TextView transactionNameField;
    private final TextView addDateField;
    private final TextView remainingDaysField;
    private final TextView remainingDaysLabelField;
    private final TextView daysLabelField;
    private final TextView lastEditDateField;
    private final TextView deadlineDateField;
    private final TextView dateWhenWasPaid;
    private final LinearLayout deleteButton;
    private final LinearLayout editButton;
    private final LinearLayout executeButton;
    private final LinearLayout duplicateButton;
    private final View root;
    private final TextView amountField;
    private final CategoryViewModel categoryViewModel;
    private final TextView categoryName;
    private final ImageView categoryIcon;
    private final ImageView profitIcon;
    private final TextView executeLabel;
    private final ImageView executeIcon;
    private ComingAndTransaction comingAndTransaction;

    public ComingBottomSheetDetails(Context context, Activity activity, ViewModelStoreOwner owner, View root) {
        this.context = context;
        this.activity = activity;
        this.root = root;

        this.comingViewModel = new ViewModelProvider(owner).get(ComingViewModel.class);
        this.categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);
        this.historyViewModel = new ViewModelProvider(owner).get(HistoryViewModel.class);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        deleteButton = bottomSheetDialog.findViewById(R.id.deleteLayout);
        editButton = bottomSheetDialog.findViewById(R.id.editLayout);
        executeButton = bottomSheetDialog.findViewById(R.id.executeLayout);
        duplicateButton = bottomSheetDialog.findViewById(R.id.duplicateLayout);

        transactionNameField = bottomSheetDialog.findViewById(R.id.transactionName);
        addDateField = bottomSheetDialog.findViewById(R.id.addDate);
        remainingDaysLabelField = bottomSheetDialog.findViewById(R.id.remainingDaysLabel);
        remainingDaysField = bottomSheetDialog.findViewById(R.id.remainingDays);
        daysLabelField = bottomSheetDialog.findViewById(R.id.daysLabel);
        lastEditDateField = bottomSheetDialog.findViewById(R.id.lastEditDate);
        deadlineDateField = bottomSheetDialog.findViewById(R.id.remainingDate);
        dateWhenWasPaid = bottomSheetDialog.findViewById(R.id.dateWhenWasPaid);
        amountField = bottomSheetDialog.findViewById(R.id.amount);
        categoryName = bottomSheetDialog.findViewById(R.id.categoryName);
        categoryIcon = bottomSheetDialog.findViewById(R.id.categoryIcon);
        profitIcon = bottomSheetDialog.findViewById(R.id.profitIcon);
        executeLabel = bottomSheetDialog.findViewById(R.id.executeLabel);
        executeIcon = bottomSheetDialog.findViewById(R.id.executeIcon);
    }

    public void setData(ComingAndTransaction comingAndTransaction) {
        this.comingAndTransaction = comingAndTransaction;
        setTransactionName();
        setAmount();
        setAddDate();
        setLastModifiedDate();
        setExecuteButton(R.string.done, R.drawable.ic_baseline_done_all_24);
        setAmountIcon();

        int categoryId = comingAndTransaction.transaction.getCategoryId();
        Category category = categoryViewModel.getCategoryById(categoryId);

//        TODO: Change field in category table to store resourceId instead iconName
        String iconName = category.getIconName();
        int iconResId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        categoryIcon.setImageResource(iconResId);
        categoryName.setText(category.getName());

        Calendar todayDate = getTodayDate();
        Calendar deadlineDate = getCalendarWithValue(getRepeatDate());

        int remainingDays = Math.abs(getRemainingDays(todayDate, deadlineDate));
        setDeadlineDateField(getRepeatDate());
        setRemainingDaysField(remainingDays);

        boolean isExecute = comingAndTransaction.coming.isExecute();
        boolean isBeforeDeadline = deadlineDate.after(todayDate);

        dateWhenWasPaid.setVisibility(View.GONE);

        daysLabelField.setVisibility(View.VISIBLE);
        remainingDaysField.setVisibility(View.VISIBLE);

        if (isBeforeDeadline) {
            setRemainingElements(R.string.Remain, R.color.font_default);
        } else {
            setRemainingElements(R.string.after_the_deadline, R.color.mat_red);
        }

        if (isExecute) {
            setDeadlineDateField(comingAndTransaction.coming.getRepeatDate());

            dateWhenWasPaid.setVisibility(View.VISIBLE);
            dateWhenWasPaid.setText(DateProcessor.parseDate(comingAndTransaction.coming.getExecutedDate()));

            daysLabelField.setVisibility(View.GONE);
            remainingDaysField.setVisibility(View.GONE);

            setExecuteButton(R.string.cancel_completed, R.drawable.ic_baseline_cancel_24);
            setRemainingElements(R.string.completed, R.color.main_green);
        }

        deleteButton.setOnClickListener(v -> deleteItem());

        editButton.setOnClickListener(v -> editSelectedElement());

        duplicateButton.setOnClickListener(v -> createNewByThisPattern());

        executeButton.setOnClickListener(v -> executePay());
    }

    private void setAmountIcon() {
        BigDecimal bigDecimal = new BigDecimal(comingAndTransaction.transaction.getAmount());
        if (isNegative(bigDecimal)) {
            profitIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            profitIcon.setColorFilter(context.getColor(R.color.mat_red));
        } else {
            profitIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            profitIcon.setColorFilter(context.getColor(R.color.main_green));
        }
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private void createNewByThisPattern() {
        Bundle bundle = new Bundle();
        bundle.putInt("comingId", comingAndTransaction.coming.getComingId());

        Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_addNewComingElement, bundle);
        bottomSheetDialog.cancel();
    }

    private void editSelectedElement() {
        Bundle bundle = new Bundle();
        bundle.putInt("comingId", comingAndTransaction.coming.getComingId());

        Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_editComingElement, bundle);
        bottomSheetDialog.cancel();
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    private void setTransactionName() {
        transactionNameField.setText(comingAndTransaction.transaction.getTitle());
    }

    private void setAmount() {
        amountField.setText(comingAndTransaction.transaction.getAmount());
    }

    private void setAddDate() {
        addDateField.setText(DateProcessor.parseDate(comingAndTransaction.coming.getAddDate()));
    }

    private void setLastModifiedDate() {
        long modifiedDate = comingAndTransaction.coming.getModifiedDate();
        if (modifiedDate != 0) {
            lastEditDateField.setText(DateProcessor.parseDate(modifiedDate));
        } else {
            lastEditDateField.setText(R.string.never);
        }
    }

    private void setDeadlineDateField(long date) {
        deadlineDateField.setText(DateProcessor.parseDate(date));
    }

    private int getRemainingDays(Calendar today, Calendar deadLine) {
       return today.get(Calendar.DAY_OF_YEAR) - deadLine.get(Calendar.DAY_OF_YEAR);
    }

    private long getRepeatDate() {
        return comingAndTransaction.coming.getRepeatDate();
    }

    private void executePay() {
        boolean execute = !comingAndTransaction.coming.isExecute();

        comingAndTransaction.coming.setExecute(!comingAndTransaction.coming.isExecute());
        comingAndTransaction.coming.setExecutedDate(getTodayDate().getTimeInMillis());
        updateComingInDatabase(comingAndTransaction);

        int comingId = comingAndTransaction.coming.getComingId();
        History newHistoryElement = new History(0, comingId,
                comingAndTransaction.transaction.getTransactionId(),
                getTodayDate().getTimeInMillis());

        if (execute) {
            historyViewModel.insert(newHistoryElement);
        } else {
            historyViewModel.deleteByComingId(comingId);
        }

        bottomSheetDialog.cancel();
    }

    private Calendar getTodayDate() {
        return Calendar.getInstance();
    }

    private void setRemainingDaysField(int remainingDays) {
        remainingDaysField.setText(String.valueOf(remainingDays));
    }

    private void deleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    removeFromDatabase(comingAndTransaction);
                    bottomSheetDialog.cancel();
                    Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> {}).show();
    }

    private void setRemainingElements(int text, int color) {
        setColor(color);
        setRemainingDaysLabelField(text);
    }

    private void setColor(int color) {
        dateWhenWasPaid.setTextColor(context.getColor(color));
        deadlineDateField.setTextColor(context.getColor(color));
        remainingDaysLabelField.setTextColor(context.getColor(color));
        remainingDaysField.setTextColor(context.getColor(color));
        daysLabelField.setTextColor(context.getColor(color));
    }

    private void setRemainingDaysLabelField(int text) {
        remainingDaysLabelField.setText(text);
    }

    private void setExecuteButton(int text, int drawable) {
        setExecuteButtonText(text);
        setExecuteIcon(drawable);
    }
    
    private void setExecuteButtonText(int text) {
        executeLabel.setText(text);
    }

    private void setExecuteIcon(int drawable) {
        executeIcon.setImageResource(drawable);
    }

    private void removeFromDatabase(ComingAndTransaction comingAndTransaction) {
        Log.d("ErrorHandle", "RemoveFromDatabase: " + comingAndTransaction.transaction.getTitle());
        comingViewModel.delete(comingAndTransaction.coming);
    }

    private void updateComingInDatabase(ComingAndTransaction comingAndTransaction) {
        comingViewModel.update(comingAndTransaction.coming);
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
