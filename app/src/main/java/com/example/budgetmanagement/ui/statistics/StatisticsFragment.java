package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.StatisticsFragmentBinding;

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

        binding.periodStatistics.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_navigation_statistics_to_periodStatisticsFragment));

        binding.periodComparator.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_navigation_statistics_to_periodComparatorFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}