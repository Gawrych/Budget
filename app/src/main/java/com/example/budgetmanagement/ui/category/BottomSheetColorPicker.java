package com.example.budgetmanagement.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.database.adapters.BottomColorPickerAdapter;
import com.example.budgetmanagement.database.adapters.BottomMonthSelectorAdapter;
import com.example.budgetmanagement.databinding.ColorPickerBottomSheetBinding;
import com.example.budgetmanagement.databinding.MonthYearPickerBottomSheetBinding;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetColorPicker extends BottomSheetDialogFragment
        implements BottomColorPickerAdapter.OnSelectedListener {

    public static final String BUNDLE_COLOR_POSITION = "colorPosition";
    public static final String BOTTOM_SHEET_COLOR_TAG = "bottomSheetColorPicker";
    private ColorPickerBottomSheetBinding binding;
    private OnColorSelectedListener colorSelectedListener;
    private int selectedColorPos = 0;

    public static BottomSheetColorPicker newInstance(int colorPos) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COLOR_POSITION, colorPos);
        BottomSheetColorPicker bottomSheetColorPicker = new BottomSheetColorPicker();
        bottomSheetColorPicker.setArguments(bundle);
        return bottomSheetColorPicker;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ColorPickerBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int colorToCheck = getArguments() != null ? getArguments().getInt(BUNDLE_COLOR_POSITION) : 0;

        BottomColorPickerAdapter adapter = new BottomColorPickerAdapter(this, requireContext(), colorToCheck);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),4);
        RecyclerView recyclerView = binding.colorItems;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        binding.acceptButton.setOnClickListener(v -> {
            this.colorSelectedListener.onColorSelected(selectedColorPos);
            dismiss();
        });
    }

    public void setOnDateSelectedListener(OnColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }

    @Override
    public void onContentSelected(int position) {
        selectedColorPos = position;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int colorPos);
    }
}