package com.example.budgetmanagement.database.viewholders;

import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.example.budgetmanagement.databinding.CategorySelectorChildBinding;

public class CategoryBottomSheetViewHolder extends RecyclerView.ViewHolder {

    private final OnNoteListener onNoteListener;
    private final CategorySelectorChildBinding binding;

    public CategoryBottomSheetViewHolder(View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        this.binding = CategorySelectorChildBinding.bind(itemView);
        this.onNoteListener = onNoteListener;
    }

    public void bind(int categoryId, String title, Drawable icon, Drawable iconBackground) {
        this.binding.setCategoryId(categoryId);
        this.binding.setTitle(title);
        this.binding.setIcon(icon);
        this.binding.setIconBackground(iconBackground);
        this.binding.setCategoryBottomSheetViewHolder(this);
    }

    public void onClickListener(int categoryId) {
        onNoteListener.onNoteClick(categoryId);
    }

    public interface OnNoteListener {
        void onNoteClick(int categoryId);
    }
}
