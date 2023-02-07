package com.example.budgetmanagement.ui.coming;

import android.content.Context;
import android.widget.EditText;

import com.example.budgetmanagement.R;
import com.google.android.material.textfield.TextInputLayout;

public class TransactionCollector {

    private boolean successCollectedData = true;
    private final String emptyFieldErrorMessage;

    public TransactionCollector(Context context) {
        emptyFieldErrorMessage = context.getString(R.string.empty_field);
    }

    public String collect(TextInputLayout inputLayout) {
        EditText editText = inputLayout.getEditText();
        String value = editText == null ? "" : editText.getText().toString();

        if (value.isEmpty()) {
            setErrorMessage(inputLayout);
            successCollectedData = false;
        }

        return value;
    }

    private void setErrorMessage(TextInputLayout inputLayout) {
        inputLayout.setError(emptyFieldErrorMessage);
    }

    public boolean isCorrect() {
        return this.successCollectedData;
    }

    public void reset() {
        this.successCollectedData = true;
    }
}
