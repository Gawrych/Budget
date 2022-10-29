package com.example.budgetmanagement.ui.Category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.common.internal.Objects;
import com.google.android.material.button.MaterialButton;
import com.maltaisn.icondialog.pack.IconPack;

public class CategoryFragment extends Fragment implements CategoryViewHolder.OnNoteListener {

    private CategoryFragmentBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoryBottomSheetDetails details;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recyclerView);

        IconPack iconPack = ((App) requireActivity().getApplication()).getIconPack();
        details = new CategoryBottomSheetDetails(requireContext(), requireActivity(), iconPack, this, view);

        final CategoryAdapter adapter = new CategoryAdapter(new CategoryAdapter.CategoryDiff(), iconPack, this);
        recyclerView.setAdapter(adapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), adapter::submitList);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);

        ImageView button = view.findViewById(R.id.addButton);
        button.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_categoryList_to_addNewCategoryElement);
        });
    }

    @Override
    public void onNoteClick(int position) {
        Category category = categoryViewModel.getCategory(position);
        details.setData(category);
        details.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}