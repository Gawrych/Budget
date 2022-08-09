package com.example.budgetmanagement.database.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.ViewHolders.ComingChildViewHolder;
import com.example.budgetmanagement.database.ViewHolders.ComingViewHolder;
import com.example.budgetmanagement.ui.Coming.Section;

public class ComingAdapter extends ListAdapter<Section, ComingViewHolder> {

    private ComingChildViewHolder.OnNoteListener mOnNoteListener;
    private Context context;

    public ComingAdapter(@NonNull DiffUtil.ItemCallback<Section> diffCallback, ComingChildViewHolder.OnNoteListener onNoteListener, Context context) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ComingViewHolder holder, int position) {
        Section current = getItem(position);
        holder.bind(mOnNoteListener, context, current.getLabelId(), current.getComingAndTransactionList());
    }

    public ComingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coming_recycler_view, parent, false);

        return new ComingViewHolder(view);
    }

    public static class ComingDiff extends DiffUtil.ItemCallback<Section> {

        @Override
        public boolean areItemsTheSame(@NonNull Section oldItem, @NonNull Section newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Section oldItem, @NonNull Section newItem) {
            return oldItem.getLabelId() == newItem.getLabelId();
        }
    }
}
