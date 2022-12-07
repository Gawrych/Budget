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

import java.util.Calendar;

public class MonthStatisticsFragment extends Fragment {

    private MonthStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    private DatePickerDialog datePickerDialog;
    private int selectedYear;
    private PeriodBarChart barChart;
    private MonthsStats monthsStats;


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
        setButtons();
        int[] allYears = comingViewModel.getAllYears();

        Calendar currentDate = Calendar.getInstance();
        this.selectedYear = currentDate.get(Calendar.YEAR);
        monthsStats = new MonthsStats(comingViewModel);

        PeriodSummary[] monthsData = monthsStats.getMonthsStatsFromYear(this.selectedYear);
        barChart = new PeriodBarChart(binding);
        barChart.setData(monthsData);
        barChart.notifyDataChanged();

        PeriodStats periodStats = new PeriodStats(binding, comingViewModel);
        periodStats.notifyPeriodChanged();

        binding.startDate.setOnClickListener(v ->
                periodStats.getStartPeriodMonthYearPicker().show(getParentFragmentManager(), MONTH_YEAR_PICKER));

        binding.endDate.setOnClickListener(v ->
                periodStats.getEndPeriodMonthYearPicker().show(getParentFragmentManager(), MONTH_YEAR_PICKER));

        binding.yearPicker.setOnClickListener(v -> changeYearToChart());
    }

    private void setButtons() {
        binding.yearButton.setOnClickListener(v -> {
//            PeriodBarChart barChart = new PeriodBarChart(binding, comingViewModel);
//            barChart.collectYearsData();
//            barChart.drawChart();
//            barChart.setChartStats();
        });

        binding.monthsButton.setOnClickListener(v -> {
//            PeriodBarChart barChart = new PeriodBarChart(binding, comingViewModel);
//            barChart.collectMonthsData(this.selectedYear);
//            barChart.drawChart();
//            barChart.setChartStats();
        });
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
                    PeriodSummary[] monthsStats = this.monthsStats.getMonthsStatsFromYear(year);
                    barChart.setData(monthsStats);
                    barChart.notifyDataChanged();
                    datePickerDialog.cancel();
                });
        datePickerDialog.show();
    }
}