package com.example.budgetmanagement.database.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.Coming.OnNoteListener;

public class SimpleBottomSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView name;
    private final OnNoteListener onNoteListener;

    public SimpleBottomSheetViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    public void bind(String name) {
        this.name.setText(name);
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getBindingAdapterPosition());
    }
}
