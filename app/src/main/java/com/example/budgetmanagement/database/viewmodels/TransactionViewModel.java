package com.example.budgetmanagement.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.budgetmanagement.database.rooms.Transaction;
import com.example.budgetmanagement.database.rooms.TransactionRepository;


public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;

    public TransactionViewModel(@NonNull Application app) {
        super(app);
        transactionRepository = new TransactionRepository(app);
    }

    public long insert(Transaction transaction) {
       return transactionRepository.insert(transaction);
    }

    public void update(Transaction transaction) {
        transactionRepository.update(transaction);
    }
}