package com.example.budgetmanagement.database.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewholders.CategoryViewHolder;
import com.example.budgetmanagement.database.rooms.Category;
import com.maltaisn.icondialog.pack.IconPack;

public class CategoryAdapter extends ListAdapter<Category, CategoryViewHolder> {

    private CategoryViewHolder.OnNoteListener mOnNoteListener;
    private IconPack iconPack;
    private View view;

    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, IconPack iconPack, CategoryViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.iconPack = iconPack;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category current = getItem(position);
        int iconId = current.getIcon();
        Drawable icon = iconPack.getIcon(iconId).getDrawable();
        holder.bind(icon, current.getName(), current.getBudget());
    }

    public CategoryViewHolder create(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recycler_view, parent, false);
        return new CategoryViewHolder(view, mOnNoteListener);
    }

    public static class CategoryDiff extends DiffUtil.ItemCallback<Category> {

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
