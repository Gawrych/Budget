package com.example.budgetmanagement.ui.Coming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.ComingViewHolder;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;

import java.util.List;


public class ComingFragment extends Fragment implements ComingViewHolder.OnNoteListener {

    private ComingViewModel comingViewModel;
    private ComingFragmentBinding binding;
    private ComingAndTransaction comingAndTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.coming_fragment, container, false);
        final ComingAdapter adapter = new ComingAdapter(new ComingAdapter.ComingDiff(), this);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        comingViewModel.getAllComingAndTransaction().observe(getViewLifecycleOwner(), adapter::submitList);

        LiveData<List<ComingAndTransaction>> coming = comingViewModel.getAllComingAndTransaction();


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