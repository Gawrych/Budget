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
    public void bind(String aTitle, float aAmount, long aDate) {
        title.setText(aTitle);
        amount.setText(String.valueOf(aAmount));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        date.setText(LocalDate.ofEpochDay(aDate).format(formatter));
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
