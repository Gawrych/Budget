package com.example.budgetmanagement.ui.coming;

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
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.ComingViewModel;
import com.example.budgetmanagement.databinding.ComingFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.ArrayList;
import java.util.Calendar;

public class ComingFragment extends Fragment {

    public static final String COMING_BOTTOM_SHEET_TAG = "coming_bottom_sheet";
    private ArrayList<Section> currentSectionList = new ArrayList<>();
    private ComingExpandableListAdapter expandableListAdapter;
    private DatePickerDialog datePickerDialog;
    private ComingFragmentBinding binding;
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
        binding = ComingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        expandableListAdapter = new ComingExpandableListAdapter
                (requireContext(), this.currentSectionList, this, iconPack);

        binding.expandableListView.setAdapter(expandableListAdapter);
        binding.pickedYear.setText(String.valueOf(selectedYear));

        ComingViewModel comingViewModel =
                new ViewModelProvider(this).get(ComingViewModel.class);

        sectionMaker = new SectionMaker(comingViewModel, selectedYear);

        comingViewModel.getAllComingAndTransactionByYear(selectedYear).observe(getViewLifecycleOwner(), list -> {
            this.currentSectionList = sectionMaker.prepareSections(true);
            notifyUpdatedList();
        });

//        int actualPositionToScroll = getActualPositionToScroll();
//        binding.expandableListView.smoothScrollToPositionFromTop(actualPositionToScroll, 0);

        binding.expandableListView.setOnChildClickListener
                ((parent, v, groupPosition, childPosition, id)
                        -> openDetailsFragment(groupPosition, childPosition, view));

        binding.expandableListView.setOnItemLongClickListener
                ((parent, v, flatPosition, id) -> showBottomSheetMenu(parent, flatPosition));

        binding.addButton.setOnClickListener(root -> Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_addNewComingElement));

        binding.categoriesButton.setOnClickListener(root -> Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_categoryList));

        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> true);

        binding.yearSelector.setOnClickListener(v -> {
            prepareYearPicker();
        });
    }

    private int getActualPositionToScroll() {
        int monthNumber = getMonthFromDate(Calendar.getInstance().getTimeInMillis());
        int endPosition = monthNumber;
        for (int i=0; i<monthNumber; i++) {
            endPosition = endPosition + this.currentSectionList.get(i).getComingAndTransactionList().size();
        }
        return endPosition;
    }

    private boolean openDetailsFragment(int groupPosition, int childPosition, View view) {
        ComingAndTransaction comingAndTransaction =
                expandableListAdapter.getChild(groupPosition, childPosition);
        ComingElementDetails comingElementDetails =
                ComingElementDetails.newInstance(comingAndTransaction.coming.getComingId());
        Navigation.findNavController(view).navigate(
                R.id.action_navigation_coming_to_comingElementDetails,
                comingElementDetails.getArguments());
        return true;
    }

    private boolean showBottomSheetMenu(AdapterView<?> parent, int flatPosition) {
        long packedPosition = ((ExpandableListView) parent).getExpandableListPosition(flatPosition);
        if (ExpandableListView.getPackedPositionType(packedPosition)
                == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

            ComingAndTransaction comingAndTransaction =
                    expandableListAdapter.getChild(groupPosition, childPosition);
            BottomSheetDialogComing bottomSheet =
                    BottomSheetDialogComing.newInstance(comingAndTransaction.coming.getComingId());
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
                    binding.pickedYear.setText(String.valueOf(year));
                    sectionMaker.changeYear(year);
                    this.currentSectionList = sectionMaker.prepareSections(false);
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