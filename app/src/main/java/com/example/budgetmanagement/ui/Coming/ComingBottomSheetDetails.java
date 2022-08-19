package com.example.budgetmanagement.ui.Coming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetEntity;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;

public class ComingBottomSheetDetails extends Fragment {

    private List<CategoryBottomSheetEntity> categoryBottomSheetEntity;
    private BottomSheetDialog bottomSheetDialog;
    private TextView transactionName;
    private TextView repeatDate;
    private TextView remainingDays;
    private TextView remainingDaysLabel;
    private TextView outOfDateLabel;
    private TextView daysLabel;
    private ImageView remainingDaysIcon;
    private Context context;

    public ComingBottomSheetDetails(Context context, Activity activity, ComingViewModel comingViewModel) {
        this.context = context;
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        Button delete = bottomSheetDialog.findViewById(R.id.delete);
        assert delete != null;
        delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.are_you_sure_to_delete)
                    .setPositiveButton(R.string.delete, (dialog, id) -> {
//                        comingViewModel.delete(historyId);
                        bottomSheetDialog.cancel();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {}).show();
                });

        Button edit = bottomSheetDialog.findViewById(R.id.edit);
        assert edit != null;
        edit.setOnClickListener(v ->{});


        transactionName = bottomSheetDialog.findViewById(R.id.transactionName);
        repeatDate = bottomSheetDialog.findViewById(R.id.repeatDate);
        remainingDaysLabel = bottomSheetDialog.findViewById(R.id.remainingDaysLabel);
        remainingDays = bottomSheetDialog.findViewById(R.id.remainingDays);
        outOfDateLabel = bottomSheetDialog.findViewById(R.id.outOfDateLabel);
        daysLabel = bottomSheetDialog.findViewById(R.id.daysLabel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(ComingAndTransaction comingAndTransaction) {
        transactionName.setText(comingAndTransaction.transaction.getTitle());
        repeatDate.setText(DateProcessor.getDate(comingAndTransaction.coming.getRepeatDate()));

        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(comingAndTransaction.coming.getRepeatDate());


        int days = Math.abs(todayDate.get(Calendar.DAY_OF_YEAR) - otherDate.get(Calendar.DAY_OF_YEAR));
        remainingDays.setText(String.valueOf(days));

        if (otherDate.after(todayDate)) {
            outOfDateLabel.setVisibility(View.GONE);
            remainingDays.setTextColor(context.getResources().getColor(R.color.font_default));
            daysLabel.setTextColor(context.getResources().getColor(R.color.font_default));
            remainingDays.setTextColor(context.getResources().getColor(R.color.font_default));
        } else {
            outOfDateLabel.setVisibility(View.VISIBLE);
            remainingDays.setTextColor(context.getResources().getColor(R.color.mat_red));
            daysLabel.setTextColor(context.getResources().getColor(R.color.mat_red));
            remainingDays.setTextColor(context.getResources().getColor(R.color.mat_red));
        }
    }

    private long getTodayDateInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
