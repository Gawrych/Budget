package com.example.budgetmanagement.ui.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.budgetmanagement.R;

public class SortingMarkIconManager {

    private ImageView categoryFilterIcon;
    private ImageView profitIcon;
    private ImageView sortFilterIcon;
    private ImageView reverseSortingIcon;
    private ConstraintLayout constraintLayout;
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public SortingMarkIconManager() {
    }

    public void prepareSortingIcons() {
        try {
            constraintLayout = view.findViewById(R.id.sortingMarksConstraintLayout);
            categoryFilterIcon = view.findViewById(R.id.categoryFilterMarkIcon);
            sortFilterIcon = view.findViewById(R.id.orderSortingMarkIcon);
            profitIcon = view.findViewById(R.id.incomeStatementMarkIcon);
            reverseSortingIcon = view.findViewById(R.id.reverseSortingMarkIcon);
        } catch (NullPointerException e) {
//            TODO: Catch this
        }
    }

    public void showSortIcon(int resourceIconId) {
        showLayout();
        sortFilterIcon.setImageResource(resourceIconId);
        setImageViewTagToResourceId(sortFilterIcon, resourceIconId);
        showShadowIcon(sortFilterIcon);
    }

    private void showLayout() {
        constraintLayout.setVisibility(View.VISIBLE);
    }

    public void showCategoryIcon(String iconName) {
        showLayout();
        changeCategoryFilterIcon(iconName);
        setImageViewTagToResourceId(categoryFilterIcon, getResourceIdFromName(iconName));
        showShadowIcon(categoryFilterIcon);
    }

    public void showProfitIcon(int resourceId) {
        showLayout();
        if (resourceId != R.drawable.profit_and_loss_icon) {
            profitIcon.setImageResource(resourceId);
            setImageViewTagToResourceId(profitIcon, resourceId);
            showShadowIcon(profitIcon);
        } else {
            goneShadowIcon(profitIcon);
        }
    }

    private int getResourceIdFromName(String resName) {
        return view.getContext().getResources().getIdentifier(resName, "drawable", view.getContext().getPackageName());
    }

    public void showReverseIcon(boolean isChecked) {
        showLayout();
        if (isChecked) {
            showShadowIcon(reverseSortingIcon);
        } else {
            goneShadowIcon(reverseSortingIcon);
        }
    }

    private void showShadowIcon(ImageView icon) {
        icon.setVisibility(View.VISIBLE);
    }

    private void goneShadowIcon(ImageView icon) {
        icon.setVisibility(View.GONE);
    }

    private void changeCategoryFilterIcon(String iconName) {
        categoryFilterIcon.setImageResource(getResourceIdFromName(iconName));
    }

    private void setImageViewTagToResourceId(ImageView icon, int resId) {
        icon.setTag(resId);
    }
}
