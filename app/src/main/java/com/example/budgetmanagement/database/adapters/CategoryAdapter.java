package com.example.budgetmanagement.database.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.viewholders.CategoryViewHolder;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.ui.category.CategoryFragment;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;

public class CategoryAdapter extends ListAdapter<Category, CategoryViewHolder> {

    private final CategoryFragment categoryFragment;
    private final IconPack iconPack;
    private final Context context;

    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<Category> diffCallback, IconPack iconPack, CategoryFragment categoryFragment, Context context) {
        super(diffCallback);
        this.iconPack = iconPack;
        this.context = context;
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
        int iconId = current.getIcon();
        Icon iconPackIcon = iconPack.getIcon(iconId);
        Drawable icon = (iconPackIcon == null) ? getNotFoundIcon() : iconPackIcon.getDrawable();

        int color = current.getColor();
        Drawable coloredBackground =
                getDrawableWithColor(R.drawable.icon_background, color);

        int backgroundColor = ColorUtils.setAlphaComponent(color, 51);

        holder.bind(current.getCategoryId(), icon, coloredBackground, backgroundColor, current.getName(), getAmountIconDependOfValue(current.getBudget()));
    }

    @Nullable
    private Drawable getNotFoundIcon() {
        return AppCompatResources.getDrawable(this.context, R.drawable.ic_outline_icon_not_found_24);
    }

    public Drawable getAmountIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_down_24, getColorFromRes(R.color.mat_red));
        } else {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_up_24, getColorFromRes(R.color.mat_green));
        }
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private Drawable getDrawableWithColor(int drawableId, int colorId) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableId, null);
        if (drawable == null) return null;
        Drawable drawableWrapped = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawableWrapped, colorId);
        return drawableWrapped;
    }

    private int getColorFromRes(int colorId) {
        return ContextCompat.getColor(context, colorId);
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
