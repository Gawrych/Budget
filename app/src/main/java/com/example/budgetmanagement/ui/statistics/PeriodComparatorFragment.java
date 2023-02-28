package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTHS_AND_YEAR_MODE;
import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTH_YEAR_PICKER_TAG;
import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.ONLY_YEAR_MODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.databinding.PeriodsComparatorFragmentBinding;
import com.github.mikephil.charting.charts.BarChart;

public class PeriodComparatorFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private int mode;
    private PeriodsComparatorFragmentBinding binding;
    private PeriodComparatorElementsCreator periodElementsCreator;
    private LinearLayout chartLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PeriodsComparatorFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.binding.setPeriodComparatorFragment(this);
        this.chartLayout = this.binding.chartLayout;

        this.periodElementsCreator =
                new PeriodComparatorElementsCreator(requireContext(), this);

        this.periodElementsCreator.getDatesPicker()
                .setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
                    this.periodElementsCreator.setNewDates(firstYear, firstMonth, secondYear, secondMonth);
            updateData();
        });

        setMonthsMode();
    }

    public void onClickSelectDate() {
        this.periodElementsCreator.getDatesPicker()
                .show(getParentFragmentManager(), MONTH_YEAR_PICKER_TAG);
    }

    public void onClickOnlyYearModeCheckbox(boolean isChecked) {
        if (isChecked) {
            setYearsMode();
        } else {
            setMonthsMode();
        }
    }

    public void swapPeriods() {
        this.periodElementsCreator.swapDates();
        updateData();
    }

    private void setMonthsMode() {
        this.mode = MONTHS_STATS_MODE;
        this.periodElementsCreator.getDatesPicker().changeMode(MONTHS_AND_YEAR_MODE);
        updateData();
    }

    private void setYearsMode() {
        this.mode = YEARS_STATS_MODE;
        this.periodElementsCreator.getDatesPicker().changeMode(ONLY_YEAR_MODE);
        updateData();
    }

    private void updateData() {
        setSummaryStats();
        setModeLabelsForChart();

        BarChart barChart = this.periodElementsCreator.createBarChart();

        this.chartLayout.removeAllViews();
        this.chartLayout.addView(barChart);

        setValuesInDetailsSection();
    }

    private void setSummaryStats() {
        if (mode == MONTHS_STATS_MODE) {
            this.periodElementsCreator.setMonthsSummaryStats();
        } else {
            this.periodElementsCreator.setYearsSummaryStats();
        }
    }

    private void setModeLabelsForChart() {
        if (mode == MONTHS_STATS_MODE) {
            this.periodElementsCreator.setMonthsModeLabelsForChart();
        } else {
            this.periodElementsCreator.setYearsModeLabelsForChart();
        }
    }

    private void setValuesInDetailsSection() {
        PeriodStatsComparator statsComparator = this.periodElementsCreator.getStatsComparator();
        PeriodComparatorValues values = new PeriodComparatorValues(statsComparator);
        this.binding.setPeriodComparatorValues(values);
    }
}