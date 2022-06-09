package com.example.budgetmanagement.ui.History;

import android.view.View;

import com.example.budgetmanagement.R;

public class TitleEditTextField extends EditTextField {

    public TitleEditTextField(View root) {
        super(root);
    }

    @Override
    protected int getResourcesId() {
        return R.id.title;
    }
}
