package com.example.budgetmanagement.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.adapters.ComingExpandableListAdapter;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.TransactionFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.ArrayList;
import java.util.Calendar;

public class TransactionFragment extends Fragment {

    public static final String COMING_BOTTOM_SHEET_TAG = "coming_bottom_sheet";
    private ArrayList<Section> currentSectionList = new ArrayList<>();
    private ComingExpandableListAdapter expandableListAdapter;
    private DatePickerDialog datePickerDialog;
    private TransactionFragmentBinding binding;
    private SectionMaker sectionMaker;
    private int selectedYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selectedYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = TransactionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        expandableListAdapter = new ComingExpandableListAdapter
                (requireContext(), this.currentSectionList, this, iconPack);

        binding.expandableListView.setAdapter(expandableListAdapter);
        binding.yearPicker.setText(String.valueOf(selectedYear));

        TransactionViewModel transactionViewModel =
                new ViewModelProvider(this).get(TransactionViewModel.class);

        sectionMaker = new SectionMaker(transactionViewModel, requireContext(), selectedYear);

        transactionViewModel.getAllTransactionsByYear(selectedYear).observe(getViewLifecycleOwner(), list -> {
            this.currentSectionList = sectionMaker.prepareSections();
            notifyUpdatedList();
        });

//        int actualPositionToScroll = getActualPositionToScroll();
//        binding.expandableListView.smoothScrollToPositionFromTop(actualPositionToScroll, 0);

        binding.expandableListView.setOnChildClickListener
                ((parent, v, groupPosition, childPosition, id)
                        -> openDetailsFragment(groupPosition, childPosition, view));

        binding.expandableListView.setOnItemLongClickListener
                ((parent, v, flatPosition, id) -> showItemBottomSheetMenu(parent, flatPosition));

        binding.addButton.setOnClickListener(root -> Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_addNewComingElement));

        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> true);

        binding.yearPicker.setOnClickListener(v -> {
            prepareYearPicker();
        });
    }

    private int getActualPositionToScroll() {
        int monthNumber = getMonthFromDate(Calendar.getInstance().getTimeInMillis());
        int endPosition = monthNumber;
        for (int i=0; i<monthNumber; i++) {
            endPosition += this.currentSectionList.get(i).getComingAndTransactionList().size();
        }
        return endPosition;
    }

    private boolean openDetailsFragment(int groupPosition, int childPosition, View view) {
        Transaction transaction =
                expandableListAdapter.getChild(groupPosition, childPosition);
        TransactionDetails transactionDetails =
                TransactionDetails.newInstance(transaction.getTransactionId());
        Navigation.findNavController(view).navigate(
                R.id.action_navigation_coming_to_comingElementDetails,
                transactionDetails.getArguments());
        return true;
    }

    private boolean showItemBottomSheetMenu(AdapterView<?> parent, int flatPosition) {
        long packedPosition = ((ExpandableListView) parent).getExpandableListPosition(flatPosition);
        if (ExpandableListView.getPackedPositionType(packedPosition)
                == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

            Transaction transaction =
                    expandableListAdapter.getChild(groupPosition, childPosition);
            ActionTransactionHandler bottomSheet =
                    ActionTransactionHandler.newInstance(transaction.getTransactionId());
            bottomSheet.show(getParentFragmentManager(), COMING_BOTTOM_SHEET_TAG);
            return true;
        }
        return false;
    }

    private void prepareYearPicker() {
        final Calendar calendarInstance = Calendar.getInstance();
        int mMonth = calendarInstance.get(Calendar.MONTH);
        int mDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
         datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {}, this.selectedYear, mMonth, mDay);

        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().getTouchables().get(1).setVisibility(View.GONE);
        datePickerDialog.getDatePicker()
                .setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                    this.selectedYear = year;
                    binding.yearPicker.setText(String.valueOf(year));
                    sectionMaker.changeYear(year);
                    this.currentSectionList = sectionMaker.prepareSections();
                    notifyUpdatedList();
                    datePickerDialog.cancel();
        });
        datePickerDialog.show();
    }

    private void notifyUpdatedList() {
        expandableListAdapter.updateItems(this.currentSectionList);
        expandableListAdapter.notifyAdapter(binding.expandableListView);
    }

    private int getMonthFromDate(long dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        return date.get(Calendar.MONTH);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}