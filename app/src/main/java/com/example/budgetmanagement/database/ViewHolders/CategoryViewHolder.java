package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView text;
    private TextView idTextView;
    private OnNoteListener onNoteListener;

    public CategoryViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
        idTextView = itemView.findViewById(R.id.idTextView);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(int aCategoryId, String aTitle, int aBudget) {
        text.setText(aTitle);
        idTextView.setText(String.valueOf(aCategoryId));
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}