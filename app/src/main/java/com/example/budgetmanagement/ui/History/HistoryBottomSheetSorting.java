package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.History.ListSorting.AMOUNT_SORT_METHOD;
import static com.example.budgetmanagement.ui.History.ListSorting.DATE_SORT_METHOD;
import static com.example.budgetmanagement.ui.History.ListSorting.NAME_SORT_METHOD;

import android.content.Context;
import android.os.Build;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class HistoryBottomSheetSorting extends Fragment {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;
    private int profit;
    private CheckBox reversedCheckBox;
    private boolean profitTrue;
    private boolean profitAll;
    private boolean profitFalse;
    private boolean reversedSorting;

    private ConstraintLayout amountSort;
    private ConstraintLayout nameSort;
    private ConstraintLayout dateSort;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryBottomSheetSorting(Context context) {
        prepareBottomSheetDialog(context);
        initializeSortingButtons();

        assert nameSort != null;
        nameSort.setOnClickListener(v -> sortList(NAME_SORT_METHOD));

        assert amountSort != null;
        amountSort.setOnClickListener(v -> sortList(AMOUNT_SORT_METHOD));

        assert dateSort != null;
        dateSort.setOnClickListener(v -> sortList(DATE_SORT_METHOD));
    }

    private void prepareBottomSheetDialog(Context context) {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.history_bottom_sheet_sorting);
    }

    private void initializeSortingButtons() {
        reversedCheckBox = bottomSheetDialog.findViewById(R.id.reversedCheck);
        amountSort = bottomSheetDialog.findViewById(R.id.amountSort);
        nameSort = bottomSheetDialog.findViewById(R.id.nameSort);
        dateSort = bottomSheetDialog.findViewById(R.id.dateSort);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList(int sortMethod) {
        ListSorting listSorting = new ListSorting(historyAndTransactionList);
        setProfit();
        setReverseSortingCheckBox();
        listSorting.sort(profit, reversedSorting, sortMethod);
        historyAndTransactionList = listSorting.getSortedList();
        bottomSheetDialog.cancel();
    }

    private void setProfit() {
        profitTrue = getSelectedProfitIconId() == getProfitTrueIconId();
        profitAll = getSelectedProfitIconId() == getProfitAllIconId();
        profitFalse = getSelectedProfitIconId() == getProfitFalseIconId();
        setProfitNumber();
    }

    private void setProfitNumber() {
        if (profitTrue) {
            profit = 1;
        } else if (profitAll) {
            profit = 0;
        } else if (profitFalse) {
            profit = -1;
        }
    }

    private int getSelectedProfitIconId() {
        return getRadioGroup().getCheckedRadioButtonId();
    }

    private RadioGroup getRadioGroup() {
        return bottomSheetDialog.findViewById(getRadioGroupId());
    }

    private int getRadioGroupId() {
        return R.id.profitFilter;
    }

    private int getProfitTrueIconId() {
        return R.id.profitTrue;
    }

    private int getProfitAllIconId() {
        return R.id.profitAll;
    }

    private int getProfitFalseIconId() {
        return R.id.profitFalse;
    }

    private void setReverseSortingCheckBox() {
        reversedSorting = reversedCheckBox.isChecked();
    }

    public void show() {
        resetButtonsToDefault();
        bottomSheetDialog.show();
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return bottomSheetDialog;
    }

    public void setListToSort(LiveData<List<HistoryAndTransaction>> historyAndTransactionList) {
        this.historyAndTransactionList = historyAndTransactionList;
    }

    public void resetButtonsToDefault() {
        getRadioGroup().check(getProfitAllIconId());
        reversedCheckBox.setChecked(false);
    }

    public LiveData<List<HistoryAndTransaction>> getSortedList() {
        return historyAndTransactionList;
    }
}
