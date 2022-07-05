//package com.example.budgetmanagement.ui.History;
//
//import static com.example.budgetmanagement.ui.utils.ListSorting.SORT_BY_AMOUNT_METHOD;
//import static com.example.budgetmanagement.ui.utils.ListSorting.SORT_BY_DATE_METHOD;
//import static com.example.budgetmanagement.ui.utils.ListSorting.SORT_BY_NAME_METHOD;
//
//import android.content.Context;
//import android.os.Build;
//import android.widget.CheckBox;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import androidx.annotation.RequiresApi;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.LiveData;
//
//import com.example.budgetmanagement.R;
//import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
//import com.example.budgetmanagement.ui.utils.ListSorting;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//
//import java.util.List;
//
//public class HistoryBottomSheetSorting extends Fragment {
//
//    private BottomSheetDialog bottomSheetDialog;
//    private LiveData<List<HistoryAndTransaction>> historyAndTransactionList;
//    private int profit;
//    private boolean isProfit;
//    private boolean isBothProfitAndLoss;
//    private boolean isLoss;
//    private boolean sortingByReverse;
//
//    private CheckBox reversedSortingCheckBox;
//    private TextView reversedCheckboxTitle;
//    private ConstraintLayout sortByName;
//    private ConstraintLayout sortByAmount;
//    private ConstraintLayout sortByDate;
//
//    private int iconResourceId;
//    private int profitIconResourceId;
//
//    private ListSorting listSorting;
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public HistoryBottomSheetSorting(Context context) {
//        initializeBottomSheetDialog(context);
//        setContentViewOnBottomSheetDialog();
//        initializeSortingButtons();
//
//        reversedSortingCheckBox.setOnClickListener(v -> changeReversedCheckboxTitle());
//
//        sortByName.setOnClickListener(v -> {
//            sortList(SORT_BY_NAME_METHOD);
//            iconResourceId = R.drawable.block;
//        });
//
//        sortByAmount.setOnClickListener(v -> {
//            sortList(SORT_BY_AMOUNT_METHOD);
//            iconResourceId = R.drawable.dollar_coin;
//        });
//
//        sortByDate.setOnClickListener(v -> {
//            sortList(SORT_BY_DATE_METHOD);
//            iconResourceId = R.drawable.calendar;
//        });
//    }
//
//    private void initializeBottomSheetDialog(Context context) {
//        bottomSheetDialog = new BottomSheetDialog(context);
//    }
//
//    private void setContentViewOnBottomSheetDialog() {
//        bottomSheetDialog.setContentView(getLayoutForHistoryBottomSheetSortingDialog());
//    }
//
//    private int getLayoutForHistoryBottomSheetSortingDialog() {
//        return R.layout.history_bottom_sheet_sorting;
//    }
//
//    private void initializeSortingButtons() {
//        reversedSortingCheckBox = bottomSheetDialog.findViewById(getReversedSortingCheckboxId());
//        reversedCheckboxTitle = bottomSheetDialog.findViewById(getReversedSortingCheckboxTitleId());
//        sortByAmount = bottomSheetDialog.findViewById(getSortByAmountButtonId());
//        sortByName = bottomSheetDialog.findViewById(getSortByTitleButtonId());
//        sortByDate = bottomSheetDialog.findViewById(getSortByDateButtonId());
//    }
//
//    private int getReversedSortingCheckboxId() {
//        return R.id.reversedCheck;
//    }
//
//    private int getReversedSortingCheckboxTitleId() {
//        return R.id.reversedCheckTitle;
//    }
//
//    private int getSortByAmountButtonId() {
//        return R.id.sortByAmountLayout;
//    }
//
//    private int getSortByTitleButtonId() {
//        return R.id.sortByNameLayout;
//    }
//
//    private int getSortByDateButtonId() {
//        return R.id.sortByDateLayout;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void sortList(int selectedMethod) {
//        initializeListSorting(selectedMethod);
//        setProfit();
//        initializeIfReversedSorting();
//        listSorting.sort(profit, sortingByReverse);
//        historyAndTransactionList = listSorting.getSortedList();
//        bottomSheetDialog.cancel();
//    }
//
//    private void initializeListSorting(int selectedMethod) {
//        listSorting = new ListSorting(historyAndTransactionList, selectedMethod);
//    }
//
//    private void setProfit() {
//        isProfit = getSelectedProfitOrLossIconId() == getProfitIconId();
//        isBothProfitAndLoss = getSelectedProfitOrLossIconId() == getProfitAndLossIconId();
//        isLoss = getSelectedProfitOrLossIconId() == getLossIconId();
//        setProfitNumber();
//    }
//
//    private void setProfitNumber() {
//        if (isProfit) {
//            profit = 1;
//            profitIconResourceId = R.drawable.profit_icon;
//        } else if (isBothProfitAndLoss) {
//            profit = 0;
//            profitIconResourceId = R.drawable.profit_and_loss_icon;
//        } else if (isLoss) {
//            profit = -1;
//            profitIconResourceId = R.drawable.loss_icon;
//        }
//    }
//
//    private int getSelectedProfitOrLossIconId() {
//        return getRadioGroup().getCheckedRadioButtonId();
//    }
//
//    private RadioGroup getRadioGroup() {
//        return bottomSheetDialog.findViewById(getRadioGroupId());
//    }
//
//    private int getRadioGroupId() {
//        return R.id.profitFilter;
//    }
//
//    private int getProfitIconId() {
//        return R.id.profitIcon;
//    }
//
//    private int getProfitAndLossIconId() {
//        return R.id.profitAndLossIcon;
//    }
//
//    private int getLossIconId() {
//        return R.id.lossIcon;
//    }
//
//    private void initializeIfReversedSorting() {
//        sortingByReverse = checkIfCheckboxIsChecked();
//    }
//
//    public boolean checkIfCheckboxIsChecked() {
//        return reversedSortingCheckBox.isChecked();
//    }
//
//    public void show() {
//        resetButtonsToDefault();
//        bottomSheetDialog.show();
//    }
//
//    public BottomSheetDialog getBottomSheetDialog() {
//        return bottomSheetDialog;
//    }
//
//    public void setListToSort(LiveData<List<HistoryAndTransaction>> historyAndTransactionList) {
//        this.historyAndTransactionList = historyAndTransactionList;
//    }
//
//    public void resetButtonsToDefault() {
//        getRadioGroup().check(getProfitAndLossIconId());
//        reversedSortingCheckBox.setChecked(false);
//        changeTextOnLowToHigh();
//    }
//
//    public LiveData<List<HistoryAndTransaction>> getSortedList() {
//        return historyAndTransactionList;
//    }
//
//    private void changeReversedCheckboxTitle() {
//        if (checkIfCheckboxIsChecked()) {
//            changeTextOnHighToLow();
//        } else {
//            changeTextOnLowToHigh();
//        }
//    }
//
//    private void changeTextOnHighToLow() {
//        reversedCheckboxTitle.setText(getHighToLowStringId());
//    }
//
//    private int getHighToLowStringId() {
//        return R.string.high_to_low;
//    }
//
//    private void changeTextOnLowToHigh() {
//        reversedCheckboxTitle.setText(getLowToHighStringId());
//    }
//
//    private int getLowToHighStringId() {
//        return R.string.low_to_high;
//    }
//
//    public int getIconResourceId() {
//        return iconResourceId;
//    }
//
//    public int getProfitIconResourceId() {
//        return profitIconResourceId;
//    }
//}
