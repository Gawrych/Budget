package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthStatisticsFragment extends Fragment {

    private MonthStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    public static final int WINTER = 0;
    public static final int SPRING = 1;
    public static final int SUMMER = 2;
    public static final int AUTUMN = 3;

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

        Calendar currentDate = Calendar.getInstance();
        List<ComingAndTransaction> allComingFromCurrentMonth =
                comingViewModel.getAllComingByMonth(currentDate.get(Calendar.MONTH));

        PieChart pieChart = binding.pieChart;

        List<PieEntry> pieChartData = new ArrayList<>();
        pieChartData.add(new PieEntry(30F, "Przychody"));
        pieChartData.add(new PieEntry(70F, "Wydatki"));

        PieDataSet dataSet = new PieDataSet(pieChartData, "Balance");
        dataSet.setColors(new int[] {R.color.mat_green, R.color.mat_red}, requireContext());

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}