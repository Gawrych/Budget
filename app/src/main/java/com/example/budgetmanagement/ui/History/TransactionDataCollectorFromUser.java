package com.example.budgetmanagement.ui.History;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionDataCollectorFromUser {

    private float amount;
    private String title;
    private long date;
    private boolean profit;
    private int categoryId;

    public TransactionDataCollectorFromUser() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean collectData(View root, EditText calendar, int categoryId) {
        EditText titleField = root.findViewById(R.id.title);
        EditText amountField = root.findViewById(R.id.amount);
        this.categoryId = categoryId;

        title = titleField.getText().toString();
        if (title.length() == 0) {
            titleField.setError(root.getContext().getString(R.string.field_cant_be_empty));
            return false;
        }

        String amountInString = amountField.getText().toString();
        if (amountInString.length() == 0) {
            amountField.setError(root.getContext().getString(R.string.field_cant_be_empty));
            return false;
        } else {
            amount = Float.parseFloat(amountInString);
        }

        RadioGroup radioGroup = root.findViewById(R.id.radioGroup);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate parsedDate = LocalDate.parse(calendar.getText(), formatter);
        date = parsedDate.toEpochDay();

        int profitIcon = radioGroup.getCheckedRadioButtonId();
        profit = profitIcon == R.id.profitTrue;

        return true;
    }

    public float getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public boolean isProfit() {
        return profit;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
