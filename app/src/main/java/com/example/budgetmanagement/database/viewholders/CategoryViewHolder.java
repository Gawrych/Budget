package com.example.budgetmanagement.database.viewholders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.compose.ui.platform.InfiniteAnimationPolicy;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.databinding.CategoryChildViewBinding;
import com.example.budgetmanagement.ui.category.CategoryChildValues;
import com.example.budgetmanagement.ui.category.CategoryFragment;

import java.util.zip.Inflater;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final CategoryChildViewBinding binding;
    private final CategoryFragment categoryFragment;

    public CategoryViewHolder(View itemView, CategoryFragment categoryFragment) {
        super(itemView);
        this.binding = CategoryChildViewBinding.bind(itemView);
        this.categoryFragment = categoryFragment;
    }

    public void bind(int categoryId, Drawable categoryIcon, Drawable categoryIconBackground, int cardLayoutBackgroundColor, String title, Drawable budgetIconWithColor) {
        this.binding.setCategoryId(categoryId);
        this.binding.setCategoryFragment(this.categoryFragment);
        CategoryChildValues categoryValues = new CategoryChildValues(
                title,
                categoryIcon,
                categoryIconBackground,
                budgetIconWithColor,
                cardLayoutBackgroundColor);
        this.binding.setCategoryValues(categoryValues);
    }
}