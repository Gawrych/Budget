package com.example.budgetmanagement.database.viewholders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private TextView text;
    private ImageView imageView;
    private OnNoteListener onNoteListener;

    public CategoryViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
        imageView = itemView.findViewById(R.id.categoryIcon);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bind(Drawable icon, String title, String budget) {
        text.setText(title);
        imageView.setImageDrawable(icon);
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getBindingAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        onNoteListener.onLongNoteClick(getBindingAdapterPosition());
        return true;
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
        void onLongNoteClick(int position);
    }
}