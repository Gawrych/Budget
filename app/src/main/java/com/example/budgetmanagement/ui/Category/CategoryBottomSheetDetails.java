package com.example.budgetmanagement.ui.Category;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;

public class CategoryBottomSheetDetails extends Fragment {

    public static final String EDIT_CATEGORY_KEY = "isEditCategory";
    public static final String SEND_CATEGORY_ID_KEY = "categoryId";
    private final Context context;
    private final CategoryViewModel categoryViewModel;
    private final View root;
    private final BottomSheetDialog bottomSheetDialog;
    private final TextView categoryNameField;
    private final ImageView profitIcon;
    private final TextView budgetField;
    private final ImageView iconField;
    private final TextView addDateField;
    private Activity activity;
    private final IconPack iconPack;
    private Category category;

    public CategoryBottomSheetDetails(Context context, Activity activity, IconPack iconPack, ViewModelStoreOwner owner, View root) {
        this.context = context;
        this.activity = activity;
        this.iconPack = iconPack;
        this.root = root;

        this.categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);

        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.category_bottom_sheet_details);

        this.categoryNameField = bottomSheetDialog.findViewById(R.id.categoryName);

        this.profitIcon = bottomSheetDialog.findViewById(R.id.profitIcon);
        this.budgetField = bottomSheetDialog.findViewById(R.id.budget);

        this.iconField = bottomSheetDialog.findViewById(R.id.icon);

        this.addDateField = bottomSheetDialog.findViewById(R.id.addDate);

        LinearLayout deleteButton = bottomSheetDialog.findViewById(R.id.deleteLayout);
        LinearLayout editButton = bottomSheetDialog.findViewById(R.id.editLayout);

        assert deleteButton != null;
        deleteButton.setOnClickListener(v -> deleteItem());

        assert editButton != null;
        editButton.setOnClickListener(v -> editSelectedElement());
    }

    public void setData(Category category) {
        this.category = category;

        categoryNameField.setText(category.getName());
        budgetField.setText(category.getBudget());
        addDateField.setText(DateProcessor.parseDate(category.getAddDate()));

        setProfitIconDependOfValue(category.getBudget());
        setCategoryIcon(category);
    }

    private void setProfitIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            profitIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            profitIcon.setColorFilter(context.getColor(R.color.mat_red));
        } else {
            profitIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            profitIcon.setColorFilter(context.getColor(R.color.main_green));
        }
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private void setCategoryIcon(Category category) {
        int iconId = category.getIcon();
        Drawable icon = iconPack.getIcon(iconId).getDrawable();
        iconField.setImageDrawable(icon);
    }

    private void deleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    removeFromDatabase();
                    bottomSheetDialog.cancel();
                    Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> {}).show();
    }

    private void removeFromDatabase() {
        categoryViewModel.delete(this.category);
    }

    private void editSelectedElement() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EDIT_CATEGORY_KEY, true);
        bundle.putInt(SEND_CATEGORY_ID_KEY, this.category.getCategoryId());

        Navigation.findNavController(root)
                .navigate(R.id.action_categoryList_to_addNewCategoryElement, bundle);
        bottomSheetDialog.cancel();
    }

    public void show() {
        bottomSheetDialog.show();
    }
}