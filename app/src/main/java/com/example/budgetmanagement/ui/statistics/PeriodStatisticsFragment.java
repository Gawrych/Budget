package com.example.budgetmanagement.ui.statistics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.FragmentPeriodStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.github.mikephil.charting.charts.BarChart;

import java.util.Calendar;

public class PeriodStatisticsFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private FragmentPeriodStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    private DatePickerDialog datePickerDialog;
    private int selectedYear;
    private PeriodBarChart barChart;
    private PeriodStats periodStats;
    private Calendar currentDate;
    private int selectedChartBar;
    private BottomSheetMonthYearPicker datesPicker;
    private int currentMonth;
    private int currentYear;
    private PeriodSummary[] periodSummary;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPeriodStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        currentDate = Calendar.getInstance();
        this.currentMonth = currentDate.get(Calendar.MONTH);
        this.currentYear = currentDate.get(Calendar.YEAR);

        setCurrentYearAsStartValueToChartAndStats();

        setMode(MONTHS_STATS_MODE);

        binding.monthsButton.setOnClickListener(v -> {
            setMode(MONTHS_STATS_MODE);
        });

        binding.yearButton.setOnClickListener(v -> {
            setMode(YEARS_STATS_MODE);
        });

        binding.yearPicker.setOnClickListener(v -> changeYearForChartMonths());
    }

    private void setMode(int newMode) {
        if (newMode == MONTHS_STATS_MODE) {
            setChartToMonthPeriod();
            showStatsFromSelectedChartBar(this.currentDate.get(Calendar.MONTH));
            showYearPicker();
        } else if (newMode == YEARS_STATS_MODE) {
            setChartToYearPeriod();
            showStatsFromSelectedChartBar(0);
            hideYearPicker();
        }
    }

    private void setChartToMonthPeriod() {
        BarChart chartView = new BarChart(requireContext());
        barChart = new PeriodBarChart(binding, chartView);

        barChart.setOnValueSelected(selectedPos -> this.selectedChartBar = selectedPos);

        MonthsStatsCollector monthsStatsCollector = new MonthsStatsCollector(comingViewModel);
        periodSummary = monthsStatsCollector.getStats(this.selectedYear);
        barChart.setMonthsAsLabels(DateProcessor.getShortMonths());
        barChart.setData(periodSummary);
        barChart.notifyDataChanged();

        barChart.setOnValueSelected(this::showStatsFromSelectedChartBar);
    }

    private void setChartToYearPeriod() {
        BarChart chartView = new BarChart(requireContext());
        barChart = new PeriodBarChart(binding, chartView);

        YearsStatsCollector yearsStatsCollector = new YearsStatsCollector(comingViewModel);
        periodSummary = yearsStatsCollector.getStats().values().toArray(new PeriodSummary[0]);
        barChart.setYearsAsLabels(yearsStatsCollector.getYears());
        barChart.setData(periodSummary);
        barChart.notifyDataChanged();

        barChart.setOnValueSelected(this::showStatsFromSelectedChartBar);
    }

    private void showStatsFromSelectedChartBar(int selectedValue) {
        PeriodSummary selectedPeriodSummary = periodSummary[selectedValue];
        binding.allTransactionNumber.setText(String.valueOf(selectedPeriodSummary.getNumberOfTransactions()));
        binding.numberOfTransactionsExecutedAfterTheTime.setText(String.valueOf(selectedPeriodSummary.getNumberOfTransactionsExecutedAfterTheTime()));
        binding.numberOfRemainingTransaction.setText(String.valueOf(selectedPeriodSummary.getNumberOfRemainingTransactions()));
    }

    private void changeYearForChartMonths() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {}, this.selectedYear, mMonth, mDay);

        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().getTouchables().get(1).setVisibility(View.GONE);
        datePickerDialog.getDatePicker()
                .setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                    this.selectedYear = year;
                    binding.yearPicker.setText(String.valueOf(year));
                    setChartToMonthPeriod();
                    showStatsFromSelectedChartBar(this.currentDate.get(Calendar.MONTH));
                    datePickerDialog.cancel();
                });
        datePickerDialog.show();
    }

    private void setCurrentYearAsStartValueToChartAndStats() {
        this.selectedYear = currentDate.get(Calendar.YEAR);
    }

    private void showYearPicker() {
        binding.yearPicker.setVisibility(View.VISIBLE);
    }

    private void hideYearPicker() {
        binding.yearPicker.setVisibility(View.INVISIBLE);
    }
}