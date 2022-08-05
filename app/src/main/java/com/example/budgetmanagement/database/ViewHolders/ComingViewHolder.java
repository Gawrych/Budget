package com.example.budgetmanagement.database.ViewHolders;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingChildAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;

import java.util.List;

public class ComingViewHolder extends RecyclerView.ViewHolder {

    private final TextView sectionName;
    private final RecyclerView childRecyclerView;

    public ComingViewHolder(View itemView) {
        super(itemView);
        sectionName = itemView.findViewById(R.id.sectionName);
        childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(ComingChildViewHolder.OnNoteListener onNoteListener, Context context, int resId, List<ComingAndTransaction> sectionItems) {
        this.sectionName.setText(getStringFromResId(resId, context));
        Log.d("ErrorCheck", "January:  " + sectionItems.get(0).transaction.getTitle());
        ComingChildAdapter adapter = new ComingChildAdapter(sectionItems, onNoteListener);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        childRecyclerView.setAdapter(adapter);
    }

    private String getStringFromResId(int resId, Context context) {
        return context.getString(resId);
    }
}
