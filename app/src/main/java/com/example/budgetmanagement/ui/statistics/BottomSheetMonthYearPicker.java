package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.database.adapters.BottomMonthSelectorAdapter;
import com.example.budgetmanagement.databinding.BottomSheetMonthYearPickerBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetMonthYearPicker extends BottomSheetDialogFragment
        implements BottomMonthSelectorAdapter.OnSelectedListener {

    public static final String MONTH_YEAR_PICKER = "monthYearPicker";
    public static final String BUNDLE_YEAR = "yearBundleTag";
    public static final String BUNDLE_MONTH = "monthBundleTag";
    public static final String PICKER_MODE = "pickerMode";
    public static final int ONLY_MONTHS_MODE = 0;
    public static final int MONTHS_AND_YEAR_MODE = 1;
    private BottomSheetMonthYearPickerBinding binding;
    private OnMonthAndYearSelectedListener monthAndYearListener;
    private int year = -1;
    private int month = -1;
    private int mode = -1;

    public static BottomSheetMonthYearPicker newInstance(int mode, int year, int month) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_YEAR, year);
        bundle.putInt(BUNDLE_MONTH, month);
        bundle.putInt(PICKER_MODE, mode);
        BottomSheetMonthYearPicker bottomSheetMonthYearPicker = new BottomSheetMonthYearPicker();
        bottomSheetMonthYearPicker.setArguments(bundle);
        return bottomSheetMonthYearPicker;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BottomSheetMonthYearPickerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        notifyYearChanged();

        final BottomMonthSelectorAdapter adapter = new BottomMonthSelectorAdapter(this, this.month);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),4);
        RecyclerView recyclerView = binding.monthsItems;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        setArrowsListenerToChangeYear();

        setMode();
    }

    private void setMode() {
        if (mode == ONLY_MONTHS_MODE) binding.yearGroup.setVisibility(View.GONE);
    }

    private void getDataFromBundle() {
        if (this.mode == -1 || this.year == -1 || this.month == -1) {
            this.mode = getArguments() != null ? getArguments().getInt(PICKER_MODE) : 0;
            this.year = getArguments() != null ? getArguments().getInt(BUNDLE_YEAR) : 0;
            this.month = getArguments() != null ? getArguments().getInt(BUNDLE_MONTH) : 0;
        }
    }

    private void notifyYearChanged() {
        binding.year.setText(String.valueOf(this.year));
    }

    private void setArrowsListenerToChangeYear() {
        binding.leftArrow.setOnClickListener(v -> {
            this.year--;
            notifyYearChanged();
        });

        binding.rightArrow.setOnClickListener(v -> {
            this.year++;
            notifyYearChanged();
        });
    }

    public void setOnDateSelectedListener(OnMonthAndYearSelectedListener listener) {
        this.monthAndYearListener = listener;
    }

    @Override
    public void onContentSelected(int position) {
        this.month = position;
        monthAndYearListener.onMonthAndYearSelected(this.year, position);
        dismiss();
    }

    public interface OnMonthAndYearSelectedListener {
        void onMonthAndYearSelected(int year, int month);
    }
}