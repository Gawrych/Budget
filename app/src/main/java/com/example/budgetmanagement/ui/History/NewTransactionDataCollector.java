package com.example.budgetmanagement.ui.History;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.content.res.Resources;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.MainThread;

import com.example.budgetmanagement.MainActivity;
import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class NewTransactionDataCollector {

    private BigDecimal amount;
    private String title;
    private long date;
    private boolean profit;
    private int categoryId;
    private final GetViewTransactionFields fieldsInterface;

    public NewTransactionDataCollector(GetViewTransactionFields fieldsInterface) {
        this.fieldsInterface = fieldsInterface;
    }

    public boolean collectData() {
        this.categoryId = fieldsInterface.getCategoryId();

        boolean correctlySetTitleContent = setTitle();
        if (!correctlySetTitleContent) {
            return false;
        }

        profit = fieldsInterface.getProfitSwitch().isChecked();

        boolean correctlySetAmountContent = setAmount();
        if (!correctlySetAmountContent) {
            return false;
        }

        setDateInPattern();

        return true;
    }

    private boolean setTitle() {
        TextInputEditText titleField = fieldsInterface.getTitleField();
        title = getContent(titleField);
        if (contentNotExist(title)) {

            try {
                runOnUiThread(() -> fieldsInterface.getTitleFieldLayout()
                        .setError(MainActivity.resources.getString(R.string.empty_field)));
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return false;
        }
        return true;
    }

    private boolean setAmount() {
        TextInputEditText amountField = fieldsInterface.getAmountField();
        String amountContent = getContent(amountField);
        if (contentNotExist(amountContent)) {
            
            try {
                runOnUiThread(() -> fieldsInterface.getAmountFieldLayout()
                        .setError(MainActivity.resources.getString(R.string.empty_field)));
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return false;
        }
        DecimalPrecision amountDecimalPrecision = new DecimalPrecision(amountContent);
        amount = addMinusIfNegativeAmount(amountDecimalPrecision.getParsedContent());
        return true;
    }

    public boolean contentNotExist(String content) {
        return content.length() < 1;
    }

    private String getContent(TextInputEditText field) {
        Editable editable = field.getText();
        if (editable == null) {
            return "";
        }
        return editable.toString();
    }

    private BigDecimal addMinusIfNegativeAmount(BigDecimal number) {
        if (!profit) {
            return number.negate();
        }
        return number;
    }

    private void setDateInPattern() {
        TextInputEditText dateField = fieldsInterface.getStartDateField();
        date = getDateInPatternFromTextField(dateField);
    }

    public long getDateInPatternFromTextField(TextInputEditText dateField) {
        LocalDate localDate = getSelectedDateInPattern(dateField);
        return localDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    private LocalDate getSelectedDateInPattern(EditText calendar) {
        return LocalDate.parse(calendar.getText(), getPattern());
    }

    private DateTimeFormatter getPattern() {
        return DateTimeFormatter.ofPattern(MONTH_NAME_YEAR_DATE_FORMAT);
    }

    public Transaction getTransaction() {
        return new Transaction(0, this.categoryId, this.title,
                this.amount.toString(), date, 0, this.profit);
    }
}
