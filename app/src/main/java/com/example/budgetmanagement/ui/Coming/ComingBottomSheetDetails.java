package com.example.budgetmanagement.ui.Coming;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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
    private ComingAndTransaction comingAndTransaction;
    private TransactionViewModel transactionViewModel;

    public ComingBottomSheetDetails(Context context, Activity activity, ViewModelStoreOwner owner) {
        this.context = context;
        this.activity = activity;

        this.comingViewModel = new ViewModelProvider(owner).get(ComingViewModel.class);
        this.historyViewModel = new ViewModelProvider(owner).get(HistoryViewModel.class);
        this.transactionViewModel = new ViewModelProvider(owner).get(TransactionViewModel.class);


        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        deleteButton = bottomSheetDialog.findViewById(R.id.delete);
        editButton = bottomSheetDialog.findViewById(R.id.edit);
        executeButton = bottomSheetDialog.findViewById(R.id.execute);
        moveButton = bottomSheetDialog.findViewById(R.id.move);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        if (isBeforeDeadline || isExecute) {
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
        }

        deleteButton.setOnClickListener(v -> deleteItem());

        editButton.setOnClickListener(v -> {});

        moveButton.setOnClickListener(v -> changeItemRepeatDate());

        executeButton.setOnClickListener(v -> executePay());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAddDate() {
        addDateField.setText(DateProcessor.parseDate(comingAndTransaction.coming.getAddDate()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setLastModifiedDate() {
        long modifiedDate = comingAndTransaction.coming.getModifiedDate();
        if (modifiedDate != 0) {
            lastEditDateField.setText(DateProcessor.parseDate(modifiedDate));
        } else {
            lastEditDateField.setText(R.string.Never);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    private void changeItemRepeatDate() {
        final Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(comingAndTransaction.coming.getRepeatDate());
        int mYear = calendarInstance.get(Calendar.YEAR);
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        Calendar newDateCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    newDateCalendar.set(year, monthOfYear, dayOfMonth);
                    comingAndTransaction.coming.setRepeatDate(newDateCalendar.getTimeInMillis());

                    updateComingInDatabase(comingAndTransaction);

                    bottomSheetDialog.cancel();
                    Toast.makeText(context, "Przełożono", Toast.LENGTH_SHORT).show();

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
        setRemainingDaysIconToCalendar();
        setRemainingDaysLabelField(text);
    }

    private void setColor(int color) {
        remainingDaysIconField.setColorFilter(context.getResources().getColor(color));
        deadlineDateField.setTextColor(context.getResources().getColor(color));
        remainingDaysLabelField.setTextColor(context.getResources().getColor(color));
        remainingDaysField.setTextColor(context.getResources().getColor(color));
        daysLabelField.setTextColor(context.getResources().getColor(color));
    }

    private void setRemainingDaysIconToCalendar() {
        remainingDaysIconField.setImageResource(R.drawable.ic_baseline_event_busy_24);
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
        executeButton.setBackgroundColor(context.getResources().getColor(color));
    }

    private void removeFromDatabase(ComingAndTransaction comingAndTransaction) {
        Log.d("ErrorHandle", "RemoveFromDatabase: " + comingAndTransaction.coming.getComingId());
        comingViewModel.delete(comingAndTransaction.coming.getComingId());
    }

    private void updateComingInDatabase(ComingAndTransaction comingAndTransaction) {
        comingViewModel.update(comingAndTransaction.coming);
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
