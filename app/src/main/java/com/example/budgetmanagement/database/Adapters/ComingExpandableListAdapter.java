package com.example.budgetmanagement.database.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComingExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private HashMap<Integer, ArrayList<ComingAndTransaction>> items;
    private List<String> groupList;

    public ComingExpandableListAdapter(Context context, List<String> groupList,
                                       HashMap<Integer, ArrayList<ComingAndTransaction>> items) {
        this.context = context;
        this.items = items;
        this.groupList = groupList;
    }

    public void updateItems(HashMap<Integer, ArrayList<ComingAndTransaction>> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        observer.onChanged();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

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

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ComingAndTransaction item = getChild(i, i1);

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_view, viewGroup, false);
        }
        TextView title = view.findViewById(R.id.title);
        title.setText(item.transaction.getTitle());
        return view;
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
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    public void removeItem(int groupPosition, int childPosition) {
        HashMap<Integer, ArrayList<ComingAndTransaction>> updated = new HashMap<>(items);
        updated.get(groupPosition).remove(childPosition);
        this.items = updated;
    }
}
