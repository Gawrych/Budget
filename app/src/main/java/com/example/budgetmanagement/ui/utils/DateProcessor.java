package com.example.budgetmanagement.ui.utils;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class DateProcessor {

    public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
    public static final String MONTH_NAME_DATE_FORMAT = "d MMM";
    public static final String MONTH_NAME_YEAR_DATE_FORMAT = "d MMM yyyy";
    public static final String FULL_MONTH_NAME_ONLY = "LLLL";
    public static final String SHORT_MONTH_NAME_ONLY = "MMM";

    public static String parseDate(long dateInMillis) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    public static String parseDate(long dateInMillis, String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    public static String getTodayDate(String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        return dateTimeFormatter.format(LocalDate.now());
    }

    public static String[] getShortMonths() {
        return new DateFormatSymbols(Locale.getDefault()).getShortMonths();
    }
}
