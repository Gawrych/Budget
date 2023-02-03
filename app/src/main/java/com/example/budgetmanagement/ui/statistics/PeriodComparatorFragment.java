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

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.FragmentPeriodComparatorBinding;

public class PeriodComparatorFragment extends Fragment {

    private static final int MONTHS_STATS_MODE = 0;
    private static final int YEARS_STATS_MODE = 1;
    private int mode;
    private FragmentPeriodComparatorBinding binding;
    private PeriodComparatorElementsCreator periodElementsCreator;

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
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        periodElementsCreator =
                new PeriodComparatorElementsCreator(requireContext(), binding, comingViewModel);

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
        if (mode == MONTHS_STATS_MODE) {
            periodElementsCreator.setMonthsSummaryStats();
            periodElementsCreator.setMonthsModeLabelsForChart();
        } else {
            periodElementsCreator.setYearsSummaryStats();
            periodElementsCreator.setYearsModeLabelsForChart();
        }

        periodElementsCreator.createBarChart();
        setValuesInDetailsSection();
    }

    private void setValuesInDetailsSection() {
        PeriodStatsComparator statsComparator = periodElementsCreator.getStatsComparator();

        String currency = getResources().getString(R.string.currency);
        binding.percentageOfIncomeIncrease
                .setText(getFormattedAmount(R.string.amount_with_percent_string, getNumberWithSign(statsComparator.getPercentIncome())));
        binding.percentageOfProfitIncrease
                .setText(getFormattedAmount(R.string.amount_with_percent_string, getNumberWithSign(statsComparator.getPercentProfit())));
        binding.percentageOfLossIncrease
                .setText(getFormattedAmount(R.string.amount_with_percent_string, getNumberWithSign(statsComparator.getPercentLoss())));

        binding.averageTimeAfterTheDeadlineFirstPeriod
                .setText(getFormattedAmount(R.string.amount_with_day_label, String.valueOf(statsComparator.getFirstPeriod().getAverageTimeAfterTheDeadlineInDays())));
        binding.averageTimeAfterTheDeadlineSecondPeriod
                .setText(getFormattedAmount(R.string.amount_with_day_label, String.valueOf(statsComparator.getSecondPeriod().getAverageTimeAfterTheDeadlineInDays())));
        binding.payOnTimeInPercentageFirstPeriod
                .setText(getFormattedAmount(R.string.amount_with_percent, statsComparator.getFirstPeriod().getPercentageOfTransactionsExecutedOnTime()));
        binding.payOnTimeInPercentageSecondPeriod
                .setText(getFormattedAmount(R.string.amount_with_percent, statsComparator.getSecondPeriod().getPercentageOfTransactionsExecutedOnTime()));

        binding.amountOfIncomeIncrease
                .setText(getFormattedAmount(R.string.amount_with_currency, getNumberWithSign(statsComparator.getObtainedIncome()), currency));
        binding.amountOfProfitIncrease
                .setText(getFormattedAmount(R.string.amount_with_currency, getNumberWithSign(statsComparator.getObtainedProfit()), currency));
        binding.amountOfLossIncrease
                .setText(getFormattedAmount(R.string.amount_with_currency, getNumberWithSign(statsComparator.getObtainedLoss()), currency));
        binding.averageGrowthTimeAfterTheDeadline
                .setText(getFormattedAmount(R.string.amount_with_day_label, getNumberWithSign(statsComparator.getGrowthAverageTimeAfterTheDeadlineInDays())));
        binding.growthPayOnTimeInPercentagePoints
                .setText(getFormattedAmount(R.string.amount_with_percentage_points, getNumberWithSign(statsComparator.getGrowthOfPercentOfTransactionsExecutedOnTime())));
    }

    private String getFormattedAmount(int formatResId, Object... args) {
        return getString(formatResId, args);
    }

    private String getNumberWithSign(float number) {
        String numberInString = String.valueOf(Math.round(number));
        if (number > 0) {
            return getFormattedAmount(R.string.number_with_plus, numberInString);
        }
        return numberInString;
    }
}