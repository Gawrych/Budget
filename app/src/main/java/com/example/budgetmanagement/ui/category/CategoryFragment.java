package com.example.budgetmanagement.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.adapters.CategoryAdapter;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.databinding.CategoryFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.List;

public class CategoryFragment extends Fragment {

    private static final String CATEGORY_FRAGMENT_TAG = "category_fragment";
    private CategoryFragmentBinding binding;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CategoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setCategoryFragment(this);

        view = view;
        RecyclerView recyclerView = binding.monthsItems;

        final CategoryAdapter adapter =
                new CategoryAdapter(new CategoryAdapter.CategoryDiff(),
                        this);
        recyclerView.setAdapter(adapter);

        CategoryViewModel categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);

        LiveData<List<Category>> categoryList = categoryViewModel.getAllCategories();
        categoryList.observe(getViewLifecycleOwner(), adapter::submitList);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void showActionHandler(int categoryId) {
        ActionCategoryItemHandler bottomSheet = ActionCategoryItemHandler.newInstance(categoryId);
        bottomSheet.show(getParentFragmentManager(), CATEGORY_FRAGMENT_TAG);
    }

    private void openDetailsFragment(int categoryId) {
        CategoryDetails elementDetails = CategoryDetails.newInstance(categoryId);
        Navigation.findNavController(view)
                .navigate(R.id.action_categoryList_to_categoryDetails, elementDetails.getArguments());
    }

    public void onItemClickListener(int categoryId) {
        openDetailsFragment(categoryId);
    }

    public boolean onItemLongClickListener(int categoryId) {
        showActionHandler(categoryId);
        return true;
    }

    public void addButtonClickListener() {
        Navigation.findNavController(view)
                .navigate(R.id.action_categoryList_to_addNewCategoryElement);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}