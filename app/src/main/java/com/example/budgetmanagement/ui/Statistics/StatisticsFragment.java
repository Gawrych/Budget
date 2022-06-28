package com.example.budgetmanagement.ui.Statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.database.ViewModels.StatisticsViewModel;
import com.example.budgetmanagement.databinding.StatisticsFragmentBinding;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    private StatisticsFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = StatisticsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.TextViewStatistics;
        statisticsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}