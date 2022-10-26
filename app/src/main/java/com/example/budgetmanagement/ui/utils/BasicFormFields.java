package com.example.budgetmanagement.ui.utils;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public interface BasicFormFields {

    TextInputEditText getTitleField();

    TextInputLayout getTitleLayoutField();

    TextInputEditText getAmountField();

    TextInputLayout getAmountLayoutField();

    SwitchMaterial getProfitSwitch();
}
