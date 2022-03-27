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
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewHolders.HistoryBottomSheetViewHolder;
import com.example.budgetmanagement.ui.History.HistoryBottomSheetEntity;

public class HistoryBottomSheetAdapter extends ListAdapter<HistoryBottomSheetEntity, HistoryBottomSheetViewHolder> {

    private HistoryBottomSheetViewHolder.OnNoteListener mOnNoteListener;

    public HistoryBottomSheetAdapter(@NonNull DiffUtil.ItemCallback<HistoryBottomSheetEntity> diffCallback, HistoryBottomSheetViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public HistoryBottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(HistoryBottomSheetViewHolder holder, int position) {
        HistoryBottomSheetEntity current = getItem(position);
        holder.bind(current.getIconName(), current.getName());
    }

    public HistoryBottomSheetViewHolder create(ViewGroup parent) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_bottom_sheet_recycler_view, parent, false);

        return new HistoryBottomSheetViewHolder(view, mOnNoteListener);
    }

    public static class HistoryBottomSheetEntityDiff extends DiffUtil.ItemCallback<HistoryBottomSheetEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull HistoryBottomSheetEntity oldItem, @NonNull HistoryBottomSheetEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull HistoryBottomSheetEntity oldItem, @NonNull HistoryBottomSheetEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }
}
