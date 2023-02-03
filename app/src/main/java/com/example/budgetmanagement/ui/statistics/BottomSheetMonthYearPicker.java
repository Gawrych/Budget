package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.database.adapters.BottomMonthSelectorAdapter;
import com.example.budgetmanagement.databinding.MonthYearPickerBottomSheetBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetMonthYearPicker extends BottomSheetDialogFragment
        implements BottomMonthSelectorAdapter.OnSelectedListener {

    public static final String MONTH_YEAR_PICKER_TAG = "monthYearPicker";
    public static final String BUNDLE_FIRST_YEAR = "firstYear";
    public static final String BUNDLE_FIRST_MONTH = "firstMonth";
    public static final String BUNDLE_SECOND_YEAR = "secondYear";
    public static final String BUNDLE_SECOND_MONTH = "secondMonth";
    public static final String PICKER_MODE = "pickerMode";
    public static final int ONLY_MONTHS_MODE = 0;
    public static final int ONLY_YEAR_MODE = 1;
    public static final int MONTHS_AND_YEAR_MODE = 2;
    private static final int FIRST_PERIOD = 0;
    private static final int SECOND_PERIOD = 1;
    private MonthYearPickerBottomSheetBinding binding;
    private OnMonthAndYearSelectedListener monthAndYearListener;
    private final String[] shortMonths = DateProcessor.getShortMonths();
    private int mode = -1;
    private int firstYear = -1;
    private int firstMonth = -1;
    private int secondYear = -1;
    private int secondMonth = -1;
    private int period = FIRST_PERIOD;
    private BottomMonthSelectorAdapter adapter;
    private int firstYearToRestoreIfCanceled;
    private int firstMonthToRestoreIfCanceled;
    private int secondYearToRestoreIfCanceled;
    private int secondMonthToRestoreIfCanceled;


    public static BottomSheetMonthYearPicker newInstance(int mode, int firstYear, int firstMonth,
                                                         int secondYear, int secondMonth) {
        Bundle bundle = new Bundle();
        bundle.putInt(PICKER_MODE, mode);
        bundle.putInt(BUNDLE_FIRST_YEAR, firstYear);
        bundle.putInt(BUNDLE_FIRST_MONTH, firstMonth);
        bundle.putInt(BUNDLE_SECOND_YEAR, secondYear);
        bundle.putInt(BUNDLE_SECOND_MONTH, secondMonth);
        BottomSheetMonthYearPicker bottomSheetMonthYearPicker = new BottomSheetMonthYearPicker();
        bottomSheetMonthYearPicker.setArguments(bundle);
        return bottomSheetMonthYearPicker;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = MonthYearPickerBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setModeFromBundle();
        setDatesFromBundle();
        setDatesInButtons();
        saveValuesToRestoreIfCanceled();

        adapter = new BottomMonthSelectorAdapter(this, getMonthToSelect(), this.mode);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),4);
        RecyclerView recyclerView = binding.monthsItems;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        setArrowsListenerToChangeYear();

        binding.firstDate.setOnClickListener(v -> {
            this.period = FIRST_PERIOD;
            setYear(this.firstYear);
            setMonth(this.firstMonth);
        });

        binding.secondDate.setOnClickListener(v -> {
            this.period = SECOND_PERIOD;
            setYear(this.secondYear);
            setMonth(this.secondMonth);
        });

        binding.select.setOnClickListener(v -> {
            monthAndYearListener.onMonthAndYearSelected(
                    this.firstYear, this.firstMonth, this.secondYear, this.secondMonth);
            dismiss();
        });

        binding.cancel.setOnClickListener(v -> {
            restoreValues();
            dismiss();
        });
    }

    private int getMonthToSelect() {
        int selectedPeriodToLetUserChangeMonthOrYear = this.firstMonth;
        if (this.period == SECOND_PERIOD) {
            selectedPeriodToLetUserChangeMonthOrYear = this.secondMonth;
        }
        return selectedPeriodToLetUserChangeMonthOrYear;
    }

    private void saveValuesToRestoreIfCanceled() {
        firstYearToRestoreIfCanceled = firstYear;
        firstMonthToRestoreIfCanceled = firstMonth;
        secondYearToRestoreIfCanceled = secondYear;
        secondMonthToRestoreIfCanceled = secondMonth;
    }

    private void restoreValues() {
        firstYear = firstYearToRestoreIfCanceled;
        firstMonth = firstMonthToRestoreIfCanceled;
        secondYear = secondYearToRestoreIfCanceled;
        secondMonth = secondMonthToRestoreIfCanceled;
    }

    public void setModeFromBundle() {
        if (this.mode == -1) {
            this.mode = getArguments() != null ? getArguments().getInt(PICKER_MODE) : MONTHS_AND_YEAR_MODE;
        }
    }

    public void setDatesFromBundle() {
        if (this.firstYear == -1 || this.firstMonth == -1 ||
                this.secondYear == -1 || this.secondMonth == -1) {
            this.firstYear = getArguments() != null ? getArguments().getInt(BUNDLE_FIRST_YEAR) : 0;
            this.firstMonth = getArguments() != null ? getArguments().getInt(BUNDLE_FIRST_MONTH) : 0;
            this.secondYear = getArguments() != null ? getArguments().getInt(BUNDLE_SECOND_YEAR) : 0;
            this.secondMonth = getArguments() != null ? getArguments().getInt(BUNDLE_SECOND_MONTH) : 0;
        }
    }

    private void setDatesInButtons() {
        String firstDate = this.firstYear + " " + shortMonths[this.firstMonth];
        String secondDate = this.secondYear + " " + shortMonths[this.secondMonth];

        if (this.mode == ONLY_YEAR_MODE) {
            firstDate = String.valueOf(this.firstYear);
            secondDate = String.valueOf(this.secondYear);
        } if (this.mode == ONLY_MONTHS_MODE) {
            firstDate = shortMonths[this.firstMonth];
            secondDate = shortMonths[this.secondMonth];
        }

        binding.firstDate.setText(firstDate);
        binding.secondDate.setText(secondDate);
    }

    private void setYear(int year) {
        binding.year.setText(String.valueOf(year));
    }

    private void setMonth(int month) {
        adapter.setMonthByPosition(month);
    }

    private void setArrowsListenerToChangeYear() {
        binding.leftArrow.setOnClickListener(v -> {
            if (this.period == FIRST_PERIOD) {
                this.firstYear--;
                setYear(this.firstYear);
            } else {
                this.secondYear--;
                setYear(this.secondYear);
            }
            setDatesInButtons();
        });

        binding.rightArrow.setOnClickListener(v -> {
            if (this.period == FIRST_PERIOD) {
                this.firstYear++;
                setYear(this.firstYear);
            } else {
                this.secondYear++;
                setYear(this.secondYear);
            }
            setDatesInButtons();
        });
    }

    public void setOnDateSelectedListener(OnMonthAndYearSelectedListener listener) {
        this.monthAndYearListener = listener;
    }

    @Override
    public void onContentSelected(int position) {
        if (this.period == FIRST_PERIOD) {
            this.firstMonth = position;
        } else {
            this.secondMonth = position;
        }
        setDatesInButtons();
    }

    public interface OnMonthAndYearSelectedListener {
        void onMonthAndYearSelected(int firstYear, int firstMonth, int secondYear, int secondMonth);
    }

    public void swapDates() {
        int firstYearBeforeChanged = this.firstYear;
        int firstMonthBeforeChanged = this.firstMonth;
        int secondYearBeforeChanged = this.secondYear;
        int secondMonthBeforeChanged = this.secondMonth;
        this.firstYear = secondYearBeforeChanged;
        this.firstMonth = secondMonthBeforeChanged;
        this.secondYear = firstYearBeforeChanged;
        this.secondMonth = firstMonthBeforeChanged;
    }

    public void changeMode(int mode) {
        this.mode = mode;
    }
}