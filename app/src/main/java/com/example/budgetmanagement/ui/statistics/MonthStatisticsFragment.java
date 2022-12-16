package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTH_YEAR_PICKER;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class MonthStatisticsFragment extends Fragment {

    private MonthStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    private DatePickerDialog datePickerDialog;
    private int selectedYear;
    private PeriodBarChart barChart;
    private StatsCollector statsCollector;
    private PeriodStats periodStats;
    private Calendar currentDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = MonthStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        currentDate = Calendar.getInstance();
        this.selectedYear = currentDate.get(Calendar.YEAR);

        statsCollector = new StatsCollector(comingViewModel);

        setMonthsAsPeriod();
        setButtons();

        binding.yearPicker.setOnClickListener(v -> changeYearToChart());
    }

    private void setButtons() {
        binding.yearButton.setOnClickListener(v -> {
            setYearAsPeriod();
        });

        binding.monthsButton.setOnClickListener(v -> {
            setMonthsAsPeriod();
        });
    }

    private void setMonthsAsPeriod() {
        barChart = new PeriodBarChart(binding, comingViewModel);
        PeriodSummary[] monthsPeriodSummary = statsCollector.getMonthsStats(this.selectedYear);
        barChart.setMonthsAsLabels(DateProcessor.getShortMonths());
        barChart.setData(monthsPeriodSummary);
        barChart.setSelectedValue(this.currentDate.get(Calendar.MONTH));
        barChart.notifyDataChanged();

        periodStats = new PeriodStats(binding, comingViewModel);
        periodStats.setMonthsPeriod();
        periodStats.notifyPeriodChanged();

        binding.startDate.setOnClickListener(v ->
                periodStats.getDatesPicker().show(getParentFragmentManager(), MONTH_YEAR_PICKER));
    }

    private void setYearAsPeriod() {
        barChart = new PeriodBarChart(binding, comingViewModel);
        PeriodSummary[] yearPeriodSummary = statsCollector.getYearStats();
        barChart.setYearsAsLabels(statsCollector.getYears());
        barChart.setData(yearPeriodSummary);
        barChart.setSelectedValue(0);
        barChart.notifyDataChanged();

        periodStats = new PeriodStats(binding, comingViewModel);
        periodStats.setYearsPeriod();
        periodStats.notifyPeriodChanged();

        binding.startDate.setOnClickListener(v ->
                periodStats.getDatesPicker().show(getParentFragmentManager(), MONTH_YEAR_PICKER));
    }

    private void changeYearToChart() {
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
                    PeriodSummary[] periodSummary = statsCollector.getMonthsStats(this.selectedYear);
                    barChart.setData(periodSummary);
                    barChart.notifyDataChanged();
                    datePickerDialog.cancel();
                });
        datePickerDialog.show();
    }
}