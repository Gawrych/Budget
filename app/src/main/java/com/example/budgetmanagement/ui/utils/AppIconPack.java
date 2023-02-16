package com.example.budgetmanagement.ui.utils;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.budgetmanagement.R;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;
import com.maltaisn.icondialog.pack.IconPackLoader;
import com.maltaisn.iconpack.defaultpack.IconPackDefault;

import javax.annotation.Nullable;

public class AppIconPack extends Application {

    @Nullable
    private IconPack iconPack;

    @Override
    public void onCreate() {
        super.onCreate();
        // Load the icon pack on application start.
        loadIconPack();
    }

    @Nullable
    public IconPack getIconPack() {
        return iconPack != null ? iconPack : loadIconPack();
    }

    private IconPack loadIconPack() {
        // Create an icon pack loader with application context.
        IconPackLoader loader = new IconPackLoader(this);

        // Create an icon pack and load all drawables.
        iconPack = IconPackDefault.createDefaultIconPack(loader);
        iconPack.loadDrawables(loader.getDrawableLoader());
        return iconPack;
    }

    public Drawable getDrawableIconFromPack(int iconId) {
        Icon icon = this.iconPack == null ? null : this.iconPack.getIcon(iconId);
        return icon == null ? AppCompatResources.getDrawable(
                getApplicationContext(), R.drawable.ic_outline_icon_not_found_24) : icon.getDrawable();
    }
}
