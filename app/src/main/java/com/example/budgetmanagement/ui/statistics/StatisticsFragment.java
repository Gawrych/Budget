package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.StatisticsFragmentBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = StatisticsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

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

        setNextTransactionData(binding.nextIncomeAmount, binding.nextIncomeRemainingDays, nextIncome, true);
        setNextTransactionData(binding.nextPaymentAmount, binding.nextPaymentRemainingDays, nextPayment, false);
    }

    private void setNextTransactionData(TextView amountView, TextView daysView, ComingAndTransaction transaction, boolean isIncome) {
        if (transaction == null) {
            amountView.setText("0");
            daysView.setText("");
            return;
        }

        String amount = (isIncome ? "+" : "-") + getAmountWithCurrency(removeTrailingZeros(transaction.transaction.getAmount()));
        amountView.setText(amount);

        int remainingDays = getRemainingDays(transaction.coming.getExpireDate());
        daysView.setText(getDaysAmountWithRemainingDaysLabel(remainingDays));
    }


    private String removeTrailingZeros(String amount) {
        return new BigDecimal(amount).setScale(0, RoundingMode.HALF_UP).abs().toPlainString();
    }

    private String getDaysAmountWithRemainingDaysLabel(int daysNumber) {
        return getString(R.string.in_number_days, daysNumber);
    }

    private String getAmountWithCurrency(String amount) {
        String currency = getString(R.string.polish_currency);
        return getString(R.string.amount_with_currency, amount, currency);
    }

    private int getRemainingDays(long finalDate) {
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(finalDate);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = otherDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}