package com.example.budgetmanagement.ui.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.budgetmanagement.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        return successCollectedData;
    }

    public void resetCollectedStatus() {
        successCollectedData = true;
    }

    public String collectBasedOnProfitSwitch(TextInputLayout amountLayout, SwitchMaterial profitSwitch) {
        String collectedValue = collect(amountLayout);
        if (collectedValue.length() == 0) return "";
        BigDecimal amountWithRounding = new BigDecimal(collectedValue).setScale(2, RoundingMode.UP).stripTrailingZeros();
        if (!profitSwitch.isChecked() && successCollectedData) {
             return amountWithRounding.negate().toPlainString();
        }
        return amountWithRounding.toPlainString();
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
