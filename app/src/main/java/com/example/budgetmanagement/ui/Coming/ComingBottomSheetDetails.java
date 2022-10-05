package com.example.budgetmanagement.ui.Coming;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    private final TextView validityField;
    private final TextView deadlineDateField;
    private final ImageView remainingDaysIconField;
    private final Button deleteButton;
    private final Button editButton;
    private final Button executeButton;
    private final Button moveButton;
    private final View root;
    private ComingAndTransaction comingAndTransaction;

    public ComingBottomSheetDetails(Context context, Activity activity, ViewModelStoreOwner owner, View root) {
        this.context = context;
        this.activity = activity;
        this.root = root;

        this.comingViewModel = new ViewModelProvider(owner).get(ComingViewModel.class);
        this.historyViewModel = new ViewModelProvider(owner).get(HistoryViewModel.class);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        deleteButton = bottomSheetDialog.findViewById(R.id.delete);
        editButton = bottomSheetDialog.findViewById(R.id.edit);
        executeButton = bottomSheetDialog.findViewById(R.id.execute);
        moveButton = bottomSheetDialog.findViewById(R.id.createNewByThisPattern);

        transactionNameField = bottomSheetDialog.findViewById(R.id.transactionName);
        addDateField = bottomSheetDialog.findViewById(R.id.addDate);
        remainingDaysLabelField = bottomSheetDialog.findViewById(R.id.remainingDaysLabel);
        remainingDaysField = bottomSheetDialog.findViewById(R.id.remainingDays);
        daysLabelField = bottomSheetDialog.findViewById(R.id.daysLabel);
        lastEditDateField = bottomSheetDialog.findViewById(R.id.lastEditDate);
        validityField = bottomSheetDialog.findViewById(R.id.validity);
        remainingDaysIconField = bottomSheetDialog.findViewById(R.id.remainingDaysIcon);
        deadlineDateField = bottomSheetDialog.findViewById(R.id.remainingDate);
    }

    public void setData(ComingAndTransaction comingAndTransaction) {
        this.comingAndTransaction = comingAndTransaction;
        setTransactionName();
        setValidityValue();
        setAddDate();
        setLastModifiedDate();
        setExecuteButton(R.string.Pay, R.color.mat_green);

        Calendar todayDate = getTodayDate();
        Calendar deadlineDate = getCalendarWithValue(getRepeatDate());

        int remainingDays = Math.abs(getRemainingDays(todayDate, deadlineDate));
        setDeadlineDateField(getRepeatDate());
        setRemainingDaysField(remainingDays);

        boolean isExecute = comingAndTransaction.coming.isExecute();
        boolean isBeforeDeadline = deadlineDate.after(todayDate);
        remainingDaysIconField.setImageResource(R.drawable.calendar_smaller_icon);

        if (isBeforeDeadline) {
            setRemainingElements(R.string.Remain, R.color.font_default);
        } else {
            setRemainingElements(R.string.after_the_deadline, R.color.mat_red);
        }

        if (isExecute) {
            long executedDateInMillis = comingAndTransaction.coming.getExecutedDate();

            setDeadlineDateField(executedDateInMillis);
            setRemainingDaysField(getRemainingDays(todayDate, getCalendarWithValue(executedDateInMillis)));

            setExecuteButton(R.string.cancel_pay, R.color.dark_grey);
            setRemainingElements(R.string.paid_from, R.color.main_green);
            remainingDaysIconField.setImageResource(R.drawable.ic_baseline_done_all_24);
        }

        deleteButton.setOnClickListener(v -> deleteItem());

        editButton.setOnClickListener(v -> editSelectedElement());

        moveButton.setOnClickListener(v -> createNewByThisPattern());

        executeButton.setOnClickListener(v -> executePay());
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

    private void setValidityValue() {
        byte validityValue = comingAndTransaction.coming.getValidity();
        if (validityValue == 2) {
            validityField.setText("Średnia");
        }
    }

    private void setAddDate() {
        addDateField.setText(DateProcessor.parseDate(comingAndTransaction.coming.getAddDate()));
    }

    private void setLastModifiedDate() {
        long modifiedDate = comingAndTransaction.coming.getModifiedDate();
        if (modifiedDate != 0) {
            lastEditDateField.setText(DateProcessor.parseDate(modifiedDate));
        } else {
            lastEditDateField.setText(R.string.Never);
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
        remainingDaysIconField.setColorFilter(context.getColor(color));
        deadlineDateField.setTextColor(context.getColor(color));
        remainingDaysLabelField.setTextColor(context.getColor(color));
        remainingDaysField.setTextColor(context.getColor(color));
        daysLabelField.setTextColor(context.getColor(color));
    }

    private void setRemainingDaysLabelField(int text) {
        remainingDaysLabelField.setText(text);
    }

    private void setExecuteButton(int text, int color) {
        setExecuteButtonText(text);
        setExecuteButtonColor(color);
    }
    
    private void setExecuteButtonText(int text) {
        executeButton.setText(text);
    }

    private void setExecuteButtonColor(int color) {
        executeButton.setBackgroundColor(context.getColor(color));
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
