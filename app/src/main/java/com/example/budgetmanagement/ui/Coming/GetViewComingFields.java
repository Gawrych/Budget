package com.example.budgetmanagement.ui.Coming;

import android.widget.AutoCompleteTextView;

import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public interface GetViewComingFields extends GetViewTransactionFields {

    AutoCompleteTextView getTimeBetweenExecutePicker();

    SwitchMaterial getCyclicalSwitch();

    TextInputEditText getEndDate();
}
