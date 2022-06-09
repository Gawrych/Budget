package com.example.budgetmanagement.ui.History;

import android.view.View;

import com.example.budgetmanagement.R;

public class AmountEditTextField extends EditTextField {

    public AmountEditTextField(View root) {
        super(root);
    }

    @Override
    protected int getResourcesId() {
        return R.id.amount;
    }

    public float getParsedContent() {
        return Float.parseFloat(getContent());
    }
}
