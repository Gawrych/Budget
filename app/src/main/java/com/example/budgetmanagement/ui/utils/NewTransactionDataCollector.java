package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.ui.utils.BasicDataCollector;
import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class NewTransactionDataCollector extends BasicDataCollector {

    private long date;
    private int categoryId;
    private final GetViewTransactionFields fieldsInterface;

    public NewTransactionDataCollector(GetViewTransactionFields fieldsInterface) {
        super(fieldsInterface);
        this.fieldsInterface = fieldsInterface;
    }

    public boolean collectData() {
        this.categoryId = fieldsInterface.getCategoryId();
        setDateInPattern();
        return super.collectData();
    }

    private void setDateInPattern() {
        AutoCompleteTextView dateField = fieldsInterface.getStartDateField();
        date = getDateInPatternFromTextField(dateField);
    }

    public long getDateInPatternFromTextField(AutoCompleteTextView dateField) {
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
        return new Transaction(0, this.categoryId, getTitle(),
                getAmount().toString(), date, 0, isProfit());
    }

    public Transaction getTransaction(int id) {
        return new Transaction(id, this.categoryId, getTitle(),
                getAmount().toString(), date, 0, isProfit());
    }
}
