package com.example.budgetmanagement.database.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ComingExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private HashMap<Integer, ArrayList<ComingAndTransaction>> items;
    private final List<String> groupList;

//    TODO: Change to sectionList?

    public ComingExpandableListAdapter(Context context, List<String> groupList,
                                       HashMap<Integer, ArrayList<ComingAndTransaction>> items) {
        this.context = context;
        this.items = items;
        this.groupList = groupList;
    }

    public void updateItems(HashMap<Integer, ArrayList<ComingAndTransaction>> items) {
        this.items = items;
    }

    public void notifyAdapter(ExpandableListView expandableListView) {
        this.notifyDataSetChanged();
        for (int i=0; i < groupList.size(); i++) {
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
        return items.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public ComingAndTransaction getChild(int i, int i1) {
        return items.get(i).get(i1);
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String sectionTitle = getGroup(i).toString();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parent_view, viewGroup, false);
        }
        TextView sectionName = view.findViewById(R.id.sectionName);
        sectionName.setText(getStringFromResId(getStringResId(sectionTitle)));
        return view;
    }

    private int getStringResId(String stringName) {
        return context.getResources().getIdentifier(stringName, "string", context.getPackageName());
    }

    private String getStringFromResId(int resId) {
        return context.getString(resId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final TextView titleField;
        final TextView amountField;
        final TextView dateField;
        final TextView currencyField;
        final TextView remainingDays;
        final TextView daysText;
        final ImageView outOfDateIcon;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_view, viewGroup, false);
        }

        titleField = view.findViewById(R.id.title);
        amountField = view.findViewById(R.id.amount);
        dateField = view.findViewById(R.id.repeatDate);
        currencyField = view.findViewById(R.id.currency);
        outOfDateIcon = view.findViewById(R.id.outOfDateIcon);
        remainingDays = view.findViewById(R.id.remainingDays);
        daysText = view.findViewById(R.id.daysText);

        ComingAndTransaction item = getChild(i, i1);
        String amount = item.transaction.getAmount();
        long repeatDate = item.coming.getRepeatDate();
        boolean execute = item.coming.isExecute();

        titleField.setText(item.transaction.getTitle());

        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(amountField, currencyField);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        amountField.setText(amount);
        dateField.setText(DateProcessor.parseDate(repeatDate));

        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(repeatDate);

        int days = otherDate.get(Calendar.DAY_OF_YEAR) - todayDate.get(Calendar.DAY_OF_YEAR);
        remainingDays.setText(String.valueOf(days));

        if (otherDate.before(todayDate) && !execute) {
            remainingDays.setTextColor(view.getContext().getResources().getColor(R.color.mat_red));
            daysText.setTextColor(view.getContext().getResources().getColor(R.color.mat_red));

            outOfDateIcon.setImageResource(R.drawable.calendar);
            outOfDateIcon.setColorFilter(view.getContext().getResources().getColor(R.color.mat_red));
        } else {
            remainingDays.setTextColor(view.getContext().getResources().getColor(R.color.font_default));
            daysText.setTextColor(view.getContext().getResources().getColor(R.color.font_default));

            outOfDateIcon.setImageResource(R.drawable.calendar);
            outOfDateIcon.setColorFilter(view.getContext().getResources().getColor(R.color.font_default));
        }

        if (execute) {
            remainingDays.setTextColor(view.getContext().getResources().getColor(R.color.main_green));
            remainingDays.setText("0");
            daysText.setTextColor(view.getContext().getResources().getColor(R.color.main_green));
            outOfDateIcon.setColorFilter(view.getContext().getResources().getColor(R.color.main_green));
            outOfDateIcon.setImageResource(R.drawable.ic_baseline_done_all_24);
        }

        return view;
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
