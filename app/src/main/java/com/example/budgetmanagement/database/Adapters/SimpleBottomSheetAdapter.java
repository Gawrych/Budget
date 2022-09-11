package com.example.budgetmanagement.database.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.ViewHolders.SimpleBottomSheetViewHolder;
import com.example.budgetmanagement.ui.Coming.OnNoteListener;

import java.util.List;

public class SimpleBottomSheetAdapter extends RecyclerView.Adapter<SimpleBottomSheetViewHolder> {

    private final List<String> items;
    private final OnNoteListener noteListener;

    public SimpleBottomSheetAdapter(List<String> list, OnNoteListener noteListener) {
        this.items = list;
        this.noteListener = noteListener;
    }

    @NonNull
    @Override
    public SimpleBottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @Override
    public void onBindViewHolder(SimpleBottomSheetViewHolder holder, int position) {
        String current = items.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public SimpleBottomSheetViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.title_bottom_sheet_details_child, parent, false);

        return new SimpleBottomSheetViewHolder(view, noteListener);
    }

}
