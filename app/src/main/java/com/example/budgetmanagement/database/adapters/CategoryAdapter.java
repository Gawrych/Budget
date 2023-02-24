package com.example.budgetmanagement.database.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewholders.CategoryViewHolder;
import com.example.budgetmanagement.ui.category.CategoryFragment;
import com.example.budgetmanagement.ui.utils.CategoryIconHelper;

public class CategoryAdapter extends ListAdapter<Category, CategoryViewHolder> {

    private final CategoryFragment categoryFragment;

    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, CategoryFragment categoryFragment) {
        super(diffCallback);
        this.categoryFragment = categoryFragment;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category current = getItem(position);

        int color = current.getColor();
        int backgroundColor = ColorUtils.setAlphaComponent(color, 50);

        Drawable categoryIcon = CategoryIconHelper.getCategoryIcon(current.getIcon(), this.categoryFragment.requireActivity());
        Drawable categoryIconBackground = CategoryIconHelper.getIconBackground(this.categoryFragment.requireContext(), color, R.drawable.icon_background);

        holder.bind(current.getCategoryId(), categoryIcon, categoryIconBackground, backgroundColor, current.getName(), current.getBudget());
    }

    public CategoryViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_child_view, parent, false);
        return new CategoryViewHolder(view, categoryFragment);
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
