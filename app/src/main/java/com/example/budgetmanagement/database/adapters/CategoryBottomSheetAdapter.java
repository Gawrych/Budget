package com.example.budgetmanagement.database.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewholders.CategoryBottomSheetViewHolder;
import com.maltaisn.icondialog.pack.IconPack;

public class CategoryBottomSheetAdapter extends ListAdapter<Category, CategoryBottomSheetViewHolder> {

    private final CategoryBottomSheetViewHolder.OnNoteListener mOnNoteListener;
    private final IconPack iconPack;

    public CategoryBottomSheetAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, IconPack iconPack, CategoryBottomSheetViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.iconPack = iconPack;
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
        int iconId = current.getIcon();
        Drawable icon = iconPack.getIcon(iconId).getDrawable();
        holder.bind(icon, current.getName());
    }

    public CategoryBottomSheetViewHolder create(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_bottom_sheet_child, parent, false);

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
