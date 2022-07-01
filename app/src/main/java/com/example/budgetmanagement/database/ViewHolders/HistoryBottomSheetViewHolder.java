package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class HistoryBottomSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView entityName;
    private final ImageView entityIcon;
    private final OnNoteListener onNoteListener;

    public HistoryBottomSheetViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        entityName = itemView.findViewById(R.id.textView);
        entityIcon = itemView.findViewById(R.id.categoryIcon);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(String iconName, String name) {
        entityName.setText(name);
        entityIcon.setImageResource(getResourceIconIdByName(iconName));
    }

    private int getResourceIconIdByName(String iconName) {
        return itemView.getContext().getResources().getIdentifier(iconName, "drawable", itemView.getContext().getPackageName());
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
