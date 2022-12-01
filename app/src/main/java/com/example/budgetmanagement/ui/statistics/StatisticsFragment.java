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
}