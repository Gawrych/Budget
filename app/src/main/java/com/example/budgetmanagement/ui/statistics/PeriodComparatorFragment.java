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
import java.util.HashMap;

public class PeriodComparatorFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private FragmentPeriodComparatorBinding binding;
    private ComingViewModel comingViewModel;
    private BottomSheetMonthYearPicker datesPicker;
    private int firstMonth;
    private int firstYear;
    private int secondMonth;
    private int secondYear;
    private int mode;
    private PeriodStatsComparator statsComparator;

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
        mode = MONTHS_STATS_MODE;

        setMode();

        binding.selectDate.setOnClickListener(v ->
                datesPicker.show(getParentFragmentManager(), MONTH_YEAR_PICKER_TAG));
    }

    private void setMode() {
        if (mode == MONTHS_STATS_MODE) {
            setMonthsModeDefaultDates();
            initializeMonthAndYearPeriodPicker();
        } else if (mode == YEARS_STATS_MODE) {
            setYearsModeDefaultDates();
            initializeOnlyYearPeriodPicker();
        }
        updateData();
    }

    private void updateData() {
        if (mode == MONTHS_STATS_MODE) {
            this.statsComparator = getMonthsSummaryStats();
        } else {
            this.statsComparator = getYearsSummaryStats();
        }

        createBarChart();
        setValuesInTextFields();
    }

    private void setMonthsModeDefaultDates() {
        Calendar currentDate = Calendar.getInstance();
        this.firstMonth = currentDate.get(Calendar.MONTH);
        this.firstYear = currentDate.get(Calendar.YEAR);

        Calendar secondDate = Calendar.getInstance();
        secondDate.add(Calendar.MONTH, -1);
        this.secondMonth = secondDate.get(Calendar.MONTH);
        this.secondYear = secondDate.get(Calendar.YEAR);
    }

    private void setYearsModeDefaultDates() {
        Calendar currentDate = Calendar.getInstance();
        this.firstMonth = currentDate.get(Calendar.MONTH);
        this.firstYear = currentDate.get(Calendar.YEAR);

        Calendar secondDate = Calendar.getInstance();
        secondDate.add(Calendar.YEAR, -1);
        this.secondMonth = currentDate.get(Calendar.MONTH);
        this.secondYear = currentDate.get(Calendar.YEAR);
    }

    public PeriodStatsComparator getMonthsSummaryStats() {
        MonthsStatsCollector monthsStatsCollector = new MonthsStatsCollector(comingViewModel);
        PeriodSummary[] firstPeriodSummary = monthsStatsCollector.getStats(firstYear);
        PeriodSummary[] secondPeriodSummary = monthsStatsCollector.getStats(secondYear);
        return new PeriodStatsComparator(firstPeriodSummary[firstMonth], secondPeriodSummary[secondMonth]);
    }

    public PeriodStatsComparator getYearsSummaryStats() {
        YearsStatsCollector yearsStatsCollector = new YearsStatsCollector(comingViewModel);
        HashMap<Integer, PeriodSummary> yearsSummary = yearsStatsCollector.getStats();
        return new PeriodStatsComparator(yearsSummary.get(firstYear), yearsSummary.get(secondYear));
    }

    private void createBarChart() {
        PeriodComparatorBarChart barChart = new PeriodComparatorBarChart(binding);
        String[] labels = new String[2];

        if (mode == MONTHS_STATS_MODE) {
            String[] months = DateProcessor.getMonths();
            labels[1] = months[firstMonth] + " " + firstYear;
            labels[0] = months[secondMonth] + " " + secondYear;
            barChart.setLabels(labels);
        } else {
            labels[1] = String.valueOf(firstYear);
            labels[0] = String.valueOf(secondYear);
            barChart.setLabels(labels);
        }

        barChart.setData(this.statsComparator);
        barChart.drawChart();
    }

    private void initializeMonthAndYearPeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(MONTHS_AND_YEAR_MODE, firstYear, firstMonth, secondYear, secondMonth);
        datesPicker.setCancelable(false);

        String[] shortMonths = DateProcessor.getMonths();

        binding.firstDateYear.setText(String.valueOf(this.firstYear));
        binding.secondDateYear.setText(String.valueOf(this.secondYear));
        binding.firstDateMonth.setText(shortMonths[this.firstMonth]);
        binding.secondDateMonth.setText(shortMonths[this.secondMonth]);
        datesPicker.setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            this.mode = MONTHS_STATS_MODE;

            this.firstMonth = firstMonth;
            this.firstYear = firstYear;
            this.secondMonth = secondMonth;
            this.secondYear = secondYear;

            updateData();

            binding.firstDateYear.setText(String.valueOf(firstYear));
            binding.secondDateYear.setText(String.valueOf(secondYear));
            binding.firstDateMonth.setText(shortMonths[firstMonth]);
            binding.secondDateMonth.setText(shortMonths[secondMonth]);
        });
    }

    private void initializeOnlyYearPeriodPicker() {
        datesPicker = BottomSheetMonthYearPicker
                .newInstance(ONLY_YEAR_MODE, firstYear, 0, firstYear - 1, 0);
        datesPicker.setCancelable(false);

        datesPicker.setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            this.mode = YEARS_STATS_MODE;

            this.firstMonth = firstMonth;
            this.firstYear = firstYear;
            this.secondMonth = secondMonth;
            this.secondYear = secondYear;

            updateData();

            binding.firstDateYear.setText(String.valueOf(firstYear));
            binding.secondDateYear.setText(String.valueOf(secondYear));
        });
    }

    private void setValuesInTextFields() {
        binding.amountOfIncomeIncrease.setText(statsComparator.getObtainedIncome()+" zł");
        binding.amountOfProfitIncrease.setText(statsComparator.getObtainedProfit()+" zł");
        binding.amountOfLossIncrease.setText(statsComparator.getObtainedLoss()+" zł");

        binding.incomeIncrease.setText(statsComparator.getPercentIncome() + "%");
        binding.profitIncrease.setText(statsComparator.getPercentProfit() + "%");
        binding.lossIncrease.setText(statsComparator.getPercentLoss() + "%");
    }
}