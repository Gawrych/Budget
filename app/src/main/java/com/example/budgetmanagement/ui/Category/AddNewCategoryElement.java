package com.example.budgetmanagement.ui.Category;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.example.budgetmanagement.R;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddNewCategoryElement extends Fragment implements IconDialog.Callback {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private TextInputLayout iconPicker;
    private IconDialog iconDialog;
    private Drawable pickedIcon;

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

        IconDialogSettings.Builder settings = new IconDialogSettings.Builder();
        settings.setSearchVisibility(IconDialog.SearchVisibility.ALWAYS);

        IconDialog dialog = (IconDialog) requireActivity().getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        iconDialog = dialog != null ? dialog : IconDialog.newInstance(settings.build());

        iconPicker = view.findViewById(R.id.categorySelectorLayout);

        AutoCompleteTextView btn = view.findViewById(R.id.categorySelector);
        btn.setOnClickListener(v -> {
            iconDialog.show(getChildFragmentManager(), ICON_DIALOG_TAG);
        });
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
        pickedIcon = selectedIcon.getDrawable();
        iconPicker.setEndIconDrawable(pickedIcon);
        iconPicker.setHint(selectedIcon.getTags().get(0));

//        Drawable d; // the drawable (Captain Obvious, to the rescue!!!)
//        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] bitmapdata = stream.toByteArray();
    }
}