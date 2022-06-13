package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionDataCollectorFromUser {

    private final View root;
    private float amount;
    private String title;
    private long date;
    private boolean isProfit;
    private int categoryId;
    private boolean ifIsEmpty;

    public TransactionDataCollectorFromUser(View root) {
        this.root = root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean collectData(EditText calendar, int categoryId) {
        setCategoryId(categoryId);

        setTitle();
        if (ifIsEmpty) {
            return false;
        }

        setAmount();
        if (ifIsEmpty) {
             return false;
        }

        setDateInPattern(calendar);
        setProfit();

        return true;
    }

    private void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private void setTitle() {
        TitleEditTextField titleField = new TitleEditTextField(root);
        initializeTitle(titleField);
        checkFillingByLength(titleField);
        if (ifIsEmpty) {
            titleField.setEmptyFieldErrorMessage();
        }
    }

    private void initializeTitle(TitleEditTextField titleField) {
        title = titleField.getContent();
    }

    private void checkFillingByLength(EditTextField editTextField) {
        ifIsEmpty = editTextField.checkIfFieldIsEmpty();
    }

    public void setAmount() {
        AmountEditTextField amountField = new AmountEditTextField(root);
        checkFillingByLength(amountField);
        if (ifIsEmpty) {
            amountField.setEmptyFieldErrorMessage();
        } else {
            initializeAmount(amountField);
        }
    }

    private void initializeAmount(AmountEditTextField amountField) {
        amount = amountField.getParsedContent();
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

    private void setProfit() {
        isProfit = getSelectedProfitIconId() == getProfitIconId();
    }

    private int getProfitIconId() {
        return R.id.profitTrue;
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

    public float getAmount() {
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
