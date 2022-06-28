package com.example.budgetmanagement.database.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.ComingViewHolder;

public class ComingAdapter extends ListAdapter<ComingAndTransaction, ComingViewHolder> {

    private ComingViewHolder.OnNoteListener mOnNoteListener;

    public ComingAdapter(@NonNull DiffUtil.ItemCallback<ComingAndTransaction> diffCallback, ComingViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ComingViewHolder holder, int position) {
        ComingAndTransaction current = getItem(position);
        holder.bind(current.transaction.getTitle(), current.transaction.getAmount(), current.coming.getRepeatDate());
    }

    public ComingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coming_recycler_view, parent, false);

        return new ComingViewHolder(view, mOnNoteListener);
    }

    public static class ComingDiff extends DiffUtil.ItemCallback<ComingAndTransaction> {

        @Override
        public boolean areItemsTheSame(@NonNull ComingAndTransaction oldItem, @NonNull ComingAndTransaction newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ComingAndTransaction oldItem, @NonNull ComingAndTransaction newItem) {
            return oldItem.coming.getComingId() == newItem.coming.getComingId();
        }
    }
}
