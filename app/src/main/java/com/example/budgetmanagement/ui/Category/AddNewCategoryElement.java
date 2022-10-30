package com.example.budgetmanagement.ui.Category;

import static com.example.budgetmanagement.ui.Category.CategoryBottomSheetDetails.EDIT_CATEGORY_KEY;
import static com.example.budgetmanagement.ui.Category.CategoryBottomSheetDetails.SEND_CATEGORY_ID_KEY;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
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

    private CategoryViewModel categoryViewModel;
    private int categoryId;
    private Category category;
    private boolean isEdit;
    private IconPack iconPack;

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
        profitSwitch = view.findViewById(R.id.profitSwitch);

        iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();

        this.category = getCategoryByIdFromBundle();
        if (category != null && isEdit) {
            fillFields(category);
            int categoryWithBanOnChangingTitle = 1;
            if (category.getCategoryId() == categoryWithBanOnChangingTitle) {
                title.setFocusable(false);
            }
        }

        boolean shouldEditButWithoutData = category == null && isEdit;
        if (shouldEditButWithoutData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage(R.string.error_element_with_this_id_was_not_found)
                    .setPositiveButton("Ok", (dialog, id) -> {}).show();
            requireActivity().onBackPressed();
            return;
        }

        acceptButton = view.findViewById(R.id.acceptButton);

        clearErrorWhenTextChanged(title, titleLayout);
        clearErrorWhenTextChanged(amount, amountLayout);

        newCategoryDataCollector = new NewCategoryDataCollector(this);

        IconDialogSettings.Builder settings = new IconDialogSettings.Builder();
        settings.setSearchVisibility(IconDialog.SearchVisibility.ALWAYS);

        IconDialog iconDialog = (IconDialog) requireActivity().getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        this.iconDialog = iconDialog != null ? iconDialog : IconDialog.newInstance(settings.build());

        iconPicker.setOnClickListener(v -> {
            this.iconDialog.show(getChildFragmentManager(), ICON_DIALOG_TAG);
            iconPickerLayout.setError(null);
        });

        acceptButton.setOnClickListener(v -> {
            boolean successfullyCollectedData = newCategoryDataCollector.collectData();
            if (successfullyCollectedData) {
                submitToDatabase();
            }
        });
    }

    private void fillFields(Category category) {
        this.title.setText(category.getName());

        BigDecimal budget = new BigDecimal(category.getBudget());
        String number = budget.abs().stripTrailingZeros().toPlainString();
        this.amount.setText(number);

        Icon icon = iconPack.getIcon(category.getIcon());
        this.iconPickerLayout.setEndIconDrawable(icon.getDrawable());
        this.iconPicker.setText(icon.getTags().get(0));
        setIconId(icon.getId());
        profitSwitch.setChecked(!isNegative(budget));
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private Category getCategoryByIdFromBundle() {
        this.categoryId = getArguments() != null ? getArguments().getInt(SEND_CATEGORY_ID_KEY) : 0;
        this.isEdit = getArguments() != null && getArguments().getBoolean(EDIT_CATEGORY_KEY);
        this.categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        return categoryViewModel.getCategoryById(categoryId);
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
        if (isEdit) {
            Category newCategoryData = newCategoryDataCollector.getCategoryWithId(category.getCategoryId());
            categoryViewModel.update(newCategoryData);
        } else {
            Category newCategory = newCategoryDataCollector.getCategory();
            categoryViewModel.insert(newCategory);
        }
        close();
    }

    private void close() {
        requireActivity().onBackPressed();
    }

    @Override
    public IconPack getIconDialogIconPack() {
        return ((AppIconPack) requireActivity().getApplication()).getIconPack();
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

    private void setIconId(int id) {
        this.iconId = id;
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