package com.example.budgetmanagement.ui.statistics;

import static com.example.budgetmanagement.ui.statistics.MonthStatisticsFragment.AUTUMN;
import static com.example.budgetmanagement.ui.statistics.MonthStatisticsFragment.SPRING;
import static com.example.budgetmanagement.ui.statistics.MonthStatisticsFragment.SUMMER;
import static com.example.budgetmanagement.ui.statistics.MonthStatisticsFragment.WINTER;
import static com.example.budgetmanagement.ui.utils.DateProcessor.FULL_MONTH_NAME_ONLY;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.database.viewmodels.StatisticsViewModel;
import com.example.budgetmanagement.databinding.StatisticsFragmentBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    private StatisticsFragmentBinding binding;
    private ComingViewModel comingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = StatisticsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comingViewModel =
                new ViewModelProvider(this).get(ComingViewModel.class);

        statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);


        binding.monthBalance.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_navigation_statistics_to_monthStatistics));
        setMonthCardView();

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();

        entriesGroup1.add(new BarEntry(0, 70));
        entriesGroup1.add(new BarEntry(0, 30));
        entriesGroup2.add(new BarEntry(1, 60));
        entriesGroup2.add(new BarEntry(1, 40));

        BarDataSet set1 = new BarDataSet(entriesGroup1, "Styczeń");
        BarDataSet set2 = new BarDataSet(entriesGroup2, "Luty");

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
// (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        BarChart barChart = binding.barChart;
        barChart.setData(data);
        barChart.groupBars(20f, groupSpace, barSpace); // perform the "explicit" grouping
        barChart.invalidate();
    }

    private void setMonthCardView() {
        Calendar currentDate = Calendar.getInstance();
        int currentMonthNumber = currentDate.get(Calendar.MONTH);

        List<ComingAndTransaction> allComingFromCurrentMonth =
                comingViewModel.getAllComingByYear(currentMonthNumber);

        String currentMonthName =
                DateProcessor.parseDate(currentDate.getTimeInMillis(), FULL_MONTH_NAME_ONLY);
        String currentMonthNameCapitalized =
                currentMonthName.substring(0, 1).toUpperCase() + currentMonthName.substring(1);

        int season = getSeason(currentMonthNumber);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), getMonthIconRes(season), null);
        binding.monthIcon.setImageDrawable(drawable);
        binding.monthName.setText(currentMonthNameCapitalized);
    }

    private String getAmountWithCurrency(String amount) {
        String currency = getResources().getString(R.string.polish_currency);
        return String.format(getResources().getString(R.string.amount_with_currency), amount, currency);
    }

    private int getSeason(int monthNumber) {
        if (monthNumber >= Calendar.MARCH && monthNumber <= Calendar.MAY) {
            return SPRING;
        } else if (monthNumber >= Calendar.JUNE && monthNumber <= Calendar.OCTOBER) {
            return SUMMER;
        } else if (monthNumber >= Calendar.SEPTEMBER && monthNumber <= Calendar.NOVEMBER) {
            return AUTUMN;
        } else {
            return WINTER;
        }
    }

    private int getMonthIconRes(int season) {
        if (season == WINTER) {
            return R.drawable.snowflake;
        } else if (season == SPRING) {
            return R.drawable.butterflies;
        } else if (season == SUMMER) {
            return R.drawable.sun;
        } else if (season == AUTUMN) {
            return R.drawable.leaves;
        }
        return R.drawable.ic_outline_icon_not_found_24;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setPieChart() {
//        PieChart pieChart = binding.pieChart;
//        Description description = new Description();
//        description.setText("");
//        pieChart.setDescription(description);
//
//        pieChart.setHoleRadius(0);
//        pieChart.setDrawHoleEnabled(false);
//        pieChart.setUsePercentValues(true);
//
//        Legend legend = pieChart.getLegend();
//        legend.setEnabled(false);
//
//        List<PieEntry> pieChartData = new ArrayList<>();
//        pieChartData.add(new PieEntry(300F));
//        pieChartData.add(new PieEntry(1700F));
//
//        PieDataSet dataSet = new PieDataSet(pieChartData, "Balance");
//        dataSet.setColors(new int[] {R.color.mat_green, R.color.mat_red}, requireContext());
//
//        PieData pieData = new PieData(dataSet);
//
//        pieChart.setData(pieData);
//        pieChart.invalidate();
    }
}