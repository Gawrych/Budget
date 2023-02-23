package com.example.budgetmanagement.database.adapters;

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

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.databinding.TransactionChildViewBinding;
import com.example.budgetmanagement.ui.transaction.Section;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final IconPack iconPack;
    private ArrayList<Section> items;
    private final CategoryViewModel categoryViewModel;
    private final OnChildClickListener onChildClickListener;

    public TransactionExpandableListAdapter(Context context, ArrayList<Section> items, ViewModelStoreOwner owner, IconPack iconPack, OnChildClickListener onChildClickListener) {
        this.context = context;
        this.items = items;
        this.iconPack = iconPack;
        this.onChildClickListener = onChildClickListener;
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
//       TODO: Get back to well working version
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.transaction_group_view, viewGroup, false);
            final TransactionChildViewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.transaction_child_view, viewGroup, false);
            if (binding == null) return null;

            Transaction item = getChild(i, i1);
            Category category = categoryViewModel.getCategoryById(item.getCategoryId());
            long repeatDate = item.getDeadline();
            Drawable icon = convertIconIdToDrawable(category.getIcon());
            Drawable iconBackground = getIconBackground(category.getColor());

            binding.setTransactionFragment(this);
            binding.setTransactionId(item.getTransactionId());
            binding.setTitle(item.getTitle());
            binding.setAmount(item.getAmount());
            binding.setDeadlineDate(repeatDate);
            binding.setIsProfit(new BigDecimal(item.getAmount()).signum() > 0);
            binding.setIcon(icon);
            binding.setIconBackground(iconBackground);
            setRemainingDays(binding, repeatDate, item.isExecuted());

            view = binding.getRoot();
        }
        return view;
    }

    private void setRemainingDays(TransactionChildViewBinding binding, long deadline, boolean isExecuted) {
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

        binding.setRemainingDays(remainingDaysText);
        binding.setRemainingDaysTextColor(context.getColor(textColor));
    }

    private int getRemainingDaysNumber(long finalDate) {
        Calendar otherDate = Calendar.getInstance();
        otherDate.setTimeInMillis(finalDate);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = otherDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    private Drawable getIconBackground(int colorRes) {
        Drawable drawable = ResourcesCompat.getDrawable(this.context.getResources(), R.drawable.background_oval, null);
        if (drawable == null) return null;
        Drawable drawableWrapped = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawableWrapped, colorRes);
        return drawableWrapped;
    }

    private Drawable convertIconIdToDrawable(int iconId) {
        Icon iconFromIconPack = iconPack.getIcon(iconId);
        if (iconFromIconPack == null) return null;
        return iconFromIconPack.getDrawable();
    }

    public void onChildCLickListener(int transactionId) {
        this.onChildClickListener.onChildClickListener(transactionId);
    }

    public interface OnChildClickListener {
        void onChildClickListener(int transactionId);
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
