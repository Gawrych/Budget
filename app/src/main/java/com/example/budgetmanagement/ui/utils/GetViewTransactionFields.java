package com.example.budgetmanagement.ui.utils;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public interface GetViewTransactionFields extends BasicFormFields {

    int getCategoryId();

    AutoCompleteTextView getStartDateField();

    AutoCompleteTextView getSelectedCategory();

    Context getFragmentContext();
}
