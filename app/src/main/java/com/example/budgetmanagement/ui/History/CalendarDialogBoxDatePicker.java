package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DEFAULT_DATE_FORMAT;

import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.example.budgetmanagement.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalendarDialogBoxDatePicker {

    MaterialDatePicker<Long> datePicker;

    public CalendarDialogBoxDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_date)
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
    }

    public void show(FragmentManager parentFragmentManager, EditText calendar) {
        if (!datePicker.isAdded()) {
            datePicker.show(parentFragmentManager, "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> calendar.setText(getDateInFormat(selection)));
        }
    }

    private String getDateInFormat(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
         return sdf.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateInFormat(LocalDate date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return dtf.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTodayDateInFormat() {
        return getDateInFormat(LocalDate.now());
    }
}
