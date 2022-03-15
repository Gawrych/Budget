package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

public class TransactionRepository {

    private BudgetRoomDatabase database;
    private TransactionDao transactionDao;

    public TransactionRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        transactionDao = database.transactionDao();
    }

    public void insert(Transaction transaction) {

        database.databaseWriteExecutor.execute(() -> {
            transactionDao.insert(transaction);
        });
    }

    public void update(Transaction transaction) {
        database.databaseWriteExecutor.execute(() -> {
            transactionDao.update(transaction);
        });
    }

    public void delete(Transaction transaction) {
        database.databaseWriteExecutor.execute(() -> {
            transactionDao.delete(transaction);
        });
    }
}
