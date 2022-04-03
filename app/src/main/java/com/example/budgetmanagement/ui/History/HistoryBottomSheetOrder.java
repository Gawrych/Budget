package com.example.budgetmanagement.ui.History;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class HistoryBottomSheetOrder extends Fragment {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<HistoryAndTransaction>> actualHistoryAndTransactionList; // In second sorting must have all list not sorting
    private LiveData<List<HistoryAndTransaction>> sortedHistoryAndTransactionList;
    private CheckBox profitCheckBox;
    private CheckBox reversedCheckBox;
    private CheckBox filterByProfitCheckBox;
    private int[] lastFilters;


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

        lastFilters = new int[4];
        lastFilters[0] = 0;
        lastFilters[1] = 0;
        lastFilters[2] = 0;
        lastFilters[3] = 0;

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
        nameSort.setOnClickListener(v -> sortByName());

        assert amountSort != null;
        amountSort.setOnClickListener(v -> sortByAmount());

        assert dateSort != null;
        dateSort.setOnClickListener(v -> sortByDate());
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return bottomSheetDialog;
    }

    public void sort(LiveData<List<HistoryAndTransaction>> historyAndTransactionList) {
        this.sortedHistoryAndTransactionList = historyAndTransactionList;

        Log.d("CheckError", String.valueOf(lastFilters[0]));
        profitCheckBox.setChecked(lastFilters[0] == 1);
        filterByProfitCheckBox.setChecked(lastFilters[1] == 1);
        reversedCheckBox.setChecked(lastFilters[2] == 1);

        bottomSheetDialog.show();
    }

    public LiveData<List<HistoryAndTransaction>> getSortedList() {
        return sortedHistoryAndTransactionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFilter() {
        boolean profit = profitCheckBox.isChecked();
        boolean filterProfit = filterByProfitCheckBox.isChecked();

        if (profit && filterProfit) {
            sortedHistoryAndTransactionList = Transformations.map(sortedHistoryAndTransactionList,
                    input -> input.stream().filter(o1 -> o1.transaction.getProfit()).collect(Collectors.toList()));
        } else if (!profit && filterProfit) {
            sortedHistoryAndTransactionList = Transformations.map(sortedHistoryAndTransactionList,
                    input -> input.stream().filter(o1 -> !o1.transaction.getProfit()).collect(Collectors.toList()));
        }

        lastFilters[0] = profit ? 1 : 0;
        lastFilters[1] = filterProfit ? 1 : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkReversed() {
        boolean reversed = reversedCheckBox.isChecked();
        lastFilters[2] = reversed ? 1 : 0;
        if (reversed) {
            sortedHistoryAndTransactionList = Transformations.map(sortedHistoryAndTransactionList,
                    input -> input.stream().collect(Collectors.collectingAndThen(Collectors.toList(), l -> {Collections.reverse(l); return l;})));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByName() {
        setFilter();
        sortedHistoryAndTransactionList = Transformations.map(sortedHistoryAndTransactionList,
                input -> input.stream().sorted(Comparator.comparing(o -> o.transaction.getTitle())).collect(Collectors.toList())); // Fix this
        checkReversed();
        lastFilters[3] = 1;
        bottomSheetDialog.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByAmount() {
        setFilter();
        sortedHistoryAndTransactionList = Transformations.map(sortedHistoryAndTransactionList,
                input -> input.stream().sorted(Comparator.comparingDouble(o ->
                        o.transaction.getAmount())).collect(Collectors.toList()));
        checkReversed();
        lastFilters[3] = 2;
        bottomSheetDialog.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByDate() {
        setFilter();
        sortedHistoryAndTransactionList = Transformations.map(sortedHistoryAndTransactionList,
                input -> input.stream().sorted(Comparator.comparingLong(o ->
                        o.history.getAddDate())).collect(Collectors.toList()));
        checkReversed();
        lastFilters[3] = 3;
        bottomSheetDialog.cancel();
    }
}
