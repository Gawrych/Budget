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
import android.widget.LinearLayout;

import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.FragmentPeriodComparatorBinding;
import com.github.mikephil.charting.charts.BarChart;

public class PeriodComparatorFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private int mode;
    private FragmentPeriodComparatorBinding binding;
    private PeriodComparatorElementsCreator periodElementsCreator;
    private LinearLayout chartLayout;

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
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        chartLayout = binding.chartLayout;

        periodElementsCreator =
                new PeriodComparatorElementsCreator(requireContext(), transactionViewModel);

        setMonthsMode();

        binding.selectDate.setOnClickListener(v -> periodElementsCreator.getDatesPicker()
                .show(getParentFragmentManager(), MONTH_YEAR_PICKER_TAG));

        periodElementsCreator.getDatesPicker()
                .setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            periodElementsCreator.setNewDates(firstYear, firstMonth, secondYear, secondMonth);
            updateData();
        });

        binding.onlyYearCheckBox.setOnClickListener(v -> {
            if (binding.onlyYearCheckBox.isChecked()) {
                setYearsMode();
            } else {
                setMonthsMode();
            }
        });

        binding.swapPeriods.setOnClickListener(v -> {
            periodElementsCreator.swapDates();
            updateData();
        });
    }

    private void setMonthsMode() {
        this.mode = MONTHS_STATS_MODE;
        periodElementsCreator.getDatesPicker().changeMode(MONTHS_AND_YEAR_MODE);
        updateData();
    }

    private void setYearsMode() {
        this.mode = YEARS_STATS_MODE;
        periodElementsCreator.getDatesPicker().changeMode(ONLY_YEAR_MODE);
        updateData();
    }

    private void updateData() {
        setSummaryStats();
        setModeLabelsForChart();

        BarChart barChart = periodElementsCreator.createBarChart();

        chartLayout.removeAllViews();
        chartLayout.addView(barChart);

        setValuesInDetailsSection();
    }

    private void setSummaryStats() {
        if (mode == MONTHS_STATS_MODE) {
            periodElementsCreator.setMonthsSummaryStats();
        } else {
            periodElementsCreator.setYearsSummaryStats();
        }
    }

    private void setModeLabelsForChart() {
        if (mode == MONTHS_STATS_MODE) {
            periodElementsCreator.setMonthsModeLabelsForChart();
        } else {
            periodElementsCreator.setYearsModeLabelsForChart();
        }
    }

    private void setValuesInDetailsSection() {
        PeriodStatsComparator statsComparator = periodElementsCreator.getStatsComparator();
        PeriodComparatorValues values = new PeriodComparatorValues(requireContext(), statsComparator);
        binding.setPeriodComparatorValues(values);
    }
}