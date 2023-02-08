//package com.example.budgetmanagement.ui.coming;
//
//import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
//
//import android.content.res.Resources;
//import android.util.ArrayMap;
//import android.widget.AutoCompleteTextView;
//
//import com.example.budgetmanagement.MainActivity;
//import com.example.budgetmanagement.R;
//import com.example.budgetmanagement.database.rooms.Transaction;
//import com.example.budgetmanagement.ui.utils.NewTransactionDataCollector;
//import com.google.android.material.switchmaterial.SwitchMaterial;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//public class NewComingDataCollector extends NewTransactionDataCollector {
//
//    private final ComingFields fieldsInterface;
//    private final ArrayMap<String, Integer> timeBetweenValues;
//    private AutoCompleteTextView endDate;
//    private AutoCompleteTextView timeBetweenExecutePicker;
//    private final int QUARTER_OF_YEAR = 0;
//
//    public NewComingDataCollector(ComingFields fieldsInterface) {
//        super(fieldsInterface);
//        this.fieldsInterface = fieldsInterface;
//        Resources resources = fieldsInterface.getFragmentContext().getResources();
//
//        timeBetweenValues = new ArrayMap<>();
//        timeBetweenValues.put(resources.getString(R.string.each_day), Calendar.DAY_OF_YEAR);
//        timeBetweenValues.put(resources.getString(R.string.each_week), Calendar.WEEK_OF_YEAR);
//        timeBetweenValues.put(resources.getString(R.string.each_month), Calendar.MONTH);
//        timeBetweenValues.put(resources.getString(R.string.each_quarter), QUARTER_OF_YEAR);
//        timeBetweenValues.put(resources.getString(R.string.each_year), Calendar.YEAR);
//    }
//
//    @Override
//    public boolean collectData() {
//        if (fieldsInterface.getCyclicalSwitch().isChecked()) {
//            boolean correctlySetEndDateContent = setEndDate();
//            if (!correctlySetEndDateContent) {
//                return false;
//            }
//
//            boolean correctlySetTimeBetweenContent = setTimeBetween();
//            if (!correctlySetTimeBetweenContent) {
//                return false;
//            }
//        }
//
//        return super.collectData();
//    }
//
//    private boolean setTimeBetween() {
//        timeBetweenExecutePicker = fieldsInterface.getTimeBetweenExecutePicker();
//        if (contentNotExist(getContent(timeBetweenExecutePicker))) {
//            try {
//                runOnUiThread(() -> fieldsInterface.getTimeBetweenExecutePickerLayout()
//                        .setError(MainActivity.resources.getString(R.string.empty_field)));
//
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private boolean setEndDate() {
//        endDate = fieldsInterface.getEndDate();
//        if (contentNotExist(getContent(endDate))) {
//            try {
//                runOnUiThread(() -> fieldsInterface.getEndDateLayout()
//                        .setError(MainActivity.resources.getString(R.string.empty_field)));
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public long getStartDate() {
//        AutoCompleteTextView startDate = fieldsInterface.getStartDateField();
//        return getDateInPatternFromTextField(startDate);
//    }
//
//    public long getEndDate() {
//        return getDateInPatternFromTextField(endDate);
//    }
//
//    public Transaction getComing(long transactionId, long date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(date);
//        int year = calendar.get(Calendar.YEAR);
//
//        return new Transaction(0, (int) transactionId, false,
//                date, year, 0, Calendar.getInstance().getTimeInMillis(), 0);
//    }
//
//    public ArrayList<Long> getNextDates() {
//        ArrayList<Long> allDatesToCreateNewComing = new ArrayList<>();
//        SwitchMaterial cyclicalSwitch = fieldsInterface.getCyclicalSwitch();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(getStartDate());
//        long nextDate = calendar.getTimeInMillis();
//
//        if (cyclicalSwitch.isChecked()) {
//            Integer valueFromTimeBetweenMap = timeBetweenValues.get(timeBetweenExecutePicker.getText().toString());
//            assert valueFromTimeBetweenMap != null;
//            int timeBetween = valueFromTimeBetweenMap;
//
//            long endDate = getEndDate();
//
//            while(nextDate <= endDate) {
//                allDatesToCreateNewComing.add(nextDate);
//                calendar.add(timeBetween, 1);
//                nextDate = calendar.getTimeInMillis();
//            }
//        } else {
//            allDatesToCreateNewComing.add(nextDate);
//        }
//
//        return allDatesToCreateNewComing;
//    }
//}
