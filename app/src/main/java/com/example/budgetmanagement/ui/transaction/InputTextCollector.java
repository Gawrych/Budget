package com.example.budgetmanagement.ui.transaction;

import android.content.Context;
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
}
