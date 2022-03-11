package com.example.budgetmanagement.database.ViewHolders;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class ComingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView text;
    private TextView idTextView;
    private ComingViewHolder.OnNoteListener onNoteListener;

    public ComingViewHolder(View itemView, ComingViewHolder.OnNoteListener onNoteListener) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
        idTextView = itemView.findViewById(R.id.idTextView);
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(int aComingId, long aAddDate) {
        idTextView.setText(String.valueOf(aComingId));
        text.setText(String.valueOf(aAddDate));
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
