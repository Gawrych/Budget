package com.example.budgetmanagement.ui.Category;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.CategoryAdapter;
import com.example.budgetmanagement.database.Rooms.CategoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.databinding.CategoryFragmentBinding;

import java.util.List;
import java.util.Objects;

public class CategoryFragment extends Fragment implements CategoryViewHolder.OnNoteListener {

    private CategoryViewModel categoryViewModel;
    private CategoryFragmentBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        RecyclerView recyclerView;

        root =  inflater.inflate(R.layout.category_fragment, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        final CategoryAdapter adapter = new CategoryAdapter(new CategoryAdapter.CategoryDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), adapter::submitList);

//        List<CategoryAndTransaction> categoryAndTransactionList = categoryViewModel.getCategoryAndTransaction();
//        Toast.makeText(getContext(), String.valueOf(Objects.requireNonNull(categoryAndTransactionList).get(0).transactionList.get(0).getTitle()), Toast.LENGTH_SHORT).show();

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    @Override
    public void onNoteClick(int position) {
//        ((MainActivity) requireActivity()).turnOnProgressBar();
//        Intent intent = new Intent(getActivity(), AddNewCategoryElement.class);
//        startActivityForResult.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}