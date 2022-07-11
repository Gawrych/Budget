package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.History.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.History.FilterFragment.REVERSE_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.utils.SortListByFilters.SORT_BY_NAME_METHOD;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.History.HistoryBottomSheetCategoryFilter;

import java.util.HashMap;

public class UIFiltersManager {

    private View root;
    private ConstraintLayout sortByNameLayout;
    private ConstraintLayout sortByAmountLayout;
    private ConstraintLayout sortByDateLayout;

    private ImageView sortByNameIconImageView;
    private ImageView sortByAmountIconImageView;
    private ImageView sortByDateIconImageView;
    private String selectedCategoryName;
    private HashMap<Integer, Integer> filters;

    private int sortMethod = 0;
    private RadioGroup radioGroup;
    private TextView reversedCheckboxTitle;
    private CheckBox reversedCheckBox;
    private Resources resources;
    private TextView categorySelector;
    private RadioButton profitRadio;
    private RadioButton profitAndLossRadio;
    private RadioButton lossRadio;
    private int selectedCategoryId = 0;
    private boolean selectedToSortByName = false;
    private boolean selectedToSortByAmount = false;
    private boolean selectedToSortByDate = false;
    private HistoryBottomSheetCategoryFilter categoryBottomSheetSelector;

    public UIFiltersManager(View root, HistoryBottomSheetCategoryFilter historyBottomSheetCategoryFilter) {
        this.root = root;
        this.resources = root.getResources();
        this.categoryBottomSheetSelector = historyBottomSheetCategoryFilter;

        sortByNameLayout = root.findViewById(R.id.sortByNameLayout);
        sortByAmountLayout = root.findViewById(R.id.sortByAmountLayout);
        sortByDateLayout = root.findViewById(R.id.sortByDateLayout);

        sortByNameIconImageView = root.findViewById(R.id.sortByNameIcon);
        sortByAmountIconImageView = root.findViewById(R.id.sortByAmountIcon);
        sortByDateIconImageView = root.findViewById(R.id.sortByDateIcon);
        categorySelector = root.findViewById(R.id.categorySelector);

        profitRadio = root.findViewById(R.id.profitIcon);
        profitAndLossRadio = root.findViewById(R.id.profitAndLossIcon);
        lossRadio = root.findViewById(R.id.lossIcon);

        radioGroup = root.findViewById(R.id.radioGroup);

        reversedCheckBox = root.findViewById(R.id.reversedCheck);
        reversedCheckboxTitle = root.findViewById(R.id.reversedCheckTitle);
    }

    public void selectOrderSorting(int sortMethod) {
        if (sortMethod == SORT_BY_NAME_METHOD) {
            selectedToSortByName = !selectedToSortByName;
            selectedToSortByAmount = false;
            selectedToSortByDate = false;
            clearOrderFilters();
            changeNameSortMethodRes(sortMethod);
        }
        if (sortMethod == SORT_BY_AMOUNT_METHOD) {
            selectedToSortByAmount = !selectedToSortByAmount;
            selectedToSortByName = false;
            selectedToSortByDate = false;
            clearOrderFilters();
            changeAmountSortMethodRes(sortMethod);
        }
        if (sortMethod == SORT_BY_DATE_METHOD) {
            selectedToSortByDate = !selectedToSortByDate;
            selectedToSortByName = false;
            selectedToSortByAmount = false;
            clearOrderFilters();
            changeDateSortMethodRes(sortMethod);
        }
    }

    private void changeNameSortMethodRes(int sortMethod) {
        if (selectedToSortByName) {
            this.sortMethod = sortMethod;
            setResources(sortByNameIconImageView, R.drawable.block_color);
            setColor(sortByNameLayout, R.color.very_light_gray);
        }
    }

    private void changeAmountSortMethodRes(int sortMethod) {
        if (selectedToSortByAmount) {
            this.sortMethod = sortMethod;
            setResources(sortByAmountIconImageView, R.drawable.dollar_coin_color);
            setColor(sortByAmountLayout, R.color.very_light_gray);
        }
    }

    private void changeDateSortMethodRes(int sortMethod) {
        if (selectedToSortByDate) {
            this.sortMethod = sortMethod;
            setResources(sortByDateIconImageView, R.drawable.calendar_color);
            setColor(sortByDateLayout, R.color.very_light_gray);
        }
    }

    private void clearOrderFilters() {
        this.sortMethod = 0;
        setResources(sortByNameIconImageView, R.drawable.block);
        setColor(sortByNameLayout, R.color.white);

        setResources(sortByAmountIconImageView, R.drawable.dollar_coin);
        setColor(sortByAmountLayout, R.color.white);

        setResources(sortByDateIconImageView, R.drawable.calendar);
        setColor(sortByDateLayout, R.color.white);
    }

    private void setResources(@NonNull ImageView view, int newRes) {
        view.setImageResource(newRes);
    }

    private void setColor(@NonNull ConstraintLayout layout, int newRes) {
        layout.setBackgroundColor(resources.getColor(newRes));
    }

    public void setDefaultValues() {
        clearGlobalVariable();
        clearOrderFilters();
        clearProfitFilter();
        clearReverseButton();
        clearCategorySelector();
    }

    private void clearGlobalVariable() {
        sortMethod = 0;
        selectedToSortByName = false;
        selectedToSortByAmount = false;
        selectedToSortByDate = false;
    }

    private void clearProfitFilter() {
        profitAndLossRadio.setChecked(true);
    }

    private void clearReverseButton() {
        reversedCheckBox.setChecked(false);
    }

    private void clearCategorySelector() {
        selectedCategoryId = 0;
        categorySelector.setText(R.string.select_category);
    }

    public int getSortMethod() {
        return sortMethod;
    }

    public void changeReversedCheckboxTitle() {
        if (checkIfCheckboxIsChecked()) {
            changeTextOnHighToLow();
        } else {
            changeTextOnLowToHigh();
        }
    }

    public boolean checkIfCheckboxIsChecked() {
        return reversedCheckBox.isChecked();
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

    public CheckBox getReverseCheckBox() {
        return reversedCheckBox;
    }

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public RadioButton getRadioProfitButton() {
        return profitRadio;
    }

    public RadioButton getRadioLossButton() {
        return lossRadio;
    }

    public void markUiFilters(HashMap<Integer, Integer> filters) {
        Integer value = filters.get(PROFIT_FILTER_ID);
        if (value != null && value == 1) {
            profitRadio.setChecked(true);
        } else if (value != null && value == 2) {
            lossRadio.setChecked(true);
        }

        value = filters.get(ORDER_FILTER_ID);
        if (value != null && value == 1) {
            selectedToSortByName = true;
            changeNameSortMethodRes(SORT_BY_NAME_METHOD);
        } else if (value != null && value == 2) {
            selectedToSortByAmount = true;
            changeAmountSortMethodRes(SORT_BY_AMOUNT_METHOD);
        } else if (value != null && value == 3) {
            selectedToSortByDate = true;
            changeDateSortMethodRes(SORT_BY_DATE_METHOD);
        }

        value = filters.get(REVERSE_FILTER_ID);
        if (value != null && value == 1) {
            reversedCheckBox.setChecked(true);
            reversedCheckboxTitle.setText(R.string.high_to_low);
        }

        value = filters.get(CATEGORY_FILTER_ID);
        selectedCategoryName = categoryBottomSheetSelector.getCategoryNameById(value);
        initializeSelectedCategoryId(value);
        changeCategorySelectorName(selectedCategoryName);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showBottomSheetToFilterByCategory() {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            changeCategorySelectorName(categoryBottomSheetSelector.getSelectedName());
            initializeSelectedCategoryId(categoryBottomSheetSelector.getSelectedId());
        });
    }

    private void initializeSelectedCategoryId(Integer value) {
        if (value != null) {
            selectedCategoryId = value;
        }
    }

    private void changeCategorySelectorName(String categoryName) {
        if (categoryName != null) {
            categorySelector.setText(categoryName);
        } else {
            categorySelector.setText(R.string.select_category);
        }
    }

}
