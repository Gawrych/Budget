package com.example.budgetmanagement.database.viewholders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private TextView text;
    private ImageView categoryIcon;
    private ImageView budgetIcon;
    private ConstraintLayout cardLayout;
    private OnNoteListener onNoteListener;

    public CategoryViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
        categoryIcon = itemView.findViewById(R.id.categoryIcon);
        cardLayout = itemView.findViewById(R.id.cardLayout);
        budgetIcon = itemView.findViewById(R.id.budgetIcon);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bind(Drawable icon, Drawable coloredBackground, int backgroundColor, String title, String budget, Drawable budgetIconWithColor) {
        text.setText(title);
        categoryIcon.setImageDrawable(icon);
        categoryIcon.setBackground(coloredBackground);
        budgetIcon.setImageDrawable(budgetIconWithColor);
        cardLayout.setBackgroundColor(backgroundColor);
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