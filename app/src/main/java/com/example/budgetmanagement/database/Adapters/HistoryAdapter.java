package com.example.budgetmanagement.database.Adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.ViewHolders.HistoryViewHolder;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;

import java.util.List;

public class HistoryAdapter extends ListAdapter<HistoryAndTransaction, HistoryViewHolder> {

    private HistoryViewHolder.OnNoteListener mOnNoteListener;

    public HistoryAdapter(@NonNull DiffUtil.ItemCallback<HistoryAndTransaction> diffCallback, HistoryViewHolder.OnNoteListener onNoteListener) {
        super(diffCallback);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryAndTransaction historyAndTransaction = getItem(position);
        holder.bind(historyAndTransaction.transaction.getTitle(),
                historyAndTransaction.transaction.getAmount(),
                historyAndTransaction.history.getAddDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HistoryViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_recycler_view, parent, false);

        return new HistoryViewHolder(view, mOnNoteListener);
    }

    public static class HistoryAndTransactionDiff extends DiffUtil.ItemCallback<HistoryAndTransaction> {

        @Override
        public boolean areItemsTheSame(@NonNull HistoryAndTransaction oldItem, @NonNull HistoryAndTransaction newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull HistoryAndTransaction oldItem, @NonNull HistoryAndTransaction newItem) {
            return oldItem.history.getHistoryId() == newItem.history.getHistoryId();
        }
    }
}
