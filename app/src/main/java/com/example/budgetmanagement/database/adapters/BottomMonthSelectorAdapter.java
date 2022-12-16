package com.example.budgetmanagement.database.adapters;

import static com.example.budgetmanagement.ui.statistics.BottomSheetMonthYearPicker.ONLY_YEAR_MODE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.DateProcessor;

public class BottomMonthSelectorAdapter extends RecyclerView.Adapter<BottomMonthSelectorAdapter.BottomMonthSelectorHolder> {

    private final String[] months;
    private final OnSelectedListener listener;
    private final int monthToSetChecked;
    private boolean disableMonths;
    private CheckBox selectedMonth;
    public CheckBox[] monthsCheckBox = new CheckBox[12];

    public BottomMonthSelectorAdapter(OnSelectedListener listener, int monthToSetChecked, int mode) {
        this.listener = listener;
        this.disableMonths = mode == ONLY_YEAR_MODE;
        this.monthToSetChecked = monthToSetChecked;
        months = DateProcessor.getShortMonths();
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
        holder.bind(position, setChecked);
    }

    @Override
    public int getItemCount() {
        return months.length;
    }

    class BottomMonthSelectorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox month;

        public BottomMonthSelectorHolder(View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month);
            itemView.setOnClickListener(this);
            month.setOnClickListener(this);
        }

        public void bind(int position, boolean setChecked) {
            monthsCheckBox[position] = month;
            month.setText(months[position]);
            if (setChecked) {
                month.setChecked(true);
                selectedMonth = month;
            }
            month.setEnabled(!disableMonths);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                CheckBox newSelectedMonth = (CheckBox) v;
                if (newSelectedMonth != selectedMonth) {
                    selectedMonth.setChecked(false);
                    selectedMonth = month;
                } else {
                    selectedMonth.setChecked(true);
                }
                listener.onContentSelected(getBindingAdapterPosition());
            }
        }
    }

    public void setMonthByPosition(int month) {
        CheckBox newSelectedMonth = monthsCheckBox[month];
        selectedMonth.setChecked(false);
        newSelectedMonth.setChecked(true);
        selectedMonth = newSelectedMonth;
    }

    public interface OnSelectedListener {
        void onContentSelected(int position);
    }
}