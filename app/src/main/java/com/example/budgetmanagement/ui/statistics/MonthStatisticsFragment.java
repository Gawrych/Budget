package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.MONTH_YEAR_PICKER;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;
import java.util.List;

public class MonthStatisticsFragment extends Fragment {

    private static final int NUMBER_OF_MONTHS = 12;
    private MonthStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    private final MonthStatsSummary[] monthStatsSummary = new MonthStatsSummary[NUMBER_OF_MONTHS];
    private Calendar currentDate;
    private int selectedStartYear = 0;
    private int selectedStartMonthNumber = 0;



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

        comingViewModel =
                new ViewModelProvider(this).get(ComingViewModel.class);

        setButtons();

        currentDate = Calendar.getInstance();
        selectedStartMonthNumber = currentDate.get(Calendar.MONTH);

        List<ComingAndTransaction> allComingFromCurrentYear =
                comingViewModel.getAllComingByYear(currentDate.get(Calendar.YEAR));

        for (int i = 0; i < monthStatsSummary.length; i++) {
            monthStatsSummary[i] = new MonthStatsSummary();
        }

        Calendar calendar = Calendar.getInstance();
        for (ComingAndTransaction element : allComingFromCurrentYear) {
            long expireDate = element.coming.getExpireDate();
            calendar.setTimeInMillis(expireDate);
            monthStatsSummary[calendar.get(Calendar.MONTH)].add(element);
        }

        PeriodBarChart barChart = new PeriodBarChart(binding, monthStatsSummary);
        barChart.drawChart();
        barChart.setChartStats();

        PeriodStats periodStats = new PeriodStats(binding, monthStatsSummary);
        periodStats.notifyPeriodChanged();

        binding.startDate.setOnClickListener(v ->
                periodStats.getStartPeriodMonthYearPicker().show(getParentFragmentManager(), MONTH_YEAR_PICKER));

        binding.endDate.setOnClickListener(v ->
                periodStats.getEndPeriodMonthYearPicker().show(getParentFragmentManager(), MONTH_YEAR_PICKER));
    }

    private void setButtons() {
        binding.yearButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Year button", Toast.LENGTH_SHORT).show();
        });

        binding.monthsButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Month button", Toast.LENGTH_SHORT).show();
        });
    }
}