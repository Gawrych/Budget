package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.Coming.OnNoteListener;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.Calendar;

public class ComingChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textField;
    private final TextView amountField;
    private final TextView dateField;
    private final TextView currency;
    private final ImageView outOfDateIcon;
    private OnNoteListener noteListener;

    public ComingChildViewHolder(View itemView, OnNoteListener noteListener) {
        super(itemView);
        this.noteListener = noteListener;
        textField = itemView.findViewById(R.id.titleLayout);
        amountField = itemView.findViewById(R.id.amountLayout);
        dateField = itemView.findViewById(R.id.repeatDate);
        currency = itemView.findViewById(R.id.currency);
        outOfDateIcon = itemView.findViewById(R.id.outOfDateIcon);
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String title, String amount, long repeatDate, short validity, boolean execute) {
        this.textField.setText(title);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.amountField, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        this.amountField.setText(amount);
        this.dateField.setText(DateProcessor.parseDate(repeatDate));

        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(repeatDate);

        if (otherDate.before(todayDate) && !execute) {
            outOfDateIcon.setVisibility(View.VISIBLE);
            outOfDateIcon.setImageResource(R.drawable.ic_baseline_event_busy_24);
        } else {
            outOfDateIcon.setVisibility(View.GONE);
        }

        if (execute) {
            outOfDateIcon.setVisibility(View.VISIBLE);
            outOfDateIcon.setImageResource(R.drawable.ic_baseline_done_all_24);
        }
    }

    @Override
    public void onClick(View v) {
        noteListener.onNoteClick(getBindingAdapterPosition());
    }
}
