package com.example.budgetmanagement.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.Rooms.TransactionRepository;


public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;

    public TransactionViewModel(@NonNull Application app) {
        super(app);
        transactionRepository = new TransactionRepository(app);
    }

    public long insert(Transaction transaction) {
       return transactionRepository.insert(transaction);
    }
}