package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateModifierForViewHolder {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateInDefaultPattern(long repeatDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.ofEpochDay(repeatDate);
        return dateTimeFormatter.format(localDate);
    }
}
