package com.example.budgetmanagement.ui.coming;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Coming;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingBottomSheetDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class BottomSheetDialogComing extends BottomSheetDialogFragment {

    private ComingBottomSheetDialogBinding binding;
    private static final String BUNDLE_COMING_VALUE = "coming_value";
    private ComingViewModel comingViewModel;
    private Coming coming;

    public static BottomSheetDialogComing newInstance(int comingId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COMING_VALUE, comingId);
        BottomSheetDialogComing bottomComing = new BottomSheetDialogComing();
        bottomComing.setArguments(bundle);
        return bottomComing;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ComingBottomSheetDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.comingViewModel = new ViewModelProvider(this).get(ComingViewModel.class);

        int comingId = getArguments() != null ? getArguments().getInt(BUNDLE_COMING_VALUE, -1) : -1;

        if (comingId == -1) {
            Toast.makeText(requireContext(), R.string.coming_id_not_found, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        this.coming = comingViewModel.getComingById(comingId);

        if (coming == null) {
            Toast.makeText(requireContext(), R.string.coming_not_found+comingId, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        if (coming.isExecuted()) {
            changeButtonText();
        }

        binding.executeLayout.setOnClickListener(v -> execute());

        binding.duplicateLayout.setOnClickListener(v -> createNewComingByThisPattern());

        binding.editLayout.setOnClickListener(v -> edit());

        binding.deleteLayout.setOnClickListener(v -> delete());
    }

    private void changeButtonText() {
        binding.executeLabel.setText(R.string.realized);
    }

    public void execute() {
        boolean negateExecute = !this.coming.isExecuted();
        this.coming.setExecuted(negateExecute);
        this.coming.setExecutedDate(getTodayDate().getTimeInMillis());
        updateComingInDatabase();
        dismiss();
    }

    private Calendar getTodayDate() {
        return Calendar.getInstance();
    }

    private void updateComingInDatabase() {
        comingViewModel.update(this.coming);
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    removeFromDatabase();
                    dismiss();
                    Toast.makeText(requireContext(), R.string.element_removed, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> {}).show();
    }

    private void removeFromDatabase() {
        comingViewModel.delete(this.coming);
    }

    private void edit() {
        View rootView = getRootView();
        if (rootView == null) {
            dismiss();
            return;
        }

        EditComingElement editComingElement = EditComingElement.newInstance(coming.getComingId());
        Bundle bundle = editComingElement.getArguments();

        Navigation.findNavController(rootView)
                .navigate(R.id.action_navigation_incoming_to_editComingElement, bundle);
        dismiss();
    }

    private void createNewComingByThisPattern() {
        View rootView = getRootView();
        if (rootView == null) {
            dismiss();
            return;
        }

        AddNewComingElement addNewComingElement = AddNewComingElement.newInstance(coming.getComingId());
        Bundle bundle = addNewComingElement.getArguments();

        Navigation.findNavController(rootView)
                .navigate(R.id.action_navigation_incoming_to_addNewComingElement, bundle);
        dismiss();
    }

    private View getRootView() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            return null;
        }

        return  parentFragment.getView();
    }
}