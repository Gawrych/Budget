package com.example.budgetmanagement.ui.utils;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.adapters.CategoryBottomSheetAdapter;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewholders.CategoryBottomSheetViewHolder;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.List;
import java.util.Objects;

public class CategoryBottomSheetSelector extends Fragment implements CategoryBottomSheetViewHolder.OnNoteListener {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<Category>> categoryLiveData;
    private final CategoryViewModel categoryViewModel;
    private String selectedName = "";
    private int iconId = 995;

    public CategoryBottomSheetSelector(Fragment rootFragment) {
        categoryViewModel = new ViewModelProvider(rootFragment).get(CategoryViewModel.class);
        Context context = rootFragment.requireContext();
        
        IconPack iconPack = ((AppIconPack) rootFragment.requireActivity().getApplication()).getIconPack();

        final CategoryBottomSheetAdapter categoryBottomSheetAdapter =
                new CategoryBottomSheetAdapter(
                        new CategoryBottomSheetAdapter.CategoryBottomSheetEntityDiff(),
                        context,
                        iconPack,
                        this);

        categoryLiveData = categoryViewModel.getAllCategory();
        categoryLiveData.observe(rootFragment.getViewLifecycleOwner(),
                categoryBottomSheetAdapter::submitList);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.category_selector);

        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.categoriesNames);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(categoryBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void show() {
        bottomSheetDialog.show();
        resetSelectedName();
        resetIconId();
    }

    @Override
    public void onNoteClick(int categoryId) {
        Category selectedCategory = categoryViewModel.getCategoryById(categoryId);
        this.selectedName = selectedCategory.getName();
        this.iconId = selectedCategory.getIcon();
        bottomSheetDialog.cancel();
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return this.bottomSheetDialog;
    }

    public String getSelectedCategoryName() {
        return selectedName;
    }

    public int getIconId() {
        return iconId;
    }

    public void resetSelectedName() {
        this.selectedName = "Różne";
    }

    public void resetIconId() {
        this.iconId = 955;
    }
}
