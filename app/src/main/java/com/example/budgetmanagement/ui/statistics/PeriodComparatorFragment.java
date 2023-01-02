package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;
import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTH_YEAR_PICKER_TAG;
import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.ONLY_YEAR_MODE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.FragmentPeriodComparatorBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class PeriodComparatorFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private FragmentPeriodComparatorBinding binding;
    private ComingViewModel comingViewModel;
    private PeriodStats periodStats;
    private Calendar currentDate;
    private BottomSheetMonthYearPicker datesPicker;
    private int currentMonth;
    private int currentYear;
    private int secondMonth;
    private int secondYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPeriodComparatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

//        TODO: Add bar charts to each stats group

        currentDate = Calendar.getInstance();
        this.currentMonth = currentDate.get(Calendar.MONTH);
        this.currentYear = currentDate.get(Calendar.YEAR);

        this.secondMonth = currentMonth - 1;
        this.secondYear = currentYear;

        if (this.currentMonth == Calendar.JANUARY) {
            changeSecondDatesToDecemberLastYear();
        }

        setTwoPeriodCompareStats(MONTHS_STATS_MODE);
    }

    private void changeSecondDatesToDecemberLastYear() {
        this.secondMonth = Calendar.DECEMBER;
        this.secondYear = currentYear - 1;
    }

    private void setTwoPeriodCompareStats(int mode) {
        periodStats = new PeriodStats(binding, comingViewModel);

        if (mode == MONTHS_STATS_MODE) {
            initializeMonthAndYearPeriodPicker();
            periodStats.setData(currentYear, currentMonth, secondYear, secondMonth);
            periodStats.setMonthsSummaryStats();
        } else if (mode == YEARS_STATS_MODE) {
            initializeYearPeriodPicker();
            periodStats.setData(currentYear, 0, currentYear - 1, 0);
            periodStats.setYearsSummaryStats();
        }

        binding.selectDate.setOnClickListener(v ->
                datesPicker.show(getParentFragmentManager(), MONTH_YEAR_PICKER_TAG));
    }

    private void initializeMonthAndYearPeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, currentYear, currentMonth, secondYear, secondMonth);
        datesPicker.setCancelable(false);

        String[] shortMonths = DateProcessor.getMonths();

        binding.firstDateYear.setText(String.valueOf(this.currentYear));
        binding.secondDateYear.setText(String.valueOf(this.secondYear));
        binding.firstDateMonth.setText(shortMonths[this.currentMonth]);
        binding.secondDateMonth.setText(shortMonths[this.secondMonth]);
        datesPicker.setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            periodStats.setData(firstYear, firstMonth, secondYear, secondMonth);
            periodStats.setMonthsSummaryStats();
            binding.firstDateYear.setText(String.valueOf(firstYear));
            binding.secondDateYear.setText(String.valueOf(secondYear));
            binding.firstDateMonth.setText(shortMonths[firstMonth]);
            binding.secondDateMonth.setText(shortMonths[secondMonth]);
        });
    }

    private void initializeYearPeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(ONLY_YEAR_MODE, currentYear, 0, currentYear - 1, 0);
        datesPicker.setCancelable(false);

        datesPicker.setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            periodStats.setData(firstYear, firstMonth, secondYear, secondMonth);
            periodStats.setYearsSummaryStats();
            binding.firstDateYear.setText(String.valueOf(firstYear));
            binding.secondDateYear.setText(String.valueOf(secondYear));
        });
    }
}