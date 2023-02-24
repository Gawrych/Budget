package com.example.budgetmanagement.database.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.databinding.BindingAdapter;

public class DrawableBindingAdapter {

    @SuppressLint("NewApi")
    @BindingAdapter("app:tint")
    public static void setTintCompat(ImageView imageView, @ColorInt int color) {
        imageView.setImageTintList(ColorStateList.valueOf(color));
    }

    @SuppressLint("NewApi")
    @BindingAdapter("app:drawableStartCompat")
    public static void setDrawableStartCompat(TextView textView, Drawable drawable) {
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    @SuppressLint({"NewApi", "UseCompatTextViewDrawableApis"})
    @BindingAdapter("app:drawableTint")
    public static void setDrawableStartCompat(TextView textView, @ColorInt int color) {
        textView.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }
}