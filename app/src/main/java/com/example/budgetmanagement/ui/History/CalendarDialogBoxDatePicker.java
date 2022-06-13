package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.widget.EditText;

import androidx.fragment.app.FragmentManager;

import com.example.budgetmanagement.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
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
            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                calendar.setText(sdf.format(selection));
            });
        }
    }
}
