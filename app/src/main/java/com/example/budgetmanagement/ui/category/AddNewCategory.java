package com.example.budgetmanagement.ui.category;

import static com.example.budgetmanagement.ui.category.BottomSheetColorPicker.BOTTOM_SHEET_COLOR_TAG;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.CategoryQuery;
import com.example.budgetmanagement.databinding.AddNewEditCategoryFragmentBinding;
import com.example.budgetmanagement.ui.utils.InputTextCollector;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.ArrayList;
import java.util.List;

public class AddNewCategory extends Fragment implements IconDialog.Callback {

    private static final String ICON_DIALOG_TAG = "iconDialog";
    private AddNewEditCategoryFragmentBinding binding;
    private int iconId;
    private int colorPosition;
    private int colorResources;
    private String title;
    private String amount;
    private IconDialog iconDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = AddNewEditCategoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.binding.setAddNewCategory(this);
        this.binding.setButtonTitle(getString(R.string.add));

        initializeIconPicker(binding.iconPicker);
        initializeColorPicker(binding.colorPicker, binding.colorPickerLayout);
    }

    public void acceptButtonClick() {
        InputTextCollector collector = new InputTextCollector(requireContext());
        collectData(collector);
        if (collector.areCorrectlyCollected()) {
            submitToDatabase();
        }
        collector.resetCollectedStatus();
    }

    private void initializeIconPicker(AutoCompleteTextView iconPicker) {
        IconDialogSettings.Builder settings = new IconDialogSettings.Builder();
        settings.setSearchVisibility(IconDialog.SearchVisibility.ALWAYS);

        IconDialog iconDialogFoundByTag = (IconDialog) requireActivity().getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        this.iconDialog = (iconDialogFoundByTag == null) ? IconDialog.newInstance(settings.build()) : iconDialogFoundByTag;

        iconPicker.setOnClickListener(v -> iconDialog.show(getChildFragmentManager(), ICON_DIALOG_TAG));
    }

    public void initializeColorPicker(AutoCompleteTextView colorPicker, TextInputLayout colorPickerLayout) {
        colorPicker.setOnClickListener(v -> {
            BottomSheetColorPicker bottomSheetColorPicker = BottomSheetColorPicker.newInstance(colorPosition);
            bottomSheetColorPicker.setOnDateSelectedListener((colorPos, colorRes) -> {
                setColorValues(colorPos, colorRes);
                setColorToPickerLayout(colorPicker, colorPickerLayout, this.colorResources);
            });
            bottomSheetColorPicker.show(getParentFragmentManager(), BOTTOM_SHEET_COLOR_TAG);
        });
    }

    private void setColorValues(int colorPos, int colorRes) {
        if (colorRes == 0 || colorPos == 0) return;
        this.colorPosition = colorPos;
        this.colorResources = colorRes;
    }

    @SuppressLint("PrivateResource")
    private void setColorToPickerLayout(AutoCompleteTextView colorPicker, TextInputLayout colorPickerLayout, int colorRes) {
        colorPicker.setText(getString(R.string.selected));
        setEndDrawableIconAsOvalWithColor(colorPickerLayout, colorRes);
    }

    private void setEndDrawableIconAsOvalWithColor(TextInputLayout field, int colorRes) {
        Drawable drawableOval = ResourcesCompat
                .getDrawable(getResources(), R.drawable.end_icon_oval_color, null);
        if (drawableOval != null) field.setEndIconDrawable(drawableOval);
        field.setEndIconTintList(ColorStateList.valueOf(colorRes));
    }

    public void collectData(InputTextCollector collector) {
        this.title = collector.collect(binding.titleTextView);
        this.amount = collector.collectBasedOnProfitSwitch(binding.amountLayout, binding.profitSwitch);
        collector.collect(binding.iconPickerLayout);
        collector.collect(binding.colorPickerLayout);

        if (this.iconId == 0) {
            collector.setErrorMessage(binding.iconPickerLayout);
            collector.markCollectedDataAsFailure();
        }

        if (this.colorResources == 0) {
            collector.setErrorMessage(binding.colorPickerLayout);
            collector.markCollectedDataAsFailure();
        }
    }

    protected void submitToDatabase() {
        CategoryQuery categoryQuery = new CategoryQuery(this);
        categoryQuery.createCategory(
                0,
                System.currentTimeMillis(),
                this.title,
                this.amount,
                this.iconId,
                this.colorResources);
        categoryQuery.submit();
        backToPreviousFragment();
    }

    protected void backToPreviousFragment() {
        requireActivity().onBackPressed();
    }

    private void setIconToPickerLayout(@NonNull Icon selectedIcon) {
        this.iconId = selectedIcon.getId();
        Drawable pickedIcon = selectedIcon.getDrawable();

        binding.setSelectedIcon(pickedIcon);
        binding.setSelectedIconName(selectedIcon.getTags().get(0));
    }

    protected AddNewEditCategoryFragmentBinding getBinding() {
        return binding;
    }

    protected void setIcon(int newIconId) {
        IconPack iconPack = getIconDialogIconPack();
        if (iconPack == null) return;
        Icon icon = iconPack.getIcon(newIconId);
        if (icon == null) return;
        setIconToPickerLayout(icon);
        setIconToIconDialog(newIconId);
    }

    private void setIconToIconDialog(int newIconId) {
        this.iconDialog.setSelectedIconIds(new ArrayList<>(List.of(newIconId)));
    }

    protected void setColor(int newColorRes) {
        int newColorPos = BottomSheetColorPicker.getColorPositionByResource(requireContext(), newColorRes);
        setColorValues(newColorPos, newColorRes);
        setColorToPickerLayout(binding.colorPicker, binding.colorPickerLayout, newColorRes);
    }

    @Override
    public IconPack getIconDialogIconPack() {
        return ((AppIconPack) requireActivity().getApplication()).getIconPack();
    }

    @Override
    public void onIconDialogCancelled() {}

    @Override
    public void onIconDialogIconsSelected(@NonNull IconDialog iconDialog, @NonNull List<Icon> list) {
        setIconToPickerLayout(list.get(0));
    }

    public int getIconId() {
        return iconId;
    }

    public int getColorResources() {
        return colorResources;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }
}