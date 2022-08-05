package com.example.budgetmanagement.ui.Coming;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.ComingViewHolder;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


public class ComingFragment extends Fragment implements ComingViewHolder.OnNoteListener {

    private ComingViewModel comingViewModel;
    private ComingFragmentBinding binding;
    private LiveData<List<ComingAndTransaction>> coming;
    private ComingAdapter januaryAdapter;
    private ComingAdapter februaryAdapter;
    private ComingAdapter marchAdapter;

    public final int JANUARY = 0;
    public final int FEBRUARY = 1;
    public final int MARCH = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coming_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        januaryAdapter = new ComingAdapter(new ComingAdapter.ComingDiff(), this);
        RecyclerView januaryRecyclerView = view.findViewById(R.id.januaryRecyclerView);
        januaryRecyclerView.setAdapter(januaryAdapter);
        januaryRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        februaryAdapter = new ComingAdapter(new ComingAdapter.ComingDiff(), this);
        RecyclerView februaryRecyclerView = view.findViewById(R.id.februaryRecyclerView);
        februaryRecyclerView.setAdapter(februaryAdapter);
        februaryRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        marchAdapter = new ComingAdapter(new ComingAdapter.ComingDiff(), this);
        RecyclerView marchRecyclerView = view.findViewById(R.id.marchRecyclerView);
        marchRecyclerView.setAdapter(marchAdapter);
        marchRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        coming = comingViewModel.getAllComingAndTransaction();
        coming.observe(getViewLifecycleOwner(), list -> setSection());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSection() {
        LiveData<List<ComingAndTransaction>> januaryItems = getTransactionByMonth(JANUARY);
        januaryItems.observe(getViewLifecycleOwner(), januaryAdapter::submitList);

        LiveData<List<ComingAndTransaction>> februaryItems = getTransactionByMonth(FEBRUARY);
        februaryItems.observe(getViewLifecycleOwner(), februaryAdapter::submitList);

        LiveData<List<ComingAndTransaction>> marchItems = getTransactionByMonth(MARCH);
        marchItems.observe(getViewLifecycleOwner(), marchAdapter::submitList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private LiveData<List<ComingAndTransaction>> getTransactionByMonth(int monthToFilterBy) {
        Calendar calendar = Calendar.getInstance();
//      You can add variable to check until month change first time and then stop. For higher performance
        return Transformations.map(coming,
                input -> input.stream().filter(comingAndTransaction -> {
                    calendar.setTimeInMillis(comingAndTransaction.coming.getRepeatDate());
                    int month = calendar.get(Calendar.MONTH);
                    return monthToFilterBy != month;
                }).collect(Collectors.toList()));
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