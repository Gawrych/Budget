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
import com.example.budgetmanagement.database.ViewHolders.CategoryBottomSheetViewHolder;

public class CategoryBottomSheetAdapter extends ListAdapter<Category, CategoryBottomSheetViewHolder> {

    private CategoryBottomSheetViewHolder.OnNoteListener mOnNoteListener;

    public CategoryBottomSheetAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, CategoryBottomSheetViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CategoryBottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(CategoryBottomSheetViewHolder holder, int position) {
        Category current = getItem(position);
        holder.bind(current.getIconName(), current.getName());
    }

    public CategoryBottomSheetViewHolder create(ViewGroup parent) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_bottom_sheet_category_filter_recycler_view, parent, false);

        return new CategoryBottomSheetViewHolder(view, mOnNoteListener);
    }

    public static class HistoryBottomSheetEntityDiff extends DiffUtil.ItemCallback<Category> {

        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getCategoryId() == newItem.getCategoryId();
        }
    }
}
