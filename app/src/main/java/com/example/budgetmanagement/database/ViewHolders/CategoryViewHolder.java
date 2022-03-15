package com.example.budgetmanagement.database.ViewHolders;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView text;
    private ImageView imageView;
    private OnNoteListener onNoteListener;

    public CategoryViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
        imageView = itemView.findViewById(R.id.categoryIcon);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String aIconName, String aTitle, int aBudget) {
        text.setText(aTitle);
        int resourceId = itemView.getContext().getResources().getIdentifier(aIconName, "drawable", itemView.getContext().getPackageName());
        imageView.setImageResource(resourceId);
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}