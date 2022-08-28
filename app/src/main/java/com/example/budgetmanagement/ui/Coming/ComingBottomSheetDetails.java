package com.example.budgetmanagement.ui.Coming;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

public class ComingBottomSheetDetails extends Fragment {

    private final Activity activity;
    private final Context context;
    private final BottomSheetDialog bottomSheetDialog;
    private final ComingViewModel comingViewModel;
    private final TextView transactionName;
    private final TextView addDate;
    private final TextView remainingDays;
    private final TextView remainingDaysLabel;
    private final TextView daysLabel;
    private final TextView lastEditDate;
    private final TextView validity;
    private final TextView remainingDate;
    private final ImageView remainingDaysIcon;
    private final Button delete;
    private final Button edit;
    private final Button execute;
    private final Button move;
    private ComingAndTransaction comingAndTransaction;

    public ComingBottomSheetDetails(Context context, Activity activity, ComingViewModel comingViewModel, FragmentManager fragmentManager) {
        this.context = context;
        this.activity = activity;
        this.comingViewModel = comingViewModel;

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        delete = bottomSheetDialog.findViewById(R.id.delete);
        edit = bottomSheetDialog.findViewById(R.id.edit);
        execute = bottomSheetDialog.findViewById(R.id.execute);
        move = bottomSheetDialog.findViewById(R.id.move);

        transactionName = bottomSheetDialog.findViewById(R.id.transactionName);
        addDate = bottomSheetDialog.findViewById(R.id.addDate);
        remainingDaysLabel = bottomSheetDialog.findViewById(R.id.remainingDaysLabel);
        remainingDays = bottomSheetDialog.findViewById(R.id.remainingDays);
        daysLabel = bottomSheetDialog.findViewById(R.id.daysLabel);
        lastEditDate = bottomSheetDialog.findViewById(R.id.lastEditDate);
        validity = bottomSheetDialog.findViewById(R.id.validity);
        remainingDaysIcon = bottomSheetDialog.findViewById(R.id.remainingDaysIcon);
        remainingDate = bottomSheetDialog.findViewById(R.id.remainingDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(ComingAndTransaction comingAndTransaction) {
        this.comingAndTransaction = comingAndTransaction;
        transactionName.setText(comingAndTransaction.transaction.getTitle());

        byte validityValue = comingAndTransaction.coming.getValidity();
        if (validityValue == 2) {
            validity.setText("Średnia");
        }

        addDate.setText(DateProcessor.parseDate(comingAndTransaction.coming.getAddDate()));

        long modifiedDate = comingAndTransaction.coming.getModifiedDate();
        if (modifiedDate != 0) {
            lastEditDate.setText(DateProcessor.parseDate(modifiedDate));
        } else {
            lastEditDate.setText(R.string.Never);
        }

        Calendar todayDate = Calendar.getInstance();
        Calendar deadlineDate = Calendar.getInstance();
        deadlineDate.setTimeInMillis(comingAndTransaction.coming.getRepeatDate());

        int days = Math.abs(todayDate.get(Calendar.DAY_OF_YEAR) - deadlineDate.get(Calendar.DAY_OF_YEAR));
        remainingDate.setText(DateProcessor.parseDate(comingAndTransaction.coming.getRepeatDate()));
        remainingDays.setText(String.valueOf(days));

        setExecuteButton(R.string.Pay, R.color.mat_green);

        boolean isExecute = comingAndTransaction.coming.isExecute();
        boolean beforeDeadline = deadlineDate.after(todayDate);

        if (beforeDeadline || isExecute) {
            setRemainingElements(R.string.Remain, R.color.font_default);
        } else {
            setRemainingElements(R.string.after_the_deadline, R.color.mat_red);
        }

        if (isExecute) {
            long executedDateInMillis = comingAndTransaction.coming.getExecutedDate();
            Calendar executedDate = Calendar.getInstance();
            executedDate.setTimeInMillis(executedDateInMillis);

            remainingDate.setText(DateProcessor.parseDate(executedDateInMillis));
            remainingDays.setText(String.valueOf(todayDate.get(Calendar.DAY_OF_YEAR) - executedDate.get(Calendar.DAY_OF_YEAR)));

            setExecuteButton(R.string.cancel_pay, R.color.dark_grey);
            setRemainingElements(R.string.paid_from, R.color.main_green);
        }

        delete.setOnClickListener(v -> deleteItem());

        edit.setOnClickListener(v -> {});

        move.setOnClickListener(v -> changeItemRepeatDate());

        execute.setOnClickListener(v -> executePay());
    }

    private void executePay() {
        comingAndTransaction.coming.setExecute(!comingAndTransaction.coming.isExecute());
        comingAndTransaction.coming.setExecutedDate(Calendar.getInstance().getTimeInMillis());
        updateInDatabase(comingAndTransaction);
        bottomSheetDialog.cancel();
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

                    updateInDatabase(comingAndTransaction);

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
        setRemainingDaysLabel(text);
    }

    private void setColor(int color) {
        remainingDaysIcon.setColorFilter(context.getResources().getColor(color));
        remainingDate.setTextColor(context.getResources().getColor(color));
        remainingDaysLabel.setTextColor(context.getResources().getColor(color));
        remainingDays.setTextColor(context.getResources().getColor(color));
        daysLabel.setTextColor(context.getResources().getColor(color));
    }

    private void setRemainingDaysIconToCalendar() {
        remainingDaysIcon.setImageResource(R.drawable.ic_baseline_event_busy_24);
    }

    private void setRemainingDaysLabel(int text) {
        remainingDaysLabel.setText(text);
    }

    private void setExecuteButton(int text, int color) {
        setExecuteButtonText(text);
        setExecuteButtonColor(color);
    }
    
    private void setExecuteButtonText(int text) {
        execute.setText(text);
    }

    private void setExecuteButtonColor(int color) {
        execute.setBackgroundColor(context.getResources().getColor(color));
    }

    private void removeFromDatabase(ComingAndTransaction comingAndTransaction) {
        comingViewModel.delete(comingAndTransaction.coming);
    }

    private void updateInDatabase(ComingAndTransaction comingAndTransaction) {
        comingViewModel.update(comingAndTransaction.coming);
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
