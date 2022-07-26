package com.example.budgetmanagement.ui.utils.filteringList;

import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_NAME_METHOD;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;

import java.util.HashMap;

public class FilterFragmentUiManager {

    private final ConstraintLayout sortByNameLayout;
    private final ConstraintLayout sortByAmountLayout;
    private final ConstraintLayout sortByDateLayout;

    private final ImageView sortByNameIconImageView;
    private final ImageView sortByAmountIconImageView;
    private final ImageView sortByDateIconImageView;

    private final TextView reversedCheckboxTitle;
    private final CheckBox reversedCheckBox;
    private final Resources resources;
    private final TextView categorySelector;
    private final RadioButton profitRadio;
    private final RadioButton profitAndLossRadio;
    private final RadioButton lossRadio;
    private int selectedCategoryId = 0;
    private boolean selectedToSortByName = false;
    private boolean selectedToSortByAmount = false;
    private boolean selectedToSortByDate = false;
    private final CategoryBottomSheetSelector categoryBottomSheetSelector;
    private final Button resetButton;
    private final Button filterButton;
    private HashMap<Integer, Integer> filters;

    public FilterFragmentUiManager(View root, CategoryBottomSheetSelector categoryBottomSheetSelector) {
        this.resources = root.getResources();
        this.categoryBottomSheetSelector = categoryBottomSheetSelector;

        resetButton = root.findViewById(R.id.resetFilters);
        filterButton = root.findViewById(R.id.filterList);
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
        reversedCheckBox = root.findViewById(R.id.reversedCheck);
        reversedCheckboxTitle = root.findViewById(R.id.reversedCheckTitle);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void markUiFilters(HashMap<Integer, Integer> filters) {
        this.filters = filters;

        int value = getValueByFilterName(PROFIT_FILTER_ID);
        if (value == 1) {
            profitRadio.setChecked(true);
        } else if (value == 2) {
            lossRadio.setChecked(true);
        }

        value = getValueByFilterName(ORDER_FILTER_ID);
        clearGlobalVariable();
        if (value == 1) {
            selectedToSortByName = true;
            changeNameSortMethodRes();
        } else if (value == 2) {
            selectedToSortByAmount = true;
            changeAmountSortMethodRes();
        } else if (value == 3) {
            selectedToSortByDate = true;
            changeDateSortMethodRes();
        }

        value = getValueByFilterName(REVERSE_FILTER_ID);
        if (value == 1) {
            reversedCheckBox.setChecked(true);
            changeTextOnHighToLow();
        }

        value = getValueByFilterName(CATEGORY_FILTER_ID);
        String selectedCategoryName = categoryBottomSheetSelector.getCategoryNameById(value);
        initializeSelectedCategoryId(value);
        changeCategorySelectorName(selectedCategoryName);
    }

    private int getValueByFilterName(int filterName) {
        Integer value = filters.get(filterName);
        return value != null ? value : 0;
    }

    private void clearGlobalVariable() {
        selectedToSortByName = false;
        selectedToSortByAmount = false;
        selectedToSortByDate = false;
    }

    private void changeTextOnHighToLow() {
        reversedCheckboxTitle.setText(getHighToLowStringId());
    }

    public void selectOrderSorting(int sortMethod) {
        if (sortMethod == SORT_BY_NAME_METHOD) {
            selectedToSortByName = !selectedToSortByName;
            selectedToSortByAmount = false;
            selectedToSortByDate = false;
            changeNameSortMethodRes();
        }
        if (sortMethod == SORT_BY_AMOUNT_METHOD) {
            selectedToSortByAmount = !selectedToSortByAmount;
            selectedToSortByName = false;
            selectedToSortByDate = false;
            changeAmountSortMethodRes();
        }
        if (sortMethod == SORT_BY_DATE_METHOD) {
            selectedToSortByDate = !selectedToSortByDate;
            selectedToSortByName = false;
            selectedToSortByAmount = false;
            changeDateSortMethodRes();
        }
    }

    private void clearOrderFilters() {
        setResources(sortByNameIconImageView, R.drawable.block);
        setColor(sortByNameLayout, R.color.white);

        setResources(sortByAmountIconImageView, R.drawable.dollar_coin);
        setColor(sortByAmountLayout, R.color.white);

        setResources(sortByDateIconImageView, R.drawable.calendar);
        setColor(sortByDateLayout, R.color.white);
    }

    private void changeNameSortMethodRes() {
        clearOrderFilters();
        if (selectedToSortByName) {
            setResources(sortByNameIconImageView, R.drawable.block_color);
            setColor(sortByNameLayout, R.color.very_light_gray);
        }
    }

    private void changeAmountSortMethodRes() {
        clearOrderFilters();
        if (selectedToSortByAmount) {
            setResources(sortByAmountIconImageView, R.drawable.dollar_coin_color);
            setColor(sortByAmountLayout, R.color.very_light_gray);
        }
    }

    private void changeDateSortMethodRes() {
        clearOrderFilters();
        if (selectedToSortByDate) {
            setResources(sortByDateIconImageView, R.drawable.calendar_color);
            setColor(sortByDateLayout, R.color.very_light_gray);
        }
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

    public void clearProfitFilter() {
        profitAndLossRadio.setChecked(true);
    }

    private void clearReverseButton() {
        reversedCheckBox.setChecked(false);
        reversedCheckboxTitle.setText(R.string.low_to_high);
    }

    private void clearCategorySelector() {
        int defaultValue = 0;
        String defaultName = "";
        initializeSelectedCategoryId(defaultValue);
        changeCategorySelectorName(defaultName);
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

    private int getHighToLowStringId() {
        return R.string.high_to_low;
    }

    private void changeTextOnLowToHigh() {
        reversedCheckboxTitle.setText(getLowToHighStringId());
    }

    private int getLowToHighStringId() {
        return R.string.low_to_high;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showBottomSheetToSelectCategory() {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            initializeSelectedCategoryId(categoryBottomSheetSelector.getSelectedId());
            changeCategorySelectorName(categoryBottomSheetSelector.getSelectedName());
        });
    }

    private void initializeSelectedCategoryId(Integer value) {
        if (value != null) {
            selectedCategoryId = value;
        }
    }

    private void changeCategorySelectorName(String categoryName) {
        if (categoryName.equals("")) {
            categorySelector.setText(R.string.select_category);
        } else {
            categorySelector.setText(categoryName);
        }
    }

    public ConstraintLayout getSortByNameLayout() {
        return sortByNameLayout;
    }

    public ConstraintLayout getSortByAmountLayout() {
        return sortByAmountLayout;
    }

    public ConstraintLayout getSortByDateLayout() {
        return sortByDateLayout;
    }

    public CheckBox getReversedCheckBox() {
        return reversedCheckBox;
    }

    public TextView getCategorySelector() {
        return categorySelector;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getFilterButton() {
        return filterButton;
    }

    public boolean isSelectedToSortByName() {
        return selectedToSortByName;
    }

    public boolean isSelectedToSortByAmount() {
        return selectedToSortByAmount;
    }

    public boolean isSelectedToSortByDate() {
        return selectedToSortByDate;
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
}
