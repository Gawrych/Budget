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
    private EditText titleField;
    private EditText amountField;
    private boolean isEmpty;

    public TransactionDataCollectorFromUser(View root) {
        this.root = root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean collectData(EditText calendar, int categoryId) {
        initializeFields(categoryId);

        setTitle();
        if (isEmpty) {
            return false;
        }

        setAmount();
        if (isEmpty) {
             return false;
        }

        setDateInPattern(calendar);
        setProfit();

        return true;
    }

    private void initializeFields(int categoryId) {
        setCategoryId(categoryId);
        getTitleField();
        getAmountField();
    }

    private void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private void getTitleField() {
         titleField = root.findViewById(R.id.title);
    }

    private void getAmountField() {
        amountField = root.findViewById(R.id.amount);
    }

    private void setTitle() {
        getTextFromTitle();
        checkLength(title);
        if (isEmpty) {
            setEmptyFieldErrorMessage(titleField);
        }
    }

    private void getTextFromTitle() {
        title = titleField.getText().toString();
    }

    private void checkLength(String value) {
        isEmpty = value.length() == 0;
    }

    private void setEmptyFieldErrorMessage(EditText field) {
        field.setError(root.getContext().getString(R.string.field_cant_be_empty));
    }

    public void setAmount() {
        String amountInString = getTextFromAmount();
        checkLength(amountInString);
        if (isEmpty) {
            setEmptyFieldErrorMessage(amountField);
        } else {
            initializeAmountVariable(amountInString);
        }
    }

    public String getTextFromAmount() {
        return amountField.getText().toString();
    }

    public void initializeAmountVariable(String amountInString) {
        amount = Float.parseFloat(amountInString);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateInPattern(EditText calendar) {
        date = getLocalDateInPattern(calendar).toEpochDay();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getLocalDateInPattern(EditText calendar) {
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
        return root.findViewById(R.id.radioGroup);
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
