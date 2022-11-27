package com.example.budgetmanagement.ui.coming;

import static com.example.budgetmanagement.ui.details.ComingDetails.MODE_AFTER_DEADLINE;
import static com.example.budgetmanagement.ui.details.ComingDetails.MODE_NORMAL;
import static com.example.budgetmanagement.ui.details.ComingDetails.MODE_REALIZED;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingElementDetailsBinding;
import com.example.budgetmanagement.ui.details.ComingDetails;

import java.util.Calendar;

public class ComingElementDetails extends Fragment {

    public static final String COMING_ID_ARG = "comingId";
    private ComingElementDetailsBinding binding;
    private Coming coming;

    public static ComingElementDetails newInstance(int comingId) {
        ComingElementDetails fragment = new ComingElementDetails();
        Bundle args = new Bundle();
        args.putInt(COMING_ID_ARG, comingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.coming_element_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int comingId = getArguments() != null ? getArguments().getInt(COMING_ID_ARG, -1) : -1;

        if (comingId == -1) {
            Toast.makeText(requireContext(), R.string.not_found_id, Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        ComingViewModel comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);
        this.coming = comingViewModel.getComingById(comingId);

        int mode = getMode();
        ComingDetails comingDetails = new ComingDetails(comingId, this, mode);
        binding.setComingDetails(comingDetails);
    }

    private int getMode() {
        boolean isExecute = coming.isExecuted();
        if (isExecute) {
            return MODE_REALIZED;
        }

        boolean isBeforeDeadline = getRemainingDays(coming.getExpireDate()) > 0;
        return isBeforeDeadline ? MODE_NORMAL : MODE_AFTER_DEADLINE;
    }

    private int getRemainingDays(long repeatDate) {
        Calendar todayDate = Calendar.getInstance();
        Calendar deadlineDate = getCalendarWithValue(repeatDate);
        return deadlineDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}