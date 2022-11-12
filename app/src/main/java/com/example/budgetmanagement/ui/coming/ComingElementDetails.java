package com.example.budgetmanagement.ui.coming;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.example.budgetmanagement.ui.coming.Details.MODE_AFTER_DEADLINE;
import static com.example.budgetmanagement.ui.coming.Details.MODE_NORMAL;
import static com.example.budgetmanagement.ui.coming.Details.MODE_REALIZED;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingElementDetailsBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

public class ComingElementDetails extends Fragment {

    public static final String COMING_ID_ARG = "comingId";
    private ComingElementDetailsBinding binding;
    private Transaction transaction;
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
        Details details = new Details(comingId, this, mode);
        binding.setDetails(details);
    }

    private int getMode() {
        boolean isExecute = coming.isExecute();
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