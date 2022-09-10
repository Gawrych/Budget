package com.example.budgetmanagement.ui.utils;

import android.view.View;
import android.widget.EditText;

import com.example.budgetmanagement.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditFieldManager {

    private TextInputEditText field;
    private View root;

    public EditFieldManager(View root, int resourceId) {
        setRoot(root);
        initializeField(resourceId);
    }

    private void setRoot(View root) {
        this.root = root;
    }

    private void initializeField(int resourceId) {
        field = root.findViewById(resourceId);
    }

    public void setEmptyFieldErrorMessage() {
        field.setError(getEmptyFieldErrorMessage());
    }

    private String getEmptyFieldErrorMessage() {
        return root.getContext().getString(getEmptyFieldErrorMessageId());
    }

    private int getEmptyFieldErrorMessageId() {
        return R.string.field_cant_be_empty;
    }

    public String getContent() {
        return field.getText().toString();
    }

    public boolean checkIfContentExist() {
        return getContent().length() > 0;
    }
}
