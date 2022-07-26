package com.example.budgetmanagement.ui.History;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.database.ViewModels.TransactionViewModel;

import java.time.LocalDate;

public class SubmitQuery {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int submitTransactionInsertQuery(ViewModelStoreOwner viewModelStoreOwner, NewTransactionDataCollector dataFromUserCollector) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(viewModelStoreOwner).get(TransactionViewModel.class);

        return (int) transactionViewModel.insert(new Transaction(0,
                dataFromUserCollector.getCategoryId(), dataFromUserCollector.getTitle(),
                dataFromUserCollector.getAmount().toString(), dataFromUserCollector.getDate(),
                LocalDate.now().toEpochDay(), dataFromUserCollector.isProfit()));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void submitHistoryInsertQuery(HistoryViewModel historyViewModel, int transactionId) {
        historyViewModel.insert(new History(0, transactionId, LocalDate.now().toEpochDay()));
    }

}
