package com.example.budgetmanagement.ui.utils;

import org.joda.time.Period;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

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

    public static String getTodayDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return dateTimeFormatter.format(LocalDate.now());
    }

    public static String getTodayDate(String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        return dateTimeFormatter.format(LocalDate.now());
    }

    public static ArrayList<String> getMonthInShort() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(SHORT_MONTH_NAME_ONLY);
        ArrayList<String> months = new ArrayList<>();

        for (int i=0; i<12; i++) {
            LocalDate date = Instant.ofEpochMilli(calendar.getTimeInMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
            months.add(dateTimeFormatter.format(date));
            calendar.add(Calendar.MONTH, 1);
        }

        return months;
    }
}
