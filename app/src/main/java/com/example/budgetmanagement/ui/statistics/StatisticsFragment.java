package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.StatisticsFragmentBinding;

import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsFragmentBinding binding;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = StatisticsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.binding.setStatisticsFragment(this);

        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.getAllTransactions().observe(getViewLifecycleOwner(), this::setGlobalStats);
    }

    public void openPeriodStatistics() {
        Navigation.findNavController(this.view).navigate(
                R.id.action_navigation_statistics_to_periodStatisticsFragment);
    }

    public void openPeriodComparatorStatistics() {
        Navigation.findNavController(this.view).navigate(
                R.id.action_navigation_statistics_to_periodComparatorFragment);
    }

    private void setGlobalStats(List<Transaction> allTransactions) {
        GlobalStatsSummary globalStats = new GlobalStatsSummary(allTransactions);
        this.binding.setGlobalStats(globalStats);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}