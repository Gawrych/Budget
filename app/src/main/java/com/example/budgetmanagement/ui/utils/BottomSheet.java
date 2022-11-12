package com.example.budgetmanagement.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;

public abstract class BottomSheet extends Fragment {

    public void setProfitIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            Drawable drawable = getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_down_24, R.color.mat_red);
            if (drawable != null) {
                getAmountField().setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        } else {
            Drawable drawable = getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_up_24, R.color.mat_green);
            if (drawable != null) {
                getAmountField().setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        }
    }

    private Drawable getDrawableWithColor(int drawableResId, int colorResId) {
        Drawable drawable = ResourcesCompat.getDrawable(getContextFromGlobalVar().getResources(), drawableResId, null);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContextFromGlobalVar(), colorResId), PorterDuff.Mode.SRC_IN));
        }
        return drawable;
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    public void setCategoryIcon(Category category) {
        int iconId = category.getIcon();
        Drawable icon = getIconPack().getIcon(iconId).getDrawable();
        getIconField().setImageDrawable(icon);
    }

    public void deleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivityFromGlobalVar());
        builder.setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    removeFromDatabase();
                    getBottomSheetDialog().cancel();
                    Toast.makeText(getContextFromGlobalVar(), "Element został usunięty", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> {}).show();
    }

    public void show() {
        getBottomSheetDialog().show();
    }

    public abstract void removeFromDatabase();

    public abstract IconPack getIconPack();

    public abstract ImageView getIconField();

    public abstract BottomSheetDialog getBottomSheetDialog();

    public abstract Context getContextFromGlobalVar();

    public abstract Activity getActivityFromGlobalVar();

    public abstract TextView getAmountField();

}
