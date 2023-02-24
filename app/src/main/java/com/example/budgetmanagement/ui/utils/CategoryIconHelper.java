package com.example.budgetmanagement.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

public interface CategoryIconHelper {

    static Drawable getCategoryIcon(int categoryIconRes, @NonNull Activity activity) {
        IconPack iconPack = ((AppIconPack) activity.getApplication()).getIconPack();
        if (iconPack == null) return null;
        Icon icon = iconPack.getIcon(categoryIconRes);
        if (icon == null) return null;
        return icon.getDrawable();
    }

    static Drawable getCategoryIcon(int categoryIconRes, @NonNull IconPack iconPack) {
        Icon icon = iconPack.getIcon(categoryIconRes);
        if (icon == null) return null;
        return icon.getDrawable();
    }

    static Drawable getIconBackground(Context context, int colorRes, int backgroundShape) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), backgroundShape, null);
        if (drawable == null) return null;
        Drawable drawableWrapped = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawableWrapped, colorRes);
        return drawableWrapped;
    }
}
