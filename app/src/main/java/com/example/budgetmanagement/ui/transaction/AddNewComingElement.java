package com.example.budgetmanagement.ui.transaction;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.AddNewComingFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.Calendar;

public class AddNewComingElement extends Fragment {

    public static final String BUNDLE_COMING_ID = "comingId";
    private AddNewComingFragmentBinding binding;
    private ArrayAdapter<String> adapter;
    private final Calendar selectedStartDate = Calendar.getInstance();
    private final Calendar selectedEndDate = Calendar.getInstance();
    private CategoryBottomSheetSelector categoryPicker;
    private long categoryId;
    private IconPack iconPack;
    private String title;
    private String amount;
    private String period;

    //    Remove this
    public static AddNewComingElement newInstance(int comingId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COMING_ID, comingId);
        AddNewComingElement addNewComingElement = new AddNewComingElement();
        addNewComingElement.setArguments(bundle);
        return addNewComingElement;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AddNewComingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TransactionViewModel transactionViewModel =
                new ViewModelProvider(this).get(TransactionViewModel.class);

        binding.cyclicalSwitch.setOnCheckedChangeListener((button, isChecked) ->
                disableOrEnabledCyclicalFields(isChecked));
        categoryPicker = new CategoryBottomSheetSelector(this);
        iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        prepareFields();

        TransactionCollector collector = new TransactionCollector(requireContext());

        binding.acceptButton.setOnClickListener(v -> {
            collectData(collector);
            if (collector.isCorrect()) {
                transactionViewModel.insert(this.title, this.amount,this.categoryId,
                        this.selectedStartDate);
                close();
            }
            collector.reset();
        });
    }

    private void disableOrEnabledCyclicalFields(boolean enabled) {
        binding.periodPickerLayout.setEnabled(enabled);
        binding.endDateLayout.setEnabled(enabled);
    }

    private void prepareFields() {
        initializeDatePicker(binding.startDate, selectedStartDate);
        initializeDatePicker(binding.endDate, selectedEndDate);
        initializeCategoryPicker(binding.categorySelector, binding.categorySelectorLayout);
        initializePeriodPicker(binding.periodPicker);
    }

    public DatePickerDialog setDatePickerDialog(AutoCompleteTextView field, Calendar selectedField) {
        Calendar today = Calendar.getInstance();
        int mYear = today.get(Calendar.YEAR);
        int mMonth = today.get(Calendar.MONTH);
        int mDay = today.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedField.set(year, monthOfYear, dayOfMonth);
                    field.setText(DateProcessor.parseDate((
                            selectedField.getTimeInMillis()),
                            DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT));
                }, mYear, mMonth, mDay);
    }

    private void initializeCategoryPicker(AutoCompleteTextView categorySelector, TextInputLayout categorySelectorLayout) {
        categorySelector.setOnClickListener(v -> categoryPicker.show());
        categoryPicker.getBottomSheetDialog().setOnDismissListener(v -> {
            this.categoryId = categoryPicker.getSelectedId();
            setTextForField(
                    categoryPicker.getSelectedName(),
                    categorySelector);
            setIconForField(
                    getDrawableIconFromIconPack(categoryPicker.getIconId()),
                    categorySelectorLayout);
        });
    }

    private void setTextForField(String text, AutoCompleteTextView field) {
        field.setText(text);
    }

    private void setIconForField(Drawable icon, TextInputLayout field) {
        field.setEndIconDrawable(icon);
    }

    private Drawable getDrawableIconFromIconPack(int categoryIconId) {
        Icon icon = this.iconPack.getIcon(categoryIconId);
        return icon != null ? icon.getDrawable() : ResourcesCompat.
                getDrawable(getResources(), R.drawable.ic_outline_icon_not_found_24, null);
    }

    private void initializePeriodPicker(AutoCompleteTextView periodPicker) {
        String[] TIME_BETWEEN = getResources().getStringArray(R.array.periods);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, TIME_BETWEEN);
        periodPicker.setAdapter(adapter);
    }

    private void initializeDatePicker(AutoCompleteTextView field, Calendar selectedField) {
        DatePickerDialog datePickerDialog = setDatePickerDialog(field, selectedField);
        field.setOnClickListener(v -> datePickerDialog.show());
    }

    private void collectData(TransactionCollector collector) {
        this.title = collector.collect(binding.titleLayout);
        this.amount = collector.collect(binding.amountLayout);
        collector.collect(binding.categorySelectorLayout);
        collector.collect(binding.startDateLayout);

        if (binding.cyclicalSwitch.isChecked()) {
            this.period = collector.collect(binding.periodPickerLayout);
            collector.collect(binding.endDateLayout);
        }
    }

    private void close() {
        requireActivity().onBackPressed();
    }

//    public ArrayList<Long> getNextDates() {
//        ArrayList<Long> allDatesToCreateNewComing = new ArrayList<>();
//        SwitchMaterial cyclicalSwitch = fieldsInterface.getCyclicalSwitch();
//
//        if (!cyclicalSwitch.isChecked()) {
//            allDatesToCreateNewComing.add(getStartDate());
//            return allDatesToCreateNewComing;
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(getStartDate());
//        long nextDate = calendar.getTimeInMillis();
//
//        Integer valueFromTimeBetweenMap = timeBetweenValues.get(timeBetweenExecutePicker.getText().toString());
//        if (valueFromTimeBetweenMap == null) {
//            return allDatesToCreateNewComing;
//        }
//
//        int timeBetween = valueFromTimeBetweenMap;
//        long endDate = getEndDate();
//
//        while (nextDate <= endDate) {
//            allDatesToCreateNewComing.add(nextDate);
//            calendar.add(timeBetween, 1);
//            nextDate = calendar.getTimeInMillis();
//        }
//
//        return allDatesToCreateNewComing;
//    }
//
//    private void submitCyclical(int amountOfNewDates) {
//        int MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING = 2;
//        if (amountOfNewDates < MIN_AMOUNT_OF_DATES_TO_CREATE_CYCLICAL_COMING) {
//            endDateLayout.setError(getString(R.string.not_enough_to_generate_cyclical_change_endDate_or_timeBetween));
//            return;
//        }
//
//        submitNewComingItemToDatabase(newComingDataCollector, dates);
//
//        String howMuchAdded = "Dodano " + amountOfNewDates + " transakcje";
//        Toast.makeText(requireContext(), howMuchAdded, Toast.LENGTH_SHORT).show();
//    }
//
//    private void submitNewComingItemToDatabase(NewComingDataCollector newComing, ArrayList<Long> dates) {
//        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
//        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
//
//        long transactionId = transactionViewModel.insert(newComing.getTransaction());
//
//        for (Long date : dates) {
//            comingViewModel.insert(newComing.getComing(transactionId, date));
//        }
//        close();
//    }
}