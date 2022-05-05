package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.History.ListSorting.AMOUNT_SORT_METHOD;
import static com.example.budgetmanagement.ui.History.ListSorting.DATE_SORT_METHOD;
import static com.example.budgetmanagement.ui.History.ListSorting.NAME_SORT_METHOD;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.HistoryBottomSheetAdapter;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;


public class HistoryBottomSheetOrder extends Fragment {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;
    private CheckBox profitCheckBox;
    private CheckBox reversedCheckBox;
    private CheckBox filterByProfitCheckBox;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryBottomSheetOrder(Context context) {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.history_bottom_sheet_sorting);

        ConstraintLayout amountSort = bottomSheetDialog.findViewById(R.id.amountSort);
        ConstraintLayout nameSort = bottomSheetDialog.findViewById(R.id.nameSort);
        ConstraintLayout dateSort = bottomSheetDialog.findViewById(R.id.dateSort);

        profitCheckBox = bottomSheetDialog.findViewById(R.id.profitCheck);
        reversedCheckBox = bottomSheetDialog.findViewById(R.id.reversedCheck);
        filterByProfitCheckBox = bottomSheetDialog.findViewById(R.id.filterByProfit);

//        Change this, this show and hide button to filter by profit
        assert filterByProfitCheckBox != null;
        filterByProfitCheckBox.setOnClickListener(v -> {
            if (filterByProfitCheckBox.isChecked()) {
                profitCheckBox.setVisibility(View.VISIBLE);
            } else {
                profitCheckBox.setVisibility(View.INVISIBLE);
            }
        });

        assert nameSort != null;
        nameSort.setOnClickListener(v -> sortList(NAME_SORT_METHOD));

        assert amountSort != null;
        amountSort.setOnClickListener(v -> sortList(AMOUNT_SORT_METHOD));

        assert dateSort != null;
        dateSort.setOnClickListener(v -> sortList(DATE_SORT_METHOD));
    }

    public void setListToSort(LiveData<List<HistoryAndTransaction>> historyAndTransactionList) {
        this.historyAndTransactionList = historyAndTransactionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList(int sortMethod) {
        ListSorting listSorting = new ListSorting(historyAndTransactionList);
        listSorting.sort(profitCheckBox, filterByProfitCheckBox, reversedCheckBox, sortMethod);
        historyAndTransactionList = listSorting.getSortedList();
        bottomSheetDialog.cancel();
    }

    public void show() {
        bottomSheetDialog.show();
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return bottomSheetDialog;
    }

    public LiveData<List<HistoryAndTransaction>> getSortedList() {
        return historyAndTransactionList;
    }
}
