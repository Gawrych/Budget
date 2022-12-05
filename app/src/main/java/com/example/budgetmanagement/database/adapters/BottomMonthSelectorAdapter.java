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

    private String[] months;
    private OnSelectedListener listener;

    public BottomMonthSelectorAdapter(OnSelectedListener listener) {
        this.listener = listener;
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
        holder.bind(months[position]);
    }

    @Override
    public int getItemCount() {
        return months.length;
    }

    class BottomMonthSelectorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CheckBox monthCheckBox;

        public BottomMonthSelectorHolder(View itemView) {
            super(itemView);
            monthCheckBox = itemView.findViewById(R.id.month);
            itemView.setOnClickListener(this);
            monthCheckBox.setOnClickListener(this);
        }

        public void bind(String shortMonth) {
            monthCheckBox.setText(shortMonth);
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