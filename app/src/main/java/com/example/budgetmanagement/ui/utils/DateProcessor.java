package com.example.budgetmanagement.ui.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.AutoCompleteTextView;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

public class DateProcessor {

    public static final String MONTH_NAME_DATE_FORMAT = "d MMM";
    public static final String DEFAULT_DATE_FORMAT = "d MMM yyyy";

    public static String parse(long dateInMillis) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    public static String parse(long dateInMillis, String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    public static long parseDateInPatternToMillis(String dateInPattern, String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate localDate = LocalDate.parse(dateInPattern, dateTimeFormatter);
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    public static String[] getShortMonths() {
        return new DateFormatSymbols(Locale.getDefault()).getShortMonths();
    }

    public static DatePickerDialog getDatePickerDialog(Context context, AutoCompleteTextView field, long startingDate) {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(startingDate);
        int mYear = startDate.get(Calendar.YEAR);
        int mMonth = startDate.get(Calendar.MONTH);
        int mDay = startDate.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    field.setText(DateProcessor.parse((
                                    selectedDate.getTimeInMillis()),
                            DEFAULT_DATE_FORMAT));
                }, mYear, mMonth, mDay);
    }

    public static Calendar getCalendarFromDateInStringPattern(String dateInPattern, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(parseDateInPatternToMillis(dateInPattern, dateFormat));
        return calendar;
    }

    public static int getRemainingDays(long deadline) {
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(deadline);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = otherDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}
