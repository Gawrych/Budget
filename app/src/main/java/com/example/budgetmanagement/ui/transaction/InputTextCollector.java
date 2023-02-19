package com.example.budgetmanagement.ui.transaction;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.budgetmanagement.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

public class InputTextCollector {

    private boolean successCollectedData = true;
    private final String emptyFieldErrorMessage;

    public InputTextCollector(Context context) {
        emptyFieldErrorMessage = context.getString(R.string.empty_field);
    }

    public String collect(TextInputLayout inputLayout) {
        EditText editText = inputLayout.getEditText();
        String value = (editText == null) ? "" : editText.getText().toString();

        setTextWatchersToClearError(editText, inputLayout);

        if (value.isEmpty()) {
            setErrorMessage(inputLayout);
            markCollectedDataAsFailure();
        }

        return value;
    }

    private void setTextWatchersToClearError(EditText editText, TextInputLayout inputLayout) {
        if (editText != null) editText.addTextChangedListener(getTextWatcher(inputLayout));
    }

    public void setErrorMessage(TextInputLayout inputLayout) {
        inputLayout.setError(emptyFieldErrorMessage);
    }

    public boolean areCorrectlyCollected() {
        return this.successCollectedData;
    }

    public void resetCollectedStatus() {
        this.successCollectedData = true;
    }

    public String collectBasedOnProfitSwitch(TextInputLayout amountLayout, SwitchMaterial profitSwitch) {
        String amount = collect(amountLayout);
        if (!profitSwitch.isChecked() && successCollectedData) {
            BigDecimal bigDecimal = new BigDecimal(amount);
            return bigDecimal.negate().toPlainString();
        }
        return amount;
    }

    public void markCollectedDataAsFailure() {
        successCollectedData = false;
    }

    private TextWatcher getTextWatcher(TextInputLayout fieldToBeCleared) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                fieldToBeCleared.setError(null);
            }
        };
    }
}
