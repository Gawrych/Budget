package com.example.budgetmanagement.database.ViewHolders;

import static com.example.budgetmanagement.MainActivity.DEFAULT_DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titleField;
    private TextView amountField;
    private TextView dateField;
    private TextView currency;
    private HistoryViewHolder.OnNoteListener onNoteListener;

    public HistoryViewHolder(View itemView, HistoryViewHolder.OnNoteListener onNoteListener) {
        super(itemView);
        titleField = itemView.findViewById(R.id.title);
        amountField = itemView.findViewById(R.id.amount);
        dateField = itemView.findViewById(R.id.createDate);
        currency = itemView.findViewById(R.id.currency);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String title, String amount, long date) {
        this.titleField.setText(title);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.amountField, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        this.amountField.setText(amount);
        this.dateField.setText(getDateInPattern(date));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateInPattern(long repeatDate) {
        return getFormatterPattern().format(convertLongToLocalDate(repeatDate));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DateTimeFormatter getFormatterPattern() {
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate convertLongToLocalDate(long repeatDate) {
        return LocalDate.ofEpochDay(repeatDate);
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
