package com.example.budgetmanagement.ui.History;

import android.view.View;
import android.widget.EditText;

import com.example.budgetmanagement.R;

public abstract class EditTextField {

    private EditText field;
    private View root;
    private String content;

    public EditTextField(View root) {
        setRoot(root);
        initializeField();
        getText();
    }

    private void setRoot(View root) {
        this.root = root;
    }

    private void initializeField() {
        field = root.findViewById(getResourcesId());
    }

    protected abstract int getResourcesId();

    private void getText() {
        content = field.getText().toString();
    }

    public boolean checkIfFieldIsEmpty() {
        return content.length() == 0;
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
        return content;
    }
}
