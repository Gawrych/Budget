package com.example.budgetmanagement.ui.Coming;

import android.content.res.Resources;
import android.util.ArrayMap;
import android.widget.AutoCompleteTextView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.ui.History.NewTransactionDataCollector;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class NewComingFragmentDataCollector extends NewTransactionDataCollector {

    private final GetViewComingFields fieldsInterface;
    private final ArrayMap<String, Integer> timeBetweenValues;

    private final int QUARTER_OF_YEAR = 0;

    public NewComingFragmentDataCollector(GetViewComingFields fieldsInterface) {
        super(fieldsInterface);
        this.fieldsInterface = fieldsInterface;
        Resources resources = fieldsInterface.getFragmentContext().getResources();

        timeBetweenValues = new ArrayMap<>();
        timeBetweenValues.put(resources.getString(R.string.each_day), Calendar.DAY_OF_YEAR);
        timeBetweenValues.put(resources.getString(R.string.each_week), Calendar.WEEK_OF_YEAR);
        timeBetweenValues.put(resources.getString(R.string.each_month), Calendar.MONTH);
        timeBetweenValues.put(resources.getString(R.string.each_quarter), QUARTER_OF_YEAR);
        timeBetweenValues.put(resources.getString(R.string.each_year), Calendar.YEAR);
    }

    public long getStartDate() {
        TextInputEditText startDate = fieldsInterface.getStartDateField();
        return getDateInPatternFromTextField(startDate);
    }

    public long getEndDate() {
        TextInputEditText endDate = fieldsInterface.getEndDate();
        return getDateInPatternFromTextField(endDate);
    }

    public Coming getComing(long transactionId, long date) {
        return new Coming(0, (int) transactionId, (byte) 0, false,
                date, 0, Calendar.getInstance().getTimeInMillis(), 0);
    }

    public ArrayList<Long> getNextDates() {
        ArrayList<Long> allDatesToCreateNewComing = new ArrayList<>();
        SwitchMaterial cyclicalSwitch = fieldsInterface.getCyclicalSwitch();
        AutoCompleteTextView timeBetweenExecutePicker = fieldsInterface.getTimeBetweenExecutePicker();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getStartDate());
        long nextDate = calendar.getTimeInMillis();

        if (cyclicalSwitch.isChecked()) {
            int howMuchAddToCreateNextDate = 1;

            Integer valueFromTimeBetweenMap = timeBetweenValues.get(timeBetweenExecutePicker.getText().toString());
            assert valueFromTimeBetweenMap != null;
            int timeBetween = valueFromTimeBetweenMap;

            if (timeBetween == QUARTER_OF_YEAR) {
                howMuchAddToCreateNextDate = 3;
                timeBetween = Calendar.MONTH;
            }

            long endDate = getEndDate();

            while(nextDate <= endDate) {
                allDatesToCreateNewComing.add(nextDate);
                calendar.add(timeBetween, howMuchAddToCreateNextDate);
                nextDate = calendar.getTimeInMillis();
            }
        } else {
            allDatesToCreateNewComing.add(nextDate);
        }

        return allDatesToCreateNewComing;
    }
}
