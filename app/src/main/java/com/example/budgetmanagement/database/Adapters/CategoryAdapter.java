package com.example.budgetmanagement.database.Adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.Rooms.Category;

public class CategoryAdapter extends ListAdapter<Category, CategoryViewHolder> {

    private CategoryViewHolder.OnNoteListener mOnNoteListener;
    private String layoutName = "";

    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, CategoryViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, CategoryViewHolder.OnNoteListener onNoteListener, String aLayoutName) {
        super(diffCallback);
        layoutName = aLayoutName;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category current = getItem(position);
        holder.bind(current.getIconName(), current.getName(), current.getBudget());
    }

    public CategoryViewHolder create(ViewGroup parent) {
        View view;
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
