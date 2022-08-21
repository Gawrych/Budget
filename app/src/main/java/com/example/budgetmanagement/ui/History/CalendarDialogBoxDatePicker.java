package com.example.budgetmanagement.ui.History;

import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.datepicker.MaterialDatePicker;

public class CalendarDialogBoxDatePicker {

    MaterialDatePicker<Long> datePicker;

    public CalendarDialogBoxDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_date)
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void show(FragmentManager parentFragmentManager, EditText calendar) {
        if (!datePicker.isAdded()) {
            datePicker.show(parentFragmentManager, "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                calendar.setText(DateProcessor.parseDate((selection)));
            });
        }
    }
}
