package com.example.budgetmanagement.ui.Coming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingExpandableListAdapter;
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
    private ComingViewModel comingViewModel;
    private TextView transactionName;
    private TextView addDate;
    private TextView remainingDays;
    private TextView remainingDaysLabel;
    private TextView daysLabel;
    private TextView lastEditDate;
    private TextView validity;
    private ImageView remainingDaysIcon;
    private Context context;
    private int categoryId = -1;
    private Button delete;
    private Activity activity;
    private Button edit;
    private Button execute;

    public ComingBottomSheetDetails(Context context, Activity activity, ComingViewModel comingViewModel) {
        this.context = context;
        this.activity = activity;
        this.comingViewModel = comingViewModel;

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        delete = bottomSheetDialog.findViewById(R.id.delete);
        edit = bottomSheetDialog.findViewById(R.id.edit);
        execute = bottomSheetDialog.findViewById(R.id.execute);

        transactionName = bottomSheetDialog.findViewById(R.id.transactionName);
        addDate = bottomSheetDialog.findViewById(R.id.addDate);
        remainingDaysLabel = bottomSheetDialog.findViewById(R.id.remainingDaysLabel);
        remainingDays = bottomSheetDialog.findViewById(R.id.remainingDays);
        daysLabel = bottomSheetDialog.findViewById(R.id.daysLabel);
        lastEditDate = bottomSheetDialog.findViewById(R.id.lastEditDate);
        validity = bottomSheetDialog.findViewById(R.id.validity);
        remainingDaysIcon = bottomSheetDialog.findViewById(R.id.remainingDaysIcon);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(ComingAndTransaction comingAndTransaction, ExpandableListView expandableListView, ComingExpandableListAdapter expandableListAdapter, int groupPosition, int childPosition) {
        transactionName.setText(comingAndTransaction.transaction.getTitle());
        addDate.setText(DateProcessor.parseDate(comingAndTransaction.coming.getAddDate()));

        byte validityValue = comingAndTransaction.coming.getValidity();
        if (validityValue == 2) {
            validity.setText("Średnia");
        }

        long modifiedDate = comingAndTransaction.coming.getModifiedDate();
        if (modifiedDate != 0) {
            lastEditDate.setText(DateProcessor.parseDate(modifiedDate));
        } else {
            lastEditDate.setText("Nigdy");
        }

        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(comingAndTransaction.coming.getRepeatDate());

        boolean isExecute = comingAndTransaction.coming.isExecute();

        int days = Math.abs(todayDate.get(Calendar.DAY_OF_YEAR) - otherDate.get(Calendar.DAY_OF_YEAR));
        remainingDays.setText(String.valueOf(days));

        if (otherDate.after(todayDate) || isExecute) {
            remainingDaysLabel.setText("Pozostało");
            remainingDaysIcon.setImageResource(R.color.white);
            remainingDaysLabel.setTextColor(context.getResources().getColor(R.color.font_default));
            remainingDays.setTextColor(context.getResources().getColor(R.color.font_default));
            daysLabel.setTextColor(context.getResources().getColor(R.color.font_default));
            remainingDays.setTextColor(context.getResources().getColor(R.color.font_default));
        } else {
            remainingDaysLabel.setText("Po terminie");
            remainingDaysIcon.setImageResource(R.drawable.ic_baseline_event_busy_24);
            remainingDaysLabel.setTextColor(context.getResources().getColor(R.color.mat_red));
            remainingDays.setTextColor(context.getResources().getColor(R.color.mat_red));
            daysLabel.setTextColor(context.getResources().getColor(R.color.mat_red));
            remainingDays.setTextColor(context.getResources().getColor(R.color.mat_red));
        }

        if (isExecute) {
            remainingDaysLabel.setText("Opłacone od");
            remainingDaysIcon.setImageResource(R.drawable.ic_baseline_done_all_24);
            Calendar executedDate = Calendar.getInstance();
            executedDate.setTimeInMillis(comingAndTransaction.coming.getExecutedDate());
            remainingDays.setText(String.valueOf(todayDate.get(Calendar.DAY_OF_YEAR) - executedDate.get(Calendar.DAY_OF_YEAR)));
        }

        delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.are_you_sure_to_delete)
                    .setPositiveButton(R.string.delete, (dialog, id) -> {
                        comingViewModel.delete(comingAndTransaction.coming.getComingId());
                        Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT).show();
                        expandableListAdapter.removeItem(groupPosition, comingAndTransaction);
                        expandableListAdapter.notifyDataSetChanged();
                        expandableListView.deferNotifyDataSetChanged();
                        bottomSheetDialog.cancel();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {}).show();
        });

        edit.setOnClickListener(v ->{});

        execute.setOnClickListener(v ->{});
    }

    private long getTodayDateInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
