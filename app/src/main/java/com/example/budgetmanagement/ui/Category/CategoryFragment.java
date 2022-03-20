package com.example.budgetmanagement.ui.Category;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.CategoryAdapter;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.ViewHolders.CategoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.databinding.CategoryFragmentBinding;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CategoryFragment extends Fragment implements CategoryViewHolder.OnNoteListener {

    private CategoryViewModel categoryViewModel;
    private CategoryFragmentBinding binding;

    ActivityResultLauncher<Intent> startActivityForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                int planned_budget = Integer.parseInt(data.getStringExtra("plannedBudget"));
                                String category_name = data.getStringExtra("name");
                                Category category = new Category(0, category_name, "ic_baseline_shopping_basket_24", planned_budget, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                                categoryViewModel.insert(category);
                            } else {
                                Log.println(Log.ERROR, "NULL", "Null as request from 'AddNewTransactionElement' class");
                            }
                        }
                    });


    @RequiresApi(api = Build.VERSION_CODES.O)
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

//        List<ComingWithTransactionAndCategory> comingWithTransactionAndCategory = categoryViewModel.getComingWithTransactionAndCategory();
//        Toast.makeText(getContext(), String.valueOf(Objects.requireNonNull(comingWithTransactionAndCategory).get(0)), Toast.LENGTH_SHORT).show();

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);

        ImageButton button = root.findViewById(R.id.addButton);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddNewCategory.class);
            startActivityForResult.launch(intent);
        });

        return root;
    }

    @Override
    public void onNoteClick(int position) {
//        ((MainActivity) requireActivity()).turnOnProgressBar();
//        Intent intent = new Intent(getActivity(), AddNewCategoryElement.class);
//        startActivityForResult.launch(intent);
        Category category = categoryViewModel.getCategory(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}