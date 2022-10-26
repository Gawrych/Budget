package com.example.budgetmanagement.ui.Category;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.ui.Coming.NewComingDataCollector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.List;

public class AddNewCategoryElement extends Fragment implements IconDialog.Callback, NewCategoryFields {

    private static final String ICON_DIALOG_TAG = "icon-dialog";

    private IconDialog iconDialog;
    private Drawable pickedIcon;
    private TextInputEditText title;
    private TextInputLayout titleLayout;
    private TextInputEditText amount;
    private TextInputLayout amountLayout;
    private AutoCompleteTextView iconPicker;
    private TextInputLayout iconPickerLayout;
    private NewCategoryDataCollector newCategoryDataCollector;
    private MaterialButton acceptButton;
    private SwitchMaterial profitSwitch;
    private int iconId = 0; // TODO Change this to global static variable

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_category_element, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.title);
        titleLayout = view.findViewById(R.id.titleLayout);
        amount = view.findViewById(R.id.amount);
        amountLayout = view.findViewById(R.id.amountLayout);
        iconPicker = view.findViewById(R.id.iconPicker);
        iconPickerLayout = view.findViewById(R.id.iconPickerLayout);
        acceptButton = view.findViewById(R.id.acceptButton);
        profitSwitch = view.findViewById(R.id.profitSwitch);

        clearErrorWhenTextChanged(title, titleLayout);
        clearErrorWhenTextChanged(amount, amountLayout);

        newCategoryDataCollector = new NewCategoryDataCollector(this);

        IconDialogSettings.Builder settings = new IconDialogSettings.Builder();
        settings.setSearchVisibility(IconDialog.SearchVisibility.ALWAYS);

        IconDialog dialog = (IconDialog) requireActivity().getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        iconDialog = dialog != null ? dialog : IconDialog.newInstance(settings.build());

        iconPicker.setOnClickListener(v -> {
            iconDialog.show(getChildFragmentManager(), ICON_DIALOG_TAG);
            iconPickerLayout.setError(null);
        });

        acceptButton.setOnClickListener(v -> {
            boolean successfullyCollectedData = newCategoryDataCollector.collectData();
            if (successfullyCollectedData) {
                submitToDatabase();
            }
        });
    }

    public void clearErrorWhenTextChanged(EditText fieldToListenTextChange, TextInputLayout fieldToBeCleared) {
        fieldToListenTextChange.addTextChangedListener(getTextWatcher(fieldToBeCleared));
    }

    private TextWatcher getTextWatcher(TextInputLayout fieldToBeCleared) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                fieldToBeCleared.setError(null);
            }
        };
    }

    private void submitToDatabase() {
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.insert(newCategoryDataCollector.getCategory());
        close();
    }

    private void close() {
        requireActivity().onBackPressed();
    }

    @Override
    public IconPack getIconDialogIconPack() {
        return ((App) requireActivity().getApplication()).getIconPack();
    }

    @Override
    public void onIconDialogCancelled() {}

    @Override
    public void onIconDialogIconsSelected(@NonNull IconDialog iconDialog, @NonNull List<Icon> list) {
        Icon selectedIcon = list.get(0);
        iconId = selectedIcon.getId();
        pickedIcon = selectedIcon.getDrawable();
        iconPickerLayout.setEndIconDrawable(pickedIcon);
        iconPickerLayout.setHint(selectedIcon.getTags().get(0));
    }

    @Override
    public int getIconId() {
        return this.iconId;
    }

    @Override
    public TextInputLayout getIconPickerLayout() {
        return iconPickerLayout;
    }

    @Override
    public TextInputEditText getTitleField() {
        return this.title;
    }

    @Override
    public TextInputLayout getTitleLayoutField() {
        return this.titleLayout;
    }

    @Override
    public TextInputEditText getAmountField() {
        return this.amount;
    }

    @Override
    public TextInputLayout getAmountLayoutField() {
        return this.amountLayout;
    }

    @Override
    public SwitchMaterial getProfitSwitch() {
        return this.profitSwitch;
    }
}