package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SortingMarkIconManager {

    private final ImageView categoryFilterIcon;
    private final ImageView profitIcon;
    private final ImageView orderFilterIcon;
    private final ImageView reverseSortingIcon;
    private final View view;
    private final List<Category> categoryList;

    public SortingMarkIconManager(View view, List<Category> categoryList) {
        this.view = view;
        this.categoryList = categoryList;

        categoryFilterIcon = view.findViewById(R.id.categoryFilterMarkIcon);
        orderFilterIcon = view.findViewById(R.id.orderSortingMarkIcon);
        profitIcon = view.findViewById(R.id.incomeStatementMarkIcon);
        reverseSortingIcon = view.findViewById(R.id.reverseSortingMarkIcon);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMarkIcons(HashMap<Integer, Integer> filters) {
        Integer value;

        value = filters.get(REVERSE_FILTER_ID);
        if (valueIsPositive(value)) {
            showReverseIcon(value);
        } else {
            goneShadowIcon(reverseSortingIcon);
        }

        value = filters.get(CATEGORY_FILTER_ID);
        if (valueIsPositive(value)) {
            showCategoryIcon(value);
        } else {
            goneShadowIcon(categoryFilterIcon);
        }

        value = filters.get(PROFIT_FILTER_ID);
        if (valueIsPositive(value)) {
            showProfitIcon(value);
        } else {
            goneShadowIcon(profitIcon);
        }

        value = filters.get(ORDER_FILTER_ID);
        if (valueIsPositive(value)) {
            showOrderIcon(value);
        } else {
            goneShadowIcon(orderFilterIcon);
        }
    }

    private boolean valueIsPositive(Integer value) {
        return value != null && value > 0;
    }

    public void showReverseIcon(int value) {
        if (value == 1) {
            showShadowIcon(reverseSortingIcon);
        }
    }

    private void showShadowIcon(ImageView icon) {
        icon.setVisibility(View.VISIBLE);
    }

    private void goneShadowIcon(ImageView icon) {
        icon.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showCategoryIcon(int categoryId) {
        Category category;
        Optional<Category> optional =
                categoryList.stream().filter(a ->
                        a.getCategoryId() == categoryId).findFirst();
        if (optional.isPresent()) {
            category = optional.get();
//            changeCategoryFilterIcon(category.getIcon());
            showShadowIcon(categoryFilterIcon);
        }
    }

    private void changeCategoryFilterIcon(String iconName) {
        categoryFilterIcon.setImageResource(getResourceIdFromName(iconName));
    }

    private int getResourceIdFromName(String resName) {
        return view
                .getContext()
                .getResources()
                .getIdentifier(resName, "drawable", view.getContext().getPackageName());
    }

    public void showProfitIcon(int profitOrLoss) {
        if (profitOrLoss == 1) {
            profitIcon.setImageResource(R.drawable.profits);
            showShadowIcon(profitIcon);
        } else if (profitOrLoss == 2) {
            profitIcon.setImageResource(R.drawable.loss_profit);
            showShadowIcon(profitIcon);
        }
    }

    public void showOrderIcon(int value) {
        if (value == 1) {
            orderFilterIcon.setImageResource(R.drawable.block);
        } else if (value == 2) {
            orderFilterIcon.setImageResource(R.drawable.dollar_coin);
        } else if (value == 3) {
            orderFilterIcon.setImageResource(R.drawable.calendar);
        }
        showShadowIcon(orderFilterIcon);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeAllMarkIcons() {
        setMarkIcons(getEmptyFiltersMap());
    }

    private HashMap<Integer, Integer> getEmptyFiltersMap() {
        return new HashMap<>();
    }

}
