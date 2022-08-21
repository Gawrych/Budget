package com.example.budgetmanagement.ui.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateProcessor {

    public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String parseDate(long dateInMillis) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        LocalDate date = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTodayDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return dateTimeFormatter.format(LocalDate.now());
    }
}
