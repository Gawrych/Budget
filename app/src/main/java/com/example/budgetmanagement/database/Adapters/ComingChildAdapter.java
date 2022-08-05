package com.example.budgetmanagement.database.Adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.ComingChildViewHolder;

import java.util.List;

public class ComingChildAdapter extends RecyclerView.Adapter<ComingChildViewHolder> {

    private List<ComingAndTransaction> items;
    private ComingChildViewHolder.OnNoteListener mOnNoteListener;

    public ComingChildAdapter(List<ComingAndTransaction> list, ComingChildViewHolder.OnNoteListener onNoteListener) {
        this.items = list;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ComingChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ComingChildViewHolder holder, int position) {
        Log.d("ErrorCheck", "OnBindViewHolder ChildAdapter" + position);
        ComingAndTransaction current = items.get(position);
        Log.d("ErrorCheck", "January list: " + current.transaction.getTitle());
        holder.bind(current.transaction.getTitle(), current.transaction.getAmount(), current.coming.getRepeatDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ComingChildViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coming_child_recycler_view, parent, false);

        return new ComingChildViewHolder(view, mOnNoteListener);
    }

}
