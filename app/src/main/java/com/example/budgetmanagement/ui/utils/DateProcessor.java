package com.example.budgetmanagement.ui.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateProcessor {

    public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
    public static final String MONTH_NAME_DATE_FORMAT = "d MMM";
    public static final String MONTH_NAME_YEAR_DATE_FORMAT = "d MMM yyyy";

    public static String parseDate(long dateInMillis) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    public static String parseDate(long dateInMillis, String customDateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(customDateFormat);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    public static String getTodayDateInPattern() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return dateTimeFormatter.format(LocalDate.now());
    }

    public static String getTodayDateInPattern(String customDateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(customDateFormat);
        return dateTimeFormatter.format(LocalDate.now());
    }
}
