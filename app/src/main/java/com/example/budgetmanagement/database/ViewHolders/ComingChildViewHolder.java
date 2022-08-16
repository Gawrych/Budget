package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.Coming.onNoteListener;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;

public class ComingChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textField;
    private final TextView amountField;
    private final TextView dateField;
    private final TextView currency;
    private onNoteListener noteListener;

    public ComingChildViewHolder(View itemView) {
        super(itemView);
        textField = itemView.findViewById(R.id.title);
        amountField = itemView.findViewById(R.id.amount);
        dateField = itemView.findViewById(R.id.createDate);
        currency = itemView.findViewById(R.id.currency);
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String title, String amount, long repeatDate, onNoteListener noteListener) {
        this.noteListener = noteListener;
        this.textField.setText(title);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.amountField, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        this.amountField.setText(amount);
        this.dateField.setText(DateProcessor.getDate(repeatDate));
    }

    @Override
    public void onClick(View v) {
        noteListener.onNoteClick(getBindingAdapterPosition());
    }
}
