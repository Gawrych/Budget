package com.example.budgetmanagement.ui.statistics;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.PeriodStatisticsFragmentBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class PeriodStatisticsFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private PeriodStatisticsFragmentBinding binding;
    private TransactionViewModel transactionViewModel;
    private DatePickerDialog datePickerDialog;
    private int selectedYear;
    private PeriodStatisticsBarChart barChart;
    private Calendar currentDate;
    private PeriodSummary[] periodSummary;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PeriodStatisticsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setPeriodStatisticsFragment(this);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        currentDate = Calendar.getInstance();
        setCurrentYearAsStartValueToChartAndStats();
        setMode(MONTHS_STATS_MODE);
        showStatsFromSelectedChartBar(currentDate.get(Calendar.MONTH));
    }

    public void onCLickYearPicker() {
        changeYearForChartMonths();
    }

    public void onClickOnlyYearModeCheckbox(boolean isChecked) {
        setMode(isChecked ? YEARS_STATS_MODE : MONTHS_STATS_MODE);
    }

    private void setMode(int newMode) {
        if (newMode == MONTHS_STATS_MODE) {
            setChartToMonthPeriod();
            showStatsFromSelectedChartBar(currentDate.get(Calendar.MONTH));
            showYearPicker();
        } else if (newMode == YEARS_STATS_MODE) {
            setChartToYearPeriod();
            showStatsFromSelectedChartBar(0);
            hideYearPicker();
        }
    }

    private void setChartToMonthPeriod() {
        MonthsStatsCollector monthsStatsCollector = new MonthsStatsCollector(transactionViewModel);
        periodSummary = monthsStatsCollector.getStats(selectedYear);

        barChart = new PeriodStatisticsBarChart(binding);
        barChart.setMonthsAsLabels(DateProcessor.getShortMonths());
        barChart.setData(periodSummary);
        barChart.setPositionToMoveView(currentDate.get(Calendar.MONTH));
        barChart.drawChart();
        barChart.setOnValueSelected(this::showStatsFromSelectedChartBar);
    }

    private void setChartToYearPeriod() {
        YearsStatsCollector yearsStatsCollector = new YearsStatsCollector(transactionViewModel);
        periodSummary = yearsStatsCollector.getStats().values().toArray(new PeriodSummary[0]);

        barChart = new PeriodStatisticsBarChart(binding);
        barChart.setYearsAsLabels(yearsStatsCollector.getYears());
        barChart.setData(periodSummary);
        barChart.drawChart();
        barChart.setOnValueSelected(this::showStatsFromSelectedChartBar);
    }

    private void showStatsFromSelectedChartBar(int selectedValue) {
        PeriodSummary selectedPeriodSummary = periodSummary[selectedValue];
        binding.setPeriodSummary(selectedPeriodSummary);
    }

    private void changeYearForChartMonths() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {}, selectedYear, mMonth, mDay);

        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().getTouchables().get(1).setVisibility(View.GONE);
        datePickerDialog.getDatePicker()
                .setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                    selectedYear = year;
                    binding.setYearPickerValue(Integer.toString(year));
                    setChartToMonthPeriod();
                    showStatsFromSelectedChartBar(currentDate.get(Calendar.MONTH));
                    datePickerDialog.cancel();
                });
        datePickerDialog.show();
    }

    private void setCurrentYearAsStartValueToChartAndStats() {
        selectedYear = currentDate.get(Calendar.YEAR);
    }

    private void showYearPicker() {
        binding.setIsYearPickerClickable(true);
    }

    private void hideYearPicker() {
        binding.setIsYearPickerClickable(false);
    }

    public void showInfo() {
        // TODO: Add info dialog
    }
}