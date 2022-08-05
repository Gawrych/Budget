package com.example.budgetmanagement.ui.Coming;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.ComingChildViewHolder;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


public class ComingFragment extends Fragment implements ComingChildViewHolder.OnNoteListener {

    private ComingViewModel comingViewModel;
    private ComingFragmentBinding binding;
    private LiveData<List<ComingAndTransaction>> coming;
    private ComingAdapter adapter;
    private List<ComingAndTransaction> globalList;
    private List<Section> sectionList = new ArrayList<>();

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

        adapter = new ComingAdapter(new ComingAdapter.ComingDiff(), this, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        coming = comingViewModel.getAllComingAndTransaction();
        coming.observe(getViewLifecycleOwner(), list -> {
            setSections(list);
            adapter.submitList(sectionList);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSections(List<ComingAndTransaction> list) {
        globalList = list;

        Section january = new Section(R.string.january, getTransactionByMonth(0));
        Section february = new Section(R.string.february, getTransactionByMonth(1));

        Log.d("ErrorCheck", "January list: " + january.getComingAndTransactionList().get(0).transaction.getTitle());

        sectionList.add(january);
        sectionList.add(february);

//        LiveData<List<ComingAndTransaction>> januaryItems = getTransactionByMonth(JANUARY);
//        januaryItems.observe(getViewLifecycleOwner(), januaryAdapter::submitList);
//
//        LiveData<List<ComingAndTransaction>> februaryItems = getTransactionByMonth(FEBRUARY);
//        februaryItems.observe(getViewLifecycleOwner(), februaryAdapter::submitList);
//
//        LiveData<List<ComingAndTransaction>> marchItems = getTransactionByMonth(MARCH);
//        marchItems.observe(getViewLifecycleOwner(), marchAdapter::submitList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<ComingAndTransaction> getTransactionByMonth(int monthToFilterBy) {
        Calendar calendar = Calendar.getInstance();
//      You can add variable to check until month change first time and then stop. For higher performance
        return globalList.stream()
                .filter(comingAndTransaction -> {
                    calendar.setTimeInMillis(comingAndTransaction.coming.getRepeatDate());
                    int month = calendar.get(Calendar.MONTH);
                    return monthToFilterBy == month;
                }).collect(Collectors.toList());
    }

    @Override
    public void onNoteClick(int position) {
//        ((MainActivity) requireActivity()).turnOnProgressBar();
//        Intent intent = new Intent(getActivity(), AddNewCategoryElement.class);
//        startActivityForResult.launch(intent);
    }

    public ComingChildViewHolder.OnNoteListener getOnNoteListener() {
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}