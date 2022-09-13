package com.example.budgetmanagement.database.ViewHolders;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;

public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView titleField;
    private final TextView amountField;
    private final TextView dateField;
    private final TextView currency;
    private final HistoryViewHolder.OnNoteListener onNoteListener;

    public HistoryViewHolder(View itemView, HistoryViewHolder.OnNoteListener onNoteListener) {
        super(itemView);
        titleField = itemView.findViewById(R.id.titleLayout);
        amountField = itemView.findViewById(R.id.amountLayout);
        dateField = itemView.findViewById(R.id.repeatDate);
        currency = itemView.findViewById(R.id.currency);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    public void bind(String title, String amount, long date) {
        this.titleField.setText(title);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.amountField, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        this.amountField.setText(amount);
        this.dateField.setText(DateProcessor.parseDate(date, MONTH_NAME_YEAR_DATE_FORMAT));
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
