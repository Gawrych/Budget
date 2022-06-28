package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.History.ListSorting.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.History.ListSorting.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.History.ListSorting.SORT_BY_NAME_METHOD;

import android.content.Context;
import android.os.Build;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    private boolean isProfit;
    private boolean isBothProfitAndLoss;
    private boolean isLoss;
    private boolean sortingByReverse;

    private CheckBox reversedSortingCheckBox;
    private TextView reversedCheckboxTitle;
    private ConstraintLayout sortByName;
    private ConstraintLayout sortByAmount;
    private ConstraintLayout sortByDate;

    private ListSorting listSorting;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HistoryBottomSheetSorting(Context context) {
        initializeBottomSheetDialog(context);
        setContentViewOnBottomSheetDialog();
        initializeSortingButtons();

        reversedSortingCheckBox.setOnClickListener(v -> changeReversedCheckboxTitle());

        sortByName.setOnClickListener(v -> sortList(SORT_BY_NAME_METHOD));

        sortByAmount.setOnClickListener(v -> sortList(SORT_BY_AMOUNT_METHOD));

        sortByDate.setOnClickListener(v -> sortList(SORT_BY_DATE_METHOD));
    }

    private void initializeBottomSheetDialog(Context context) {
        bottomSheetDialog = new BottomSheetDialog(context);
    }

    private void setContentViewOnBottomSheetDialog() {
        bottomSheetDialog.setContentView(getLayoutForHistoryBottomSheetSortingDialog());
    }

    private int getLayoutForHistoryBottomSheetSortingDialog() {
        return R.layout.history_bottom_sheet_sorting;
    }

    private void initializeSortingButtons() {
        reversedSortingCheckBox = bottomSheetDialog.findViewById(getReversedSortingCheckboxId());
        reversedCheckboxTitle = bottomSheetDialog.findViewById(getReversedSortingCheckboxTitleId());
        sortByAmount = bottomSheetDialog.findViewById(getSortByAmountButtonId());
        sortByName = bottomSheetDialog.findViewById(getSortByTitleButtonId());
        sortByDate = bottomSheetDialog.findViewById(getSortByDateButtonId());
    }

    private int getReversedSortingCheckboxId() {
        return R.id.reversedCheck;
    }

    private int getReversedSortingCheckboxTitleId() {
        return R.id.reversedCheckTitle;
    }

    private int getSortByAmountButtonId() {
        return R.id.amountSort;
    }

    private int getSortByTitleButtonId() {
        return R.id.nameSort;
    }

    private int getSortByDateButtonId() {
        return R.id.dateSort;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList(int selectedMethod) {
        initializeListSorting(selectedMethod);
        setProfit();
        initializeIfReversedSorting();
        listSorting.sort(profit, sortingByReverse);
        historyAndTransactionList = listSorting.getSortedList();
        bottomSheetDialog.cancel();
    }

    private void initializeListSorting(int selectedMethod) {
        listSorting = new ListSorting(historyAndTransactionList, selectedMethod);
    }

    private void setProfit() {
        isProfit = getSelectedProfitOrLossIconId() == getProfitIconId();
        isBothProfitAndLoss = getSelectedProfitOrLossIconId() == getProfitAndLossIconId();
        isLoss = getSelectedProfitOrLossIconId() == getLossIconId();
        setProfitNumber();
    }

    private void setProfitNumber() {
        if (isProfit) {
            profit = 1;
        } else if (isBothProfitAndLoss) {
            profit = 0;
        } else if (isLoss) {
            profit = -1;
        }
    }

    private int getSelectedProfitOrLossIconId() {
        return getRadioGroup().getCheckedRadioButtonId();
    }

    private RadioGroup getRadioGroup() {
        return bottomSheetDialog.findViewById(getRadioGroupId());
    }

    private int getRadioGroupId() {
        return R.id.profitFilter;
    }

    private int getProfitIconId() {
        return R.id.profit;
    }

    private int getProfitAndLossIconId() {
        return R.id.profitAndLoss;
    }

    private int getLossIconId() {
        return R.id.loss;
    }

    private void initializeIfReversedSorting() {
        sortingByReverse = checkIfCheckboxIsChecked(reversedSortingCheckBox);
    }

    private boolean checkIfCheckboxIsChecked(CheckBox checkBox) {
        return checkBox.isChecked();
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
        getRadioGroup().check(getProfitAndLossIconId());
        reversedSortingCheckBox.setChecked(false);
        changeTextOnLowToHigh();
    }

    public LiveData<List<HistoryAndTransaction>> getSortedList() {
        return historyAndTransactionList;
    }

    private void changeReversedCheckboxTitle() {
        if (checkIfCheckboxIsChecked(reversedSortingCheckBox)) {
            changeTextOnHighToLow();
        } else {
            changeTextOnLowToHigh();
        }
    }

    private void changeTextOnHighToLow() {
        reversedCheckboxTitle.setText(getHighToLowStringId());
    }

    private int getHighToLowStringId() {
        return R.string.high_to_low;
    }

    private void changeTextOnLowToHigh() {
        reversedCheckboxTitle.setText(getLowToHighStringId());
    }

    private int getLowToHighStringId() {
        return R.string.low_to_high;
    }

}
