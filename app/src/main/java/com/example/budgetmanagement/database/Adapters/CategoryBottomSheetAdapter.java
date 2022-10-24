package com.example.budgetmanagement.database.Adapters;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.CategoryBottomSheetViewHolder;

public class CategoryBottomSheetAdapter extends ListAdapter<Category, CategoryBottomSheetViewHolder> {

    private final CategoryBottomSheetViewHolder.OnNoteListener mOnNoteListener;
    View view;

    public CategoryBottomSheetAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, CategoryBottomSheetViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CategoryBottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @Override
    public void onBindViewHolder(CategoryBottomSheetViewHolder holder, int position) {
        Category current = getItem(position);
        byte[] b = current.getIcon();
        holder.bind(new BitmapDrawable(view.getContext().getResources(), BitmapFactory.decodeByteArray(b, 0, b.length)), current.getName());
    }

    public CategoryBottomSheetViewHolder create(ViewGroup parent) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_title_bottom_sheet_details_child, parent, false);

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
