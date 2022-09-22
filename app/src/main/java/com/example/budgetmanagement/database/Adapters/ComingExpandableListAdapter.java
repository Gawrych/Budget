package com.example.budgetmanagement.database.Adapters;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_DATE_FORMAT;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.ui.Coming.Section;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.ArrayList;
import java.util.Calendar;

public class ComingExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private ArrayList<Section> items;
    private ImageView outOfDateIcon;

    public ComingExpandableListAdapter(Context context, ArrayList<Section> items) {
        this.context = context;
        this.items = items;
    }

    public void updateItems(ArrayList<Section> items) {
        this.items = items;
    }

    public void notifyAdapter(ExpandableListView expandableListView) {
        this.notifyDataSetChanged();
        for (int i=0; i < items.size(); i++) {
            expandableListView.collapseGroup(i);
            expandableListView.expandGroup(i);
        }
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return items.get(i).getComingAndTransactionList().size();
    }

    @Override
    public Object getGroup(int i) {
        return items.get(i);
    }

    @Override
    public ComingAndTransaction getChild(int i, int i1) {
        return items.get(i).getComingAndTransactionList().get(i1);
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Section section = items.get(i);
        String sectionTitle = getStringFromResId(section.getLabelId());
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coming_group_view, viewGroup, false);
        }
        TextView sectionName = view.findViewById(R.id.sectionName);
        TextView balance = view.findViewById(R.id.balance);
        TextView currency = view.findViewById(R.id.currency);
        balance.setText(section.getBalance());

        if (section.isBalanceNegative()) {
            balance.setTextColor(context.getResources().getColor(R.color.mat_red));
            currency.setTextColor(context.getResources().getColor(R.color.mat_red));
        } else {
            balance.setTextColor(context.getResources().getColor(R.color.mat_green));
            currency.setTextColor(context.getResources().getColor(R.color.mat_green));
        }

        sectionName.setText(sectionTitle);
        return view;
    }

    private String getStringFromResId(int resId) {
        return context.getString(resId);
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coming_child_view, viewGroup, false);
        }

        final TextView titleField = view.findViewById(R.id.titleLayout);
        final TextView amountField = view.findViewById(R.id.amountLayout);
        final TextView dateField = view.findViewById(R.id.repeatDate);
        final TextView currencyField = view.findViewById(R.id.currency);
        final TextView remainingDays = view.findViewById(R.id.remainingDays);
        final TextView daysText = view.findViewById(R.id.daysText);
        final TextView dateInfo = view.findViewById(R.id.dateInfo);
        outOfDateIcon = view.findViewById(R.id.outOfDateIcon);


        ComingAndTransaction item = getChild(i, i1);
        String amount = item.transaction.getAmount();
        long repeatDate = item.coming.getRepeatDate();
        boolean isExecuted = item.coming.isExecute();

        titleField.setText(item.transaction.getTitle());

        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(amountField, currencyField);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        amountField.setText(amount);
        dateField.setText(DateProcessor.parseDate(repeatDate, MONTH_NAME_DATE_FORMAT));

        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(repeatDate);

        int days = otherDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
        remainingDays.setText(String.valueOf(days));

        boolean afterDeadline = otherDate.before(todayDate);
        if (afterDeadline && !isExecuted) {
            remainingDays.setTextColor(view.getContext().getColor(R.color.mat_red));
            daysText.setTextColor(view.getContext().getColor(R.color.mat_red));
            dateInfo.setTextColor(view.getContext().getColor(R.color.mat_red));

            outOfDateIconSetResource(view, R.drawable.calendar, R.color.mat_red);
        } else {
            remainingDays.setTextColor(view.getContext().getColor(R.color.font_default));
            daysText.setTextColor(view.getContext().getColor(R.color.font_default));
            dateInfo.setTextColor(view.getContext().getColor(R.color.font_default));

            outOfDateIconSetResource(view, R.drawable.calendar, R.color.font_default);
        }

        if (isExecuted) {
            long executedDateInMillis = item.coming.getExecutedDate();
            remainingDays.setText(String.valueOf(getRemainingDays(todayDate, getCalendarWithValue(executedDateInMillis))));

            remainingDays.setTextColor(view.getContext().getColor(R.color.main_green));
            daysText.setTextColor(view.getContext().getColor(R.color.main_green));
            daysText.setText(R.string.paid);

            outOfDateIconSetResource(view, R.drawable.ic_baseline_done_all_24, R.color.main_green);
        }

        return view;
    }

    private int getRemainingDays(Calendar today, Calendar deadLine) {
        return today.get(Calendar.DAY_OF_YEAR) - deadLine.get(Calendar.DAY_OF_YEAR);
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    private void outOfDateIconSetResource(View view, int drawable, int color) {
        outOfDateIcon.setColorFilter(view.getContext().getResources().getColor(color));
        outOfDateIcon.setImageResource(drawable);
    }

    public void clearItems() {
        this.items.clear();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {}

    @Override
    public void onGroupCollapsed(int groupPosition) {}

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}
}
