package com.example.budgetmanagement.database.adapters;

import static com.example.budgetmanagement.ui.utils.DateProcessor.MONTH_NAME_DATE_FORMAT;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.ui.transaction.Section;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ComingExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final IconPack iconPack;
    private ArrayList<Section> items;
    private final CategoryViewModel categoryViewModel;

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
    public Transaction getChild(int i, int i1) {
        return items.get(i).getComingAndTransactionList().get(i1);
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Section section = items.get(i);
        String sectionTitle = section.getLabel();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.transaction_group_view, viewGroup, false);
        }
        TextView sectionName = view.findViewById(R.id.sectionName);
        sectionName.setText(sectionTitle);
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            view = getInflatedView(viewGroup);
        }

        TextView titleField = view.findViewById(R.id.titleLayout);
        TextView amountField = view.findViewById(R.id.amount);
        TextView dateField = view.findViewById(R.id.repeatDate);
        TextView remainingDaysField = view.findViewById(R.id.remainingDays);
        ImageView mainIconField = view.findViewById(R.id.outOfDateIcon);

        Transaction item = getChild(i, i1);
        String amount = item.getAmount();
        long repeatDate = item.getDeadline();
        boolean isExecuted = item.isExecuted();

        setTitleField(titleField, item.getTitle());
        setAmountField(amountField, amount);
        setDateField(dateField, repeatDate);
        setRemainingDays(remainingDaysField, repeatDate, isExecuted);
        setMainIcon(mainIconField, item.getCategoryId());

        return view;
    }

    private View getInflatedView(ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.transaction_child_view, viewGroup, false);
    }

    private void setTitleField(TextView titleField, String title) {
        titleField.setText(title);
    }

    private void setAmountField(TextView amountField, String amount) {
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(amountField);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(amount);
        amountField.setText(getAmountWithCurrency(amount));
    }

    private String getAmountWithCurrency(String amount) {
        String currency = context.getString(R.string.polish_currency);
        return context.getString(R.string.amount_with_currency, amount, currency);
    }

    private void setDateField(TextView dateField, long repeatDate) {
        dateField.setText(DateProcessor.parseDate(repeatDate, MONTH_NAME_DATE_FORMAT));
    }

    private void setRemainingDays(TextView remainingDays, long deadline, boolean isExecuted) {
        int days = getRemainingDaysNumber(deadline);
        int textColor;
        String remainingDaysText;

        if (isExecuted) {
            remainingDaysText = context.getString(R.string.realized);
            textColor = R.color.mat_green;
        } else if (days == 0) {
            remainingDaysText = context.getString(R.string.today);
            textColor = R.color.mat_red;
        } else if (days < 0) {
            remainingDaysText = context.getString(R.string.for_number_days, Math.abs(days));
            textColor = R.color.mat_red;
        } else {
            remainingDaysText = context.getString(R.string.in_number_days, days);
            textColor = R.color.font_default;
        }

        remainingDays.setText(remainingDaysText);
        remainingDays.setTextColor(context.getColor(textColor));
    }

    private int getRemainingDaysNumber(long finalDate) {
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(finalDate);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = otherDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    private void setMainIcon(ImageView mainIcon, int categoryId) {
        Category category = categoryViewModel.getCategoryById(categoryId);

        int iconId = category.getIcon();
        Drawable icon = Objects.requireNonNull(iconPack.getIcon(iconId)).getDrawable();
        mainIcon.setImageDrawable(icon);

        int color = category.getColor();
        Drawable ovalWithColorBackground = getDrawableWithColor(R.drawable.background_oval, color);

        if (ovalWithColorBackground != null) {
            mainIcon.setBackground(ovalWithColorBackground);
        }
    }
    private Drawable getDrawableWithColor(int drawableId, int colorId) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableId, null);

        if (drawable != null) {
            Drawable drawableWrapped = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawableWrapped, colorId);
            return drawableWrapped;
        }

        return null;
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
