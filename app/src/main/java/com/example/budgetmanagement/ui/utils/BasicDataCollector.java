package com.example.budgetmanagement.ui.utils;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

import android.text.Editable;
import android.widget.EditText;

import com.example.budgetmanagement.MainActivity;
import com.example.budgetmanagement.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

public class BasicDataCollector {

    private final BasicFormFields basicFormFields;
    private String title;
    private BigDecimal amount;
    private boolean profit;

    public BasicDataCollector(BasicFormFields basicFormFields) {
        this.basicFormFields = basicFormFields;
    }

    public boolean collectData() {
        boolean correctlySetTitleContent = setTitle();
        if (!correctlySetTitleContent) {
            return false;
        }

        profit = basicFormFields.getProfitSwitch().isChecked();

        boolean correctlySetAmountContent = setAmount();
        if (!correctlySetAmountContent) {
            return false;
        }

        return true;
    }

    private boolean setTitle() {
        TextInputEditText titleField = basicFormFields.getTitleField();
        title = getContent(titleField);
        if (contentNotExist(title)) {
            setError(basicFormFields.getTitleLayoutField());
            return false;
        }
        return true;
    }

    private boolean setAmount() {
        TextInputEditText amountField = basicFormFields.getAmountField();
        String amountContent = getContent(amountField);
        if (contentNotExist(amountContent)) {
            setError(basicFormFields.getAmountLayoutField());
            return false;
        }
        DecimalPrecision amountDecimalPrecision = new DecimalPrecision(amountContent);
        amount = negateIfNegativeProfit(amountDecimalPrecision.getParsedContent());
        return true;
    }

    private void setError(TextInputLayout layout) {
        try {
            runOnUiThread(() -> layout.setError(MainActivity.resources.getString(R.string.empty_field)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean contentNotExist(String content) {
        return content.length() < 1;
    }

    public String getContent(EditText field) {
        Editable editable = field.getText();
        if (editable == null) {
            return "";
        }
        return editable.toString();
    }

    private BigDecimal negateIfNegativeProfit(BigDecimal number) {
        if (!profit) {
            return number.negate();
        }
        return number;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isProfit() {
        return profit;
    }
}
