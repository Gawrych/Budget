package com.example.budgetmanagement.ui.details;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class DetailsUtils {

    public abstract void setValues();

    public Drawable getAmountIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_down_24, R.color.mat_red);
        } else {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_up_24, R.color.mat_green);
        }
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private Drawable getDrawableWithColor(int drawableResId, int colorResId) {
        Drawable drawable = ResourcesCompat.getDrawable(getFragment().getResources(), drawableResId, null);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getFragment().requireContext(), colorResId), PorterDuff.Mode.SRC_IN));
        } else {
            drawable = ResourcesCompat.getDrawable(getFragment().getResources(), R.drawable.ic_outline_icon_not_found_24, null);
        }
        return drawable;
    }

    public Drawable getCategoryIcon(Category category) {
        IconPack iconPack = ((AppIconPack) getFragment().requireActivity().getApplication()).getIconPack();
        assert iconPack != null;
        return Objects.requireNonNull(iconPack.getIcon(category.getIcon())).getDrawable();
    }

    public String getValueFromModifiedDate(long modifiedDate) {
        if (modifiedDate != 0) {
            return DateProcessor.parseDate(modifiedDate);
        }
        return getFragment().requireActivity().getApplicationContext().getString(R.string.never);
    }

    public abstract Fragment getFragment();
}
