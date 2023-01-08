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
                new PeriodComparatorElementsCreator(binding, comingViewModel);

        setDatePickerValues();
        setMonthsMode();

        binding.selectDate.setOnClickListener(v -> periodElementsCreator.getDatesPicker()
                .show(getParentFragmentManager(), MONTH_YEAR_PICKER_TAG));

        periodElementsCreator.getDatesPicker()
                .setOnDateSelectedListener((firstYear, firstMonth, secondYear, secondMonth) -> {
            periodElementsCreator.setNewDates(firstYear, firstMonth, secondYear, secondMonth);
            updateData();
            setDatePickerValues();
        });

        binding.onlyYearCheckBox.setOnClickListener(v -> {
            if (binding.onlyYearCheckBox.isChecked()) {
                setYearsMode();
            } else {
                setMonthsMode();
            }
        });
    }

    private void setDatePickerValues() {
        binding.firstDateYear.setText(periodElementsCreator.getFirstYear());
        binding.secondDateYear.setText(periodElementsCreator.getSecondYear());
        binding.firstDateMonth.setText(periodElementsCreator.getFirstMonth());
        binding.secondDateMonth.setText(periodElementsCreator.getSecondMonth());
    }

    private void setMonthsMode() {
        this.mode = MONTHS_STATS_MODE;
        periodElementsCreator.getDatesPicker().changeMode(MONTHS_AND_YEAR_MODE);
        updateData();
        showMonths();
    }

    private void setYearsMode() {
        this.mode = YEARS_STATS_MODE;
        periodElementsCreator.getDatesPicker().changeMode(ONLY_YEAR_MODE);
        updateData();
        hideMonths();
    }

    private void hideMonths() {
        binding.firstDateMonth.setTextColor(getResources().getColor(R.color.white_30, null));
        binding.secondDateMonth.setTextColor(getResources().getColor(R.color.white_30, null));
    }

    private void showMonths() {
        binding.firstDateMonth.setTextColor(getResources().getColor(R.color.white, null));
        binding.secondDateMonth.setTextColor(getResources().getColor(R.color.white, null));
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
        setValuesInTextFields();
    }

    private void setValuesInTextFields() {
        PeriodStatsComparator statsComparator = periodElementsCreator.getStatsComparator();
        binding.amountOfIncomeIncrease
                .setText(getAmountWithCurrency(statsComparator.getObtainedIncome()));
        binding.amountOfProfitIncrease
                .setText(getAmountWithCurrency(statsComparator.getObtainedProfit()));
        binding.amountOfLossIncrease
                .setText(getAmountWithCurrency(statsComparator.getObtainedLoss()));

        binding.incomeIncrease
                .setText(getAmountWithCurrency(statsComparator.getPercentIncome()));
        binding.profitIncrease
                .setText(getAmountWithCurrency(statsComparator.getPercentProfit()));
        binding.lossIncrease
                .setText(getAmountWithCurrency(statsComparator.getPercentLoss()));
    }

    private String getAmountWithCurrency(int amount) {
        return getString(R.string.amount_with_percent, amount);
    }
}