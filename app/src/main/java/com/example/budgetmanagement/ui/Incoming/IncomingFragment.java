package com.example.budgetmanagement.ui.Incoming;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.budgetmanagement.databinding.IncomingFragmentBinding;


public class IncomingFragment extends Fragment {

    private IncomingViewModel incomingViewModel;
    private IncomingFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        incomingViewModel =
                new ViewModelProvider(this).get(IncomingViewModel.class);

        binding = IncomingFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.TextViewIncoming;
        incomingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}