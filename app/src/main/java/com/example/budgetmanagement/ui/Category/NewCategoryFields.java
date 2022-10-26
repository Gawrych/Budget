package com.example.budgetmanagement.ui.Category;

import com.example.budgetmanagement.ui.utils.BasicFormFields;
import com.google.android.material.textfield.TextInputLayout;

public interface NewCategoryFields extends BasicFormFields {

    int getIconId();

    TextInputLayout getIconPickerLayout();
}
