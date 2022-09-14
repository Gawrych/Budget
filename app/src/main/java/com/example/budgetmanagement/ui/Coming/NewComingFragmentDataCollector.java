package com.example.budgetmanagement.ui.Coming;

import android.app.assist.AssistStructure;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.ui.History.NewTransactionDataCollector;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class NewComingFragmentDataCollector extends NewTransactionDataCollector {

    private final View root;

    public NewComingFragmentDataCollector(View root) {
        super(root);
        this.root = root;
    }

    public long getStartDate() {
        TextInputEditText startDate = this.root.findViewById(R.id.startDate);
        return getDateInPatternFromTextField(startDate);
    }

    public long getEndDate() {
        TextInputEditText endDate = this.root.findViewById(R.id.endDate);
        return getDateInPatternFromTextField(endDate);
    }

    public Coming getComing(long transactionId, long date) {
        return new Coming(0, (int) transactionId, (byte) 0, false,
                date, 0, Calendar.getInstance().getTimeInMillis(), 0);
    }

    public ArrayList<Long> getAllDates() {
        ArrayList<Long> allDateToComingAdd = new ArrayList<>();
        SwitchMaterial cyclicalSwitch = this.root.findViewById(R.id.isCyclical);
        AutoCompleteTextView timeBetweenExecutePicker = this.root.findViewById(R.id.timeBetweenPay);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getStartDate());
        long nextDate = calendar.getTimeInMillis();

        if (cyclicalSwitch.isChecked()) {
            int addAmount = 1;
            int timeBetween = getNumberFromTimeBetweenField(timeBetweenExecutePicker.getText().toString());

            if (timeBetweenExecutePicker.getText().toString().equals("Co kwartał")) {
                addAmount = 3;
                timeBetween = Calendar.MONTH;
            }

            while(nextDate <= getEndDate()) {
                allDateToComingAdd.add(nextDate);
                calendar.add(timeBetween, addAmount);
                nextDate = calendar.getTimeInMillis();
            }
        } else {
            allDateToComingAdd.add(nextDate);
        }

        return allDateToComingAdd;
    }

    private int getNumberFromTimeBetweenField(String pickedTimeBetween) {
        if ("Co dzień".equals(pickedTimeBetween)) {
            return Calendar.DAY_OF_YEAR;
        } else if ("Co tydzień".equals(pickedTimeBetween)) {
            return Calendar.WEEK_OF_YEAR;
        }else if ("Co miesiąc".equals(pickedTimeBetween)) {
            return Calendar.MONTH;
        } else if ("Co rok".equals(pickedTimeBetween)) {
            return Calendar.YEAR;
        }
        return 0;
    }
}
