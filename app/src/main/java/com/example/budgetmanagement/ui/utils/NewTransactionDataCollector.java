package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.budgetmanagement.database.rooms.Transaction;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
        Calendar calendar = Calendar.getInstance();
        return new Transaction(0, 1,
                "Samochód", "-200", 1,
                0, true, calendar.getTimeInMillis(), calendar.get(Calendar.YEAR), 1241155550L);
    }

    public Transaction getTransaction(int id) {
        Calendar calendar = Calendar.getInstance();
        return new Transaction(0, 1,
                "Samochód", "-200", 1,
                0, true, calendar.getTimeInMillis(), calendar.get(Calendar.YEAR), 1241155550L);
    }
}
