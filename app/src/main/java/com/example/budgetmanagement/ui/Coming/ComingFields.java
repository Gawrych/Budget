package com.example.budgetmanagement.ui.Coming;

import android.widget.AutoCompleteTextView;

import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public interface ComingFields extends GetViewTransactionFields {

    AutoCompleteTextView getTimeBetweenExecutePicker();

    TextInputEditText getEndDate();

    TextInputLayout getTimeBetweenExecutePickerLayout();

    TextInputLayout getEndDateLayout();

    SwitchMaterial getCyclicalSwitch();
}
