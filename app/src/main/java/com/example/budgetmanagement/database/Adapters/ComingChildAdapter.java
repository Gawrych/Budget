package com.example.budgetmanagement.database.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.ComingChildViewHolder;
import com.example.budgetmanagement.ui.Coming.onNoteListener;

import java.util.List;

public class ComingChildAdapter extends RecyclerView.Adapter<ComingChildViewHolder> {

    private List<ComingAndTransaction> items;
    private final onNoteListener noteListener;

    public ComingChildAdapter(List<ComingAndTransaction> list, onNoteListener noteListener) {
        this.items = list;
        this.noteListener = noteListener;
    }

    @NonNull
    @Override
    public ComingChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ComingChildViewHolder holder, int position) {
        ComingAndTransaction current = items.get(position);
        holder.bind(current.transaction.getTitle(), current.transaction.getAmount(), current.coming.getRepeatDate(), noteListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ComingChildViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coming_child_recycler_view, parent, false);

        return new ComingChildViewHolder(view);
    }

}
