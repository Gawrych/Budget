package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class HistoryBottomSheetDetails extends Fragment {

    private List<HistoryBottomSheetEntity> historyBottomSheetEntity;
    private BottomSheetDialog bottomSheetDialog;
    private int historyId;
    private TextView transactionName;
    private TextView categoryName;
    private TextView date;
    private ImageView categoryIcon;
    private Context context;

    public HistoryBottomSheetDetails(Context context, Activity activity, HistoryViewModel historyViewModel) {
        this.context = context;
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.history_bottom_sheet_details);

        historyBottomSheetEntity = historyViewModel.getHistoryBottomSheetEntityList();

        Button delete = bottomSheetDialog.findViewById(R.id.delete);
        assert delete != null;
        delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.AreYouSureToDelete)
                    .setPositiveButton(R.string.delete, (dialog, id) -> {
                        historyViewModel.delete(historyId);
                        bottomSheetDialog.cancel();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {}).show();
                });

        Button edit = bottomSheetDialog.findViewById(R.id.edit);
        assert edit != null;
        edit.setOnClickListener(v ->
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show());


        transactionName = bottomSheetDialog.findViewById(R.id.transactionName);
        categoryName = bottomSheetDialog.findViewById(R.id.categoryName);
        date = bottomSheetDialog.findViewById(R.id.lastModifiedDate);
        categoryIcon = bottomSheetDialog.findViewById(R.id.categoryIcon);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(HistoryAndTransaction historyAndTransaction) {
        this.historyId = historyAndTransaction.history.getHistoryId();
        int categoryId = historyAndTransaction.transaction.getCategoryId();

        int selectedId = historyBottomSheetEntity.get(0).getId();
        Toast.makeText(context, String.valueOf(selectedId), Toast.LENGTH_SHORT).show();

        Optional<HistoryBottomSheetEntity> result = historyBottomSheetEntity.stream().filter(v -> v.getId() == categoryId).findAny();
        result.ifPresent(bottomSheetEntity -> {
            transactionName.setText(historyAndTransaction.transaction.getTitle());
            categoryName.setText(bottomSheetEntity.getName());
            int resourceId = context.getResources().getIdentifier(bottomSheetEntity.getIconName(), "drawable", context.getPackageName());
            categoryIcon.setImageResource(resourceId);
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        date.setText(LocalDate.ofEpochDay(historyAndTransaction.transaction.getAddDate()).format(formatter));
//        Show recurring instead date
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
