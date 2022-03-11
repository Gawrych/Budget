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
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.ComingViewHolder;

public class ComingAdapter extends ListAdapter<Coming, ComingViewHolder> {

    private ComingViewHolder.OnNoteListener mOnNoteListener;

    public ComingAdapter(@NonNull DiffUtil.ItemCallback<Coming> diffCallback, ComingViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ComingViewHolder holder, int position) {
        Coming current = getItem(position);
        holder.bind(current.getComingId(), current.getAddDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ComingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recycler_view, parent, false);

        return new ComingViewHolder(view, mOnNoteListener);
    }

    public static class ComingDiff extends DiffUtil.ItemCallback<Coming> {

        @Override
        public boolean areItemsTheSame(@NonNull Coming oldItem, @NonNull Coming newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Coming oldItem, @NonNull Coming newItem) {
            return oldItem.getComingId() == newItem.getComingId();
        }
    }
}
