package com.example.budgetmanagement.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatchers {

    public static Matcher<View> withBackground(final int resourceId) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                return sameBitmap(view.getContext(), view.getBackground(), resourceId);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has background resource " + resourceId);
            }
        };
    }

    public static Matcher<View> withCompoundDrawable(final int resourceId) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has compound drawable resource " + resourceId);
            }

            @Override
            public boolean matchesSafely(TextView textView) {
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (sameBitmap(textView.getContext(), drawable, resourceId)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Matcher<View> withImageDrawable(final int resourceId) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has image drawable resource " + resourceId);
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return sameBitmap(imageView.getContext(), imageView.getDrawable(), resourceId);
            }
        };
    }

    public static Matcher<View> withBackgroundColor(final int resourceId) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has background color " + resourceId);
            }

            @Override
            public boolean matchesSafely(View view) {
                return sameColor(view.getContext(), view.getBackground(), resourceId);
            }
        };
    }

    private static boolean sameBitmap(Context context, Drawable drawable, int resourceId) {
        Drawable otherDrawable = context.getResources().getDrawable(resourceId);
        if (drawable == null || otherDrawable == null) {
            return false;
        }

        drawable = drawable.getCurrent();
        otherDrawable = otherDrawable.getCurrent();

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap otherBitmap = ((BitmapDrawable) otherDrawable).getBitmap();
            return bitmap.sameAs(otherBitmap);
        } else if (drawable instanceof VectorDrawable) {
            Bitmap bitmap = getBitmapFromVector(drawable);
            Bitmap otherBitmap = getBitmapFromVector(otherDrawable);
            if (bitmap != null && otherBitmap != null) {
                return bitmap.sameAs(otherBitmap);
            }
        }
        return false;
    }

    private static Bitmap getBitmapFromVector(Drawable drawable) {
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean sameColor(Context context, Drawable drawable, int resourceId) {
        int otherColorId = context.getResources().getColor(resourceId);
        if (drawable instanceof ColorDrawable) {
            int colorId = ((ColorDrawable) drawable).getColor();
            return otherColorId == colorId;
        }
        return false;
    }
}
