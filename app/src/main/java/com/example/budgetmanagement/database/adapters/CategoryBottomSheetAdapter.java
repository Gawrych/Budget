package com.example.budgetmanagement.database.adapters;

import android.content.Context;
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
import com.example.budgetmanagement.ui.utils.CategoryIconHelper;
import com.maltaisn.icondialog.pack.IconPack;

public class CategoryBottomSheetAdapter extends ListAdapter<Category, CategoryBottomSheetViewHolder> {

    private final CategoryBottomSheetViewHolder.OnNoteListener mOnNoteListener;
    private final Context context;
    private final IconPack iconPack;

    public CategoryBottomSheetAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, Context context, IconPack iconPack, CategoryBottomSheetViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.context = context;
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
        Category category = getItem(position);
        Drawable categoryIcon = CategoryIconHelper.getCategoryIcon(category.getIcon(), this.iconPack);
        Drawable categoryIconBackground = CategoryIconHelper.getIconBackground(this.context, category.getColor(), R.drawable.icon_background);
        holder.bind(category.getCategoryId(), category.getName(), categoryIcon, categoryIconBackground);
    }

    public CategoryBottomSheetViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_selector_child, parent, false);

        return new CategoryBottomSheetViewHolder(view, this.mOnNoteListener);
    }

    public static class CategoryBottomSheetEntityDiff extends DiffUtil.ItemCallback<Category> {

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
