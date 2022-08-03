package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.ui.utils.DateProcessor.DEFAULT_DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.EditFieldManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    public void setAmount() {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateInPattern(EditText calendar) {
        LocalDate localDate = getSelectedDateInPattern(calendar);
        date = localDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getSelectedDateInPattern(EditText calendar) {
        return LocalDate.parse(calendar.getText(), getPattern());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DateTimeFormatter getPattern() {
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    }

    private void prepareProfit() {
        profit = getSelectedProfitIconId() == getProfitIconId();
    }

    private int getProfitIconId() {
        return R.id.profitIcon;
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
        return profit;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
