package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionDataCollectorFromUser {

    private final View root;
    private BigDecimal amount;
    private String title;
    private long date;
    private boolean isProfit;
    private int categoryId;
    private boolean contentExist;

    public TransactionDataCollectorFromUser(View root) {
        this.root = root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean collectData(EditText calendar, int categoryId) {
        setCategoryId(categoryId);

        setTitle();
        if (!contentExist) {
            return false;
        }

        setAmount();
        if (!contentExist) {
             return false;
        }

        setDateInPattern(calendar);
        prepareProfit();

        return true;
    }

    private void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private void setTitle() {
        EditField titleField = new EditField(root, R.id.title);
        initializeTitle(titleField);
        checkFillingByLength(titleField);
        if (!contentExist) {
            titleField.setEmptyFieldErrorMessage();
        }
    }

    private void initializeTitle(@NonNull EditField titleField) {
        title = titleField.getContent();
    }


    private void checkFillingByLength(@NonNull EditField editTextField) {
        contentExist = editTextField.checkIfContentExist();
    }

    public void setAmount() {
        EditField amountField = new EditField(root, R.id.amount);
        DecimalPrecision amountContent = new DecimalPrecision(amountField.getContent());
        checkFillingByLength(amountField);
        if (!contentExist) {
            amountField.setEmptyFieldErrorMessage();
        } else {
            initializeAmount(amountContent);
        }
    }

    private void initializeAmount(DecimalPrecision decimalPrecision) {
        amount = decimalPrecision.getParsedContent();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateInPattern(EditText calendar) {
        date = getSelectedDateInPattern(calendar).toEpochDay();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getSelectedDateInPattern(EditText calendar) {
        return LocalDate.parse(calendar.getText(), getPattern());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DateTimeFormatter getPattern() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT);
    }

    private void prepareProfit() {
        isProfit = getSelectedProfitIconId() == getProfitIconId();
    }

    private int getProfitIconId() {
        return R.id.profit;
    }

    private int getSelectedProfitIconId() {
        return getRadioGroup().getCheckedRadioButtonId();
    }

    private RadioGroup getRadioGroup() {
        return root.findViewById(getRadioGroupId());
    }

    private int getRadioGroupId() {
        return R.id.radioGroup;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public boolean isProfit() {
        return isProfit;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
