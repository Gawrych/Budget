package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ComingChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textField;
    private final TextView amountField;
    private final TextView dateField;
    private final TextView currency;
    private int comingId;
    private BottomSheetDialog bottomSheetDialog;
    private final ComingChildViewHolder.OnNoteListener onNoteListener;

    public ComingChildViewHolder(View itemView, ComingChildViewHolder.OnNoteListener onNoteListener, BottomSheetDialog bottomSheetDialog) {
        super(itemView);
        this.bottomSheetDialog = bottomSheetDialog;
        textField = itemView.findViewById(R.id.title);
        amountField = itemView.findViewById(R.id.amount);
        dateField = itemView.findViewById(R.id.createDate);
        currency = itemView.findViewById(R.id.currency);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
        Log.d("ErrorCheck", "ComingChildViewHolder");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(int comingId, String title, String amount, long repeatDate) {
        this.comingId = comingId;
        this.textField.setText(title);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.amountField, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        this.amountField.setText(amount);
        this.dateField.setText(DateProcessor.getDate(repeatDate));
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(comingId);
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
