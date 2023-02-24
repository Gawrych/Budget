package com.example.budgetmanagement.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.adapters.TransactionExpandableListAdapter;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.TransactionViewModel;
import com.example.budgetmanagement.databinding.TransactionFragmentBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.ArrayList;
import java.util.Calendar;

public class TransactionFragment extends Fragment implements TransactionExpandableListAdapter.OnChildClickListener {

    public static final String TRANSACTION_ACTION_HANDLER_TAG = "TransactionActionHandlerTag";
    private ArrayList<Section> currentSectionList = new ArrayList<>();
    private TransactionExpandableListAdapter expandableListAdapter;
    private DatePickerDialog datePickerDialog;
    private TransactionFragmentBinding binding;
    private SectionMaker sectionMaker;
    private int selectedYear;
    private View view;
    private TransactionViewModel transactionViewModel;

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
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
        this.binding.setTransactionFragment(this);

        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        expandableListAdapter = new TransactionExpandableListAdapter
                (requireContext(), this.currentSectionList, this, iconPack, this);

        binding.expandableListView.setAdapter(expandableListAdapter);
        binding.yearPicker.setText(String.valueOf(selectedYear));

        transactionViewModel =
                new ViewModelProvider(this).get(TransactionViewModel.class);

        sectionMaker = new SectionMaker(transactionViewModel, requireContext(), selectedYear);

        transactionViewModel.getAllTransactionsByYear(selectedYear).observe(getViewLifecycleOwner(), list -> {
            this.currentSectionList = sectionMaker.prepareSections();
            notifyUpdatedList();
        });

//        int actualPositionToScroll = getActualPositionToScroll();
//        binding.expandableListView.smoothScrollToPositionFromTop(actualPositionToScroll, 0);
    }

    public void addButtonOnCLick() {
        Navigation.findNavController(this.view)
                .navigate(R.id.action_navigation_transaction_to_addNewTransactionElement);
    }

    private int getActualPositionToScroll() {
        int monthNumber = getMonthFromDate(Calendar.getInstance().getTimeInMillis());
        int endPosition = monthNumber;
        for (int i=0; i<monthNumber; i++) {
            endPosition += this.currentSectionList.get(i).getTransactionList().size();
        }
        return endPosition;
    }

    @Override
    public void openDetailsFragment(int transactionId) {
        Transaction transaction = transactionViewModel.getTransactionById(transactionId);
        TransactionDetails transactionDetails =
                TransactionDetails.newInstance(transaction.getTransactionId());
        Navigation.findNavController(view).navigate(
                R.id.action_navigation_transaction_to_transactionElementDetails,
                transactionDetails.getArguments());
    }

    @Override
    public void showActionTransactionHandler(int transactionId) {
        ActionTransactionItemHandler bottomSheet =
                ActionTransactionItemHandler.newInstance(transactionId);
        bottomSheet.show(getParentFragmentManager(), TRANSACTION_ACTION_HANDLER_TAG);
    }

    public void yearSelectorOnClick() {
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