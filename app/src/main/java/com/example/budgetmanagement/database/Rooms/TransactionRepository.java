package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

public class TransactionRepository {

    private BudgetRoomDatabase database;
    private TransactionDao transactionDao;

    public TransactionRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        transactionDao = database.transactionDao();
    }

    public long insert(Transaction transaction) {
       return transactionDao.insert(transaction);
    }

    public void update(Transaction transaction) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.update(transaction);
        });
    }

    public void delete(Transaction transaction) {
        BudgetRoomDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.delete(transaction);
        });
    }
}
