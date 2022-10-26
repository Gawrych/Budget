package com.example.budgetmanagement.ui.Category;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

import com.example.budgetmanagement.MainActivity;
import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.ui.utils.BasicDataCollector;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class NewCategoryDataCollector extends BasicDataCollector {

    private NewCategoryFields newCategoryFields;
    private int iconId;

    public NewCategoryDataCollector(NewCategoryFields newCategoryFields) {
        super(newCategoryFields);
        this.newCategoryFields = newCategoryFields;
    }

    public boolean collectData() {
        this.iconId = newCategoryFields.getIconId();
        if (this.iconId == 0) {
            setError(newCategoryFields.getIconPickerLayout());
            return false;
        }
        return super.collectData();
    }

    private void setError(TextInputLayout layout) {
        try {
            runOnUiThread(() -> layout.setError(MainActivity.resources.getString(R.string.empty_field)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Category getCategory() {
        Calendar today = Calendar.getInstance();
        return new Category(0, getTitle(),
                iconId, getAmount().toString(), today.getTimeInMillis(), 0);
    }
}
