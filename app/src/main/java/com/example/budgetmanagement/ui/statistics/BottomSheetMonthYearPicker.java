
package com.example.budgetmanagement.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.database.adapters.BottomMonthSelectorAdapter;
import com.example.budgetmanagement.database.adapters.CategoryAdapter;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.databinding.BottomSheetMonthYearPickerBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.maltaisn.icondialog.pack.IconPack;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class BottomSheetMonthYearPicker extends BottomSheetDialogFragment implements BottomMonthSelectorAdapter.OnSelectedListener {

    public static final String MONTH_YEAR_PICKER = "monthYearPicker";
    private BottomSheetMonthYearPickerBinding binding;
    private OnMonthAndYearSelectedListener monthAndYearListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BottomSheetMonthYearPickerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView;
        recyclerView = binding.recyclerView;

        final BottomMonthSelectorAdapter adapter = new BottomMonthSelectorAdapter(this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    public void setOnDateSelectedListener(OnMonthAndYearSelectedListener listener) {
        this.monthAndYearListener = listener;
    }

    @Override
    public void onContentSelected(int position) {
        monthAndYearListener.onMonthAndYearSelected(1, position);
        dismiss();
    }

    public interface OnMonthAndYearSelectedListener {
        void onMonthAndYearSelected(int year, int month);
    }
}