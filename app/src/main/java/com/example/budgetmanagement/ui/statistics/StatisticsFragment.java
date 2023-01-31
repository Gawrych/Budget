package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.StatisticsFragmentBinding;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class StatisticsFragment extends Fragment {

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
        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        comingViewModel.getAllComingAndTransaction().observe(getViewLifecycleOwner(), this::setGlobalStats);

        binding.periodStatistics.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_navigation_statistics_to_periodStatisticsFragment));

        binding.periodComparatorStatisticsCardView.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_navigation_statistics_to_periodComparatorFragment));
    }

    private void setGlobalStats(List<ComingAndTransaction> allTransactions) {
        GlobalStatsSummary globalStats = new GlobalStatsSummary(allTransactions);

        binding.allTransactionsNumber.setText(String.valueOf(globalStats.getNumberOfTransactions()));
        binding.allTransactionsAfterTheTimeNumber.setText(String.valueOf(globalStats.getNumberOfTransactionsAfterTheTime()));

        ComingAndTransaction nextIncome = globalStats.getNextIncomeTransaction();
        ComingAndTransaction nextPayment = globalStats.getNextPaymentTransaction();

        int nextIncomeRemainingDays = 0;
        String nextIncomeAmount = "0";
        if (nextIncome != null) {
            nextIncomeAmount = getAmountWithPlus(getAmountWithCurrency(removeTrailingZeros(nextIncome.transaction.getAmount())));
            nextIncomeRemainingDays = getRemainingDays(nextIncome.coming.getExpireDate());
        }

        int nextPaymentRemainingDays = 0;
        String nextPaymentAmount = "0";
        if (nextPayment != null) {
            nextPaymentAmount = getAmountWithMinus(getAmountWithCurrency(removeTrailingZeros(nextPayment.transaction.getAmount())));
            nextPaymentRemainingDays = getRemainingDays(nextPayment.coming.getExpireDate());
        }

        binding.nextIncomeAmount.setText(nextIncomeAmount);
        binding.nextPaymentAmount.setText(nextPaymentAmount);

        binding.nextIncomeRemainingDays.setText(getDaysAmountWithRemainingDaysLabel(nextIncomeRemainingDays));
        binding.nextPaymentRemainingDays.setText(getDaysAmountWithRemainingDaysLabel(nextPaymentRemainingDays));
    }

    private String removeTrailingZeros(String amount) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        return bigDecimal.abs().stripTrailingZeros().toPlainString();
    }

    private String getAmountWithPlus(String amount) {
        return getString(R.string.number_with_plus, amount);
    }

    private String getAmountWithMinus(String amount) {
        return getString(R.string.number_with_minus, amount);
    }

    private String getDaysAmountWithRemainingDaysLabel(int daysNumber) {
        return getString(R.string.in_number_days, daysNumber);
    }

    private String getAmountWithCurrency(String amount) {
        String currency = getString(R.string.polish_currency);
        return getString(R.string.amount_with_currency, amount, currency);
    }

    private int getRemainingDays(long finalDate) {
        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(finalDate);

        DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
        DateTime startDate = new DateTime(todayDate.getTimeInMillis(), defaultTimeZone);
        DateTime endDate = new DateTime(otherDate.getTimeInMillis(), defaultTimeZone);
        return Days.daysBetween(startDate.withTimeAtStartOfDay(), endDate.withTimeAtStartOfDay()).getDays();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}