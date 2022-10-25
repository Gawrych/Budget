package com.example.budgetmanagement.ui.utils;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.MainActivity;
import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.CategoryBottomSheetAdapter;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.ui.Category.App;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoryBottomSheetSelector extends Fragment implements CategoryViewHolder.OnNoteListener {

    private BottomSheetDialog bottomSheetDialog;
    private LiveData<List<Category>> categoryLiveData;
    private final CategoryViewModel categoryViewModel;
    private int selectedId = 0;
    private String selectedName = "";
    private int iconId;

    public CategoryBottomSheetSelector(Fragment rootFragment) {
        categoryViewModel = new ViewModelProvider(rootFragment).get(CategoryViewModel.class);

        IconPack iconPack = ((App) rootFragment.requireActivity().getApplication()).getIconPack();

        final CategoryBottomSheetAdapter categoryBottomSheetAdapter =
                new CategoryBottomSheetAdapter(
                        new CategoryBottomSheetAdapter.HistoryBottomSheetEntityDiff(),
                        iconPack,
                        this::onNoteClick);

        categoryLiveData = categoryViewModel.getAllCategory();
        categoryLiveData.observe(rootFragment.getViewLifecycleOwner(),
                categoryBottomSheetAdapter::submitList);

        bottomSheetDialog = new BottomSheetDialog(rootFragment.requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        RecyclerView bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        Objects.requireNonNull(bottomSheetRecyclerView).setAdapter(categoryBottomSheetAdapter);
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void show() {
        bottomSheetDialog.show();
        resetSelectedId();
        resetSelectedName();
    }

    @Override
    public void onNoteClick(int position) {
        List<Category> listOfEntity = categoryViewModel.getCategoryList();
        this.selectedId = listOfEntity.get(position).getCategoryId();
        this.selectedName = listOfEntity.get(position).getName();
        this.iconId = listOfEntity.get(position).getIcon();
        bottomSheetDialog.cancel();
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return this.bottomSheetDialog;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public int getIconId() {
        return iconId;
    }

    public String getCategoryNameById(Integer id) {
        if (id == null || id <= 0) {
            return "";
        }
        List<Category> listOfEntity = categoryViewModel.getCategoryList();
        Optional<Category> searchElement;

        searchElement = listOfEntity
                .stream()
                .filter(e -> e.getCategoryId() == id)
                .findFirst();

        return searchElement.map(Category::getName).orElse("");
    }

    public static String getCategoryName(Integer id, Fragment rootFragment) {
        if (id == null || id <= 0) {
            return "";
        }

        List<Category> listOfEntity = new ViewModelProvider(rootFragment).get(CategoryViewModel.class).getCategoryList();
        Optional<Category> searchElement;

        searchElement = listOfEntity
                .stream()
                .filter(e -> e.getCategoryId() == id)
                .findFirst();

        return searchElement.map(Category::getName).orElse("");
    }

    public void resetSelectedName() {
        this.selectedName = "Różne";
    }

    public void resetSelectedId() {
        this.selectedId = 1;
    }

}
