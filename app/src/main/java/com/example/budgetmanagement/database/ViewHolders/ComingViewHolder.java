package com.example.budgetmanagement.database.ViewHolders;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Adapters.ComingChildAdapter;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.ui.Coming.OnNoteListener;
import com.example.budgetmanagement.ui.Coming.ParentOnNoteListener;
import com.example.budgetmanagement.ui.utils.AmountFieldModifierToViewHolder;

import java.util.List;

public class ComingViewHolder extends RecyclerView.ViewHolder implements OnNoteListener {

    private final TextView sectionName;
    private final RecyclerView childRecyclerView;
    private final ParentOnNoteListener parentOnNoteListener;
    private final TextView balance;
    private final TextView currency;

    public ComingViewHolder(View itemView, ParentOnNoteListener parentOnNoteListener) {
        super(itemView);
        this.parentOnNoteListener = parentOnNoteListener;
        sectionName = itemView.findViewById(R.id.sectionName);
        childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        currency = itemView.findViewById(R.id.currency);
        balance = itemView.findViewById(R.id.balance);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(RecyclerView.RecycledViewPool pool, Context context, int resId, List<ComingAndTransaction> sectionItems, String balance) {
        this.sectionName.setText(getStringFromResId(resId, context));
        this.balance.setText(balance);
        AmountFieldModifierToViewHolder amountFieldModifierToViewHolder = new AmountFieldModifierToViewHolder(this.balance, this.currency);
        amountFieldModifierToViewHolder.setRedColorIfIsNegative(balance);

        ComingChildAdapter adapter = new ComingChildAdapter(sectionItems, this);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        childRecyclerView.setRecycledViewPool(pool);
        childRecyclerView.setAdapter(adapter);
    }

    private String getStringFromResId(int resId, Context context) {
        return context.getString(resId);
    }

    @Override
    public void onNoteClick(int childPosition) {
        parentOnNoteListener.onItemClick(getBindingAdapterPosition(), childPosition);
    }
}
