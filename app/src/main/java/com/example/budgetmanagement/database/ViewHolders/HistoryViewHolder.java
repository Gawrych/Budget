package com.example.budgetmanagement.database.ViewHolders;

import static com.example.budgetmanagement.MainActivity.DATE_FORMAT;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView title;
    private TextView amount;
    private TextView date;
    private HistoryViewHolder.OnNoteListener onNoteListener;

    public HistoryViewHolder(View itemView, HistoryViewHolder.OnNoteListener onNoteListener) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        amount = itemView.findViewById(R.id.amount);
        date = itemView.findViewById(R.id.createDate);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String title, float amount, long date) {
        this.title.setText(title);
        this.amount.setText(String.valueOf(amount));

        this.date.setText(getDateInPattern(date));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateInPattern(long repeatDate) {
        return getFormatterPattern().format(convertLongToLocalDate(repeatDate));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DateTimeFormatter getFormatterPattern() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT);
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
