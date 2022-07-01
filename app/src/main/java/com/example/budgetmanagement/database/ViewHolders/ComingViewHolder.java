package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;

public class ComingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textField;
    private TextView amountField;
    private TextView dateField;
    private TextView currency;
    private ComingViewHolder.OnNoteListener onNoteListener;

    public ComingViewHolder(View itemView, ComingViewHolder.OnNoteListener onNoteListener) {
        super(itemView);
        textField = itemView.findViewById(R.id.title);
        amountField = itemView.findViewById(R.id.amount);
        dateField = itemView.findViewById(R.id.date);
        currency = itemView.findViewById(R.id.currency);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String title, String amount, long repeatDate) {
        this.textField.setText(title);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.amountField, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        this.amountField.setText(amount);
        this.dateField.setText(DateProcessor.getDate(repeatDate));
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
