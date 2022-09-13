package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.utils.DateProcessor.DEFAULT_DATE_FORMAT;
import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.ui.utils.EditFieldManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class NewTransactionDataCollector {

    private final View root;
    private BigDecimal amount;
    private String title;
    private long date;
    private boolean profit;
    private int categoryId;
    private boolean contentExist;

    public NewTransactionDataCollector(View root) {
        this.root = root;
    }

    public boolean collectData(EditText calendar, int categoryId) {
        setCategoryId(categoryId);

        setTitle();
        if (!contentExist) {
            return false;
        }

        prepareProfit();
        setAmount();
        if (!contentExist) {
             return false;
        }

        setDateInPattern(calendar);

        return true;
    }

    private void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private void setTitle() {
        EditFieldManager titleField = new EditFieldManager(root, R.id.title);
        initializeTitle(titleField);
        checkFillingByLength(titleField);
        if (!contentExist) {
            titleField.setEmptyFieldErrorMessage();
        }
    }

    private void initializeTitle(@NonNull EditFieldManager titleField) {
        title = titleField.getContent();
    }


    private void checkFillingByLength(@NonNull EditFieldManager editTextField) {
        contentExist = editTextField.checkIfContentExist();
    }

    private void setAmount() {
        EditFieldManager amountField = new EditFieldManager(root, R.id.amount);
        DecimalPrecision amountContent = new DecimalPrecision(amountField.getContent());
        checkFillingByLength(amountField);
        if (!contentExist) {
            amountField.setEmptyFieldErrorMessage();
        } else {
            initializeAmount(amountContent);
        }
    }

    private void initializeAmount(DecimalPrecision decimalPrecision) {
        amount = addMinusToNegativeAmount(decimalPrecision.getParsedContent());
    }

    private BigDecimal addMinusToNegativeAmount(BigDecimal number) {
        if (!profit) {
            return number.negate();
        }
        return number;
    }

    private void setDateInPattern(EditText calendar) {
        LocalDate localDate = getSelectedDateInPattern(calendar);
        date = localDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    private LocalDate getSelectedDateInPattern(EditText calendar) {
        return LocalDate.parse(calendar.getText(), getPattern());
    }

    private DateTimeFormatter getPattern() {
        return DateTimeFormatter.ofPattern(MONTH_NAME_YEAR_DATE_FORMAT);
    }

    private void prepareProfit() {
       profit = getProfitSwitch().isChecked();
    }

    private SwitchMaterial getProfitSwitch() {
        return root.findViewById(R.id.isProfit);
    }


    public Transaction getTransaction() {
        // TODO Change last modified date to 0 instead today date
        Calendar today = Calendar.getInstance();
        return new Transaction(0, this.categoryId, this.title,
                this.amount.toString(), date, today.getTimeInMillis(), this.profit);
    }
}
