package com.example.budgetmanagement.database.adapters;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_DATE_FORMAT;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.ui.coming.Section;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Calendar;

public class ComingExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final IconPack iconPack;
    private ArrayList<Section> items;
    private ImageView outOfDateIcon;
    private CategoryViewModel categoryViewModel;

    public ComingExpandableListAdapter(Context context, ArrayList<Section> items, ViewModelStoreOwner owner, IconPack iconPack) {
        this.context = context;
        this.items = items;
        this.iconPack = iconPack;
        categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);
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
            balance.setTextColor(context.getColor(R.color.black));
            currency.setTextColor(context.getColor(R.color.black));
        } else {
            balance.setTextColor(context.getColor(R.color.mat_green));
            currency.setTextColor(context.getColor(R.color.mat_green));
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
        final TextView dateInfo = view.findViewById(R.id.info);
        outOfDateIcon = view.findViewById(R.id.outOfDateIcon);


        ComingAndTransaction item = getChild(i, i1);
        String amount = item.transaction.getAmount();
        long repeatDate = item.coming.getExpireDate();
        boolean isExecuted = item.coming.isExecuted();

        titleField.setText(item.transaction.getTitle());

        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(amountField, currencyField);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        amountField.setText(amount);
        dateField.setText(DateProcessor.parseDate(repeatDate, MONTH_NAME_DATE_FORMAT));

        Calendar todayDate = Calendar.getInstance();
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(repeatDate);

        DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
        DateTime startDate = new DateTime(todayDate.getTimeInMillis(), defaultTimeZone);
        DateTime endDate = new DateTime(otherDate.getTimeInMillis(), defaultTimeZone);
        int days = Days.daysBetween(startDate.withTimeAtStartOfDay(), endDate.withTimeAtStartOfDay()).getDays();


        int categoryId = item.transaction.getCategoryId();
        Category category = categoryViewModel.getCategoryById(categoryId);

        int iconId = category.getIcon();
        Drawable icon = iconPack.getIcon(iconId).getDrawable();
        outOfDateIcon.setImageDrawable(icon);

        boolean afterDeadline = otherDate.before(todayDate);
        remainingDays.setText(String.valueOf(Math.abs(days)));
        if (afterDeadline && !isExecuted) {
            remainingDays.setTextColor(view.getContext().getColor(R.color.mat_red));
            daysText.setTextColor(view.getContext().getColor(R.color.mat_red));

            dateInfo.setTextColor(view.getContext().getColor(R.color.mat_red));

            if (days == 0) {
                remainingDays.setText("");
                daysText.setText("");
                dateInfo.setText(R.string.today);
            } else {
                daysText.setText("dni");
                dateInfo.setText(R.string.forDays);
            }
        } else {
            remainingDays.setTextColor(view.getContext().getColor(R.color.font_default));
            daysText.setText("dni");
            daysText.setTextColor(view.getContext().getColor(R.color.font_default));
            dateInfo.setText(R.string.inDays);
            dateInfo.setTextColor(view.getContext().getColor(R.color.font_default));
        }

        if (isExecuted) {
            remainingDays.setText("");
            daysText.setText("");
            dateInfo.setText(R.string.realized);
            dateInfo.setTextColor(view.getContext().getColor(R.color.main_green));
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
        outOfDateIcon.setColorFilter(view.getContext().getColor(color));
        outOfDateIcon.setImageResource(drawable);
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