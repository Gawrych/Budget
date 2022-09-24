package com.example.budgetmanagement.ui.utils;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public interface GetViewTransactionFields {

    TextInputEditText getTitleField();

    int getCategoryId();

    TextInputEditText getStartDateField();

    SwitchMaterial getProfitSwitch();

    TextInputLayout getTitleLayoutField();

    TextInputLayout getAmountLayoutField();

    TextInputEditText getAmountField();

    Context getFragmentContext();

    AutoCompleteTextView getSelectedCategory();
}
