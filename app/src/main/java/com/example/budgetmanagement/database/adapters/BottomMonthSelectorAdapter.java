package com.example.budgetmanagement.database.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class BottomMonthSelectorAdapter extends RecyclerView.Adapter<BottomMonthSelectorAdapter.BottomMonthSelectorHolder> {

    private final String[] months;
    private final OnSelectedListener listener;
    private final int monthToSetChecked;

    public BottomMonthSelectorAdapter(OnSelectedListener listener, int monthToSetChecked) {
        this.listener = listener;
        this.monthToSetChecked = monthToSetChecked;
        months = new DateFormatSymbols(Locale.getDefault()).getShortMonths();
    }

    @NonNull
    @Override
    public BottomMonthSelectorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.month_year_picker_item, parent, false);
        return new BottomMonthSelectorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomMonthSelectorHolder holder, int position) {
        boolean setChecked = position == monthToSetChecked;
        holder.bind(months[position], setChecked);
    }

    @Override
    public int getItemCount() {
        return months.length;
    }

    class BottomMonthSelectorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox monthCheckBox;

        public BottomMonthSelectorHolder(View itemView) {
            super(itemView);
            monthCheckBox = itemView.findViewById(R.id.month);
            itemView.setOnClickListener(this);
            monthCheckBox.setOnClickListener(this);
        }

        public void bind(String shortMonth, boolean setChecked) {
            monthCheckBox.setText(shortMonth);
            monthCheckBox.setChecked(setChecked);
        }

        @Override
        public void onClick(View v) {
            listener.onContentSelected(getBindingAdapterPosition());
        }
    }

    public interface OnSelectedListener {
        void onContentSelected(int position);
    }
}