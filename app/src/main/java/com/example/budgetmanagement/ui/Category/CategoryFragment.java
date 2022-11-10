package com.example.budgetmanagement.ui.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.CategoryAdapter;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.databinding.CategoryFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.List;

public class CategoryFragment extends Fragment implements CategoryViewHolder.OnNoteListener {

    private static final String TAG = "category_fragment";
    private CategoryFragmentBinding binding;
    private LiveData<List<Category>> categoryList;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CategoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        RecyclerView recyclerView;
        recyclerView = binding.recyclerView;

        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();

        final CategoryAdapter adapter =
                new CategoryAdapter(new CategoryAdapter.CategoryDiff(), iconPack, this);
        recyclerView.setAdapter(adapter);

        CategoryViewModel categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryList = categoryViewModel.getAllCategories();
        categoryList.observe(getViewLifecycleOwner(), adapter::submitList);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);

        binding.addButton.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_categoryList_to_addNewCategoryElement));
    }

    @Override
    public void onNoteClick(int position) {
        Category category = getCategoryFromList(position);
        if (category == null) {
            Toast.makeText(requireContext(), R.string.category_not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        openDetailsFragment(category.getCategoryId());
    }

    @Override
    public void onLongNoteClick(int position) {
        Category category = getCategoryFromList(position);
        if (category == null) {
            Toast.makeText(requireContext(), R.string.category_not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        showBottomSheetMenu(category.getCategoryId());
    }

    private Category getCategoryFromList(int position) {
        List<Category> value = categoryList.getValue();
        if (value == null) {
            return null;
        }

        return value.get(position);
    }

    private void showBottomSheetMenu(int categoryId) {
        CategoryBottomSheetDialog bottomSheet = CategoryBottomSheetDialog.newInstance(categoryId);
        bottomSheet.show(getParentFragmentManager(), TAG);
    }

    private void openDetailsFragment(int categoryId) {
        CategoryElementDetails elementDetails = CategoryElementDetails.newInstance(categoryId);
        Navigation.findNavController(view)
                .navigate(R.id.action_categoryList_to_categoryElementDetails, elementDetails.getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}