package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.Category.AppIconPack;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.Calendar;

public class TransactionFormService extends Fragment implements GetViewTransactionFields {

    private CategoryBottomSheetSelector categoryBottomSheetSelector;
    private int categoryId = 1;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateField;
    private TextInputEditText title;
    private TextInputEditText amount;
    private TextInputLayout titleLayout;
    private TextInputLayout amountLayout;
    private SwitchMaterial profitSwitch;
    private TextInputLayout categorySelectorLayout;
    private TextInputEditText selectedCategory;
    private IconPack iconPack;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_form_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        selectedCategory = rootView.findViewById(R.id.categorySelector);
        categorySelectorLayout = rootView.findViewById(R.id.categorySelectorLayout);
        title = rootView.findViewById(R.id.title);
        titleLayout = rootView.findViewById(R.id.titleLayout);
        amount = rootView.findViewById(R.id.amount);
        amountLayout = rootView.findViewById(R.id.amountLayout);
        profitSwitch = rootView.findViewById(R.id.profitSwitch);
        dateField = rootView.findViewById(R.id.startDate);
        dateField.setCursorVisible(false);

        categoryBottomSheetSelector = new CategoryBottomSheetSelector(this);

        datePickerDialog = setDatePickerDialog(Calendar.getInstance());

        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        clearErrorWhenTextChanged(title, titleLayout);
        clearErrorWhenTextChanged(amount, amountLayout);

        iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        selectedCategory.setCursorVisible(false);
        selectedCategory.setText(rootView.getResources().getString(R.string.category_example_various));
        selectedCategory.setOnClickListener(view -> selectCategory(selectedCategory));

        dateField.setText(DateProcessor.getTodayDateInPattern(MONTH_NAME_YEAR_DATE_FORMAT));
        dateField.setOnClickListener(view -> {
            serviceDatePickerDialog();
            datePickerDialog.show();
        });
    }

    private void serviceDatePickerDialog() {
        Calendar selectedDate = Calendar.getInstance();
        datePickerDialog.setOnDateSetListener((v, year, monthOfYear, dayOfMonth) -> {
            selectedDate.set(year, monthOfYear, dayOfMonth);
            dateField.setText(
                    DateProcessor.parseDate((selectedDate.getTimeInMillis()), MONTH_NAME_YEAR_DATE_FORMAT));
        });
    }

    public void clearErrorWhenTextChanged(EditText fieldToListenTextChange, TextInputLayout fieldToBeCleared) {
        fieldToListenTextChange.addTextChangedListener(getTextWatcher(fieldToBeCleared));
    }

    private TextWatcher getTextWatcher(TextInputLayout fieldToBeCleared) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                fieldToBeCleared.setError(null);
            }
        };
    }

    public DatePickerDialog setDatePickerDialog(Calendar calendarInstance) {
        int mYear = calendarInstance.get(Calendar.YEAR);
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    dateField.setText(DateProcessor.parseDate((selectedDate.getTimeInMillis())));
                }, mYear, mMonth, mDay);
    }

    private void selectCategory(EditText categoryEditText) {
        categoryBottomSheetSelector.show();
        categoryBottomSheetSelector.getBottomSheetDialog().setOnDismissListener(v -> {
            categoryId = categoryBottomSheetSelector.getSelectedId();
            categoryEditText.setText(categoryBottomSheetSelector.getSelectedName());
            categorySelectorLayout.setEndIconDrawable(getIcon(categoryBottomSheetSelector.getIconId()));
        });
    }

    private Drawable getIcon(int categoryIconId) {
        Icon icon = iconPack.getIcon(categoryIconId);
        if (icon == null) {
            return ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_icon_not_found_24, null);
        }
        return icon.getDrawable();
    }

    public DatePickerDialog getDatePickerDialog() {
        return datePickerDialog;
    }

    public void setCategoryId(int id) {
        this.categoryId = id;
    }

    @Override
    public Context getFragmentContext() {
        return requireContext();
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public TextInputEditText getStartDateField() {
        return dateField;
    }

    @Override
    public SwitchMaterial getProfitSwitch() {
        return profitSwitch;
    }

    @Override
    public TextInputEditText getTitleField() {
        return title;
    }

    @Override
    public TextInputEditText getSelectedCategory() {
        return selectedCategory;
    }

    @Override
    public TextInputLayout getTitleLayoutField() {
        return titleLayout;
    }

    @Override
    public TextInputEditText getAmountField() {
        return amount;
    }

    @Override
    public TextInputLayout getAmountLayoutField() {
        return amountLayout;
    }
}