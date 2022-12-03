package com.example.budgetmanagement.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.MonthStatisticsBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthStatisticsFragment extends Fragment implements OnChartValueSelectedListener {

    private static final int NUMBER_OF_MONTHS = 12;
    private MonthStatisticsBinding binding;
    private ComingViewModel comingViewModel;
    private ArrayList<String> monthsNames;
    private final MonthStatsSummary[] monthStatsSummary = new MonthStatsSummary[NUMBER_OF_MONTHS];
    private Calendar currentDate;
    private int selectedMonthNumber = 0;

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

        setButtons();

        currentDate = Calendar.getInstance();
        selectedMonthNumber = currentDate.get(Calendar.MONTH);

        List<ComingAndTransaction> allComingFromCurrentYear =
                comingViewModel.getAllComingByYear(currentDate.get(Calendar.YEAR));

        for (int i = 0; i < monthStatsSummary.length; i++) {
            monthStatsSummary[i] = new MonthStatsSummary();
        }

        Calendar calendar = Calendar.getInstance();
        for (ComingAndTransaction element : allComingFromCurrentYear) {
            long expireDate = element.coming.getExpireDate();
            calendar.setTimeInMillis(expireDate);
            monthStatsSummary[calendar.get(Calendar.MONTH)].add(element);
        }
        groupBarChart(monthStatsSummary);

        bindData();
    }

    private void bindData() {
        MonthStatsSummary selectedMonthStatsSummary = monthStatsSummary[selectedMonthNumber];
        binding.allTransactionNumber.setText(String.valueOf(selectedMonthStatsSummary.getNumberOfTransactions()));
        binding.numberOfTransactionsExecutedAfterTheTime.setText(String.valueOf(selectedMonthStatsSummary.getNumberOfTransactionsExecutedAfterTheTime()));
        binding.numberOfRemainingTransaction.setText(String.valueOf(selectedMonthStatsSummary.getNumberOfRemainingTransactions()));

        if (selectedMonthNumber == 0) {
            return;
        }

        PeriodStatsComparator statsComparator = new PeriodStatsComparator(selectedMonthStatsSummary, monthStatsSummary[selectedMonthNumber-1]);
        binding.profitIncrease.setText(statsComparator.getIncome() + "%");

    }


    private void setButtons() {
        binding.yearButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Year button", Toast.LENGTH_SHORT).show();

        });

        binding.monthsButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Month button", Toast.LENGTH_SHORT).show();
        });
    }

    public void groupBarChart(MonthStatsSummary[] monthStatsSummary) {
        monthsNames = new ArrayList<>();
        monthsNames.add("Chart is skipping this line, but it have to be here");
        monthsNames.addAll(DateProcessor.getMonthInShort());
        monthsNames.add("Chart is skipping this line, but it have to be here");

        BarChart mChart = binding.barChart;
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthsNames));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        ArrayList<BarEntry> loss = new ArrayList<>();
        ArrayList<BarEntry> profit = new ArrayList<>();
        for (int i = 0; i < monthStatsSummary.length; i++) {
            loss.add(new BarEntry(i, monthStatsSummary[i].getLoss()));
            profit.add(new BarEntry(i, monthStatsSummary[i].getProfit()));
        }

        BarDataSet set1 = new BarDataSet(loss, "loss");
        set1.setHighlightEnabled(true);
        set1.setColors(new int[]{R.color.mat_red}, requireContext());
        set1.setHighLightColor(Color.BLACK);
        BarDataSet set2 = new BarDataSet(profit, "profit");
        set2.setHighlightEnabled(true);
        set2.setColors(new int[]{R.color.mat_green}, requireContext());
        set2.setHighLightColor(Color.BLACK);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.2f;
        float barSpace = 0.1f;
        float barWidth = 0.3f;
        data.setBarWidth(barWidth);
        xAxis.setAxisMaximum(monthsNames.size() - 1.1f);
        mChart.setData(data);
        mChart.setExtraBottomOffset(10);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(4f);
        mChart.moveViewToX(monthsNames.size());
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        selectedMonthNumber = (int) (e.getX() - 1);
        bindData();
    }

    @Override
    public void onNothingSelected() {}
}