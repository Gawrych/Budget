package com.example.budgetmanagement.database.Rooms;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ComingRepository {

    private BudgetRoomDatabase database;
    private ComingDao comingDao;

    private LiveData<List<Coming>> allComing;

    public ComingRepository(Application app) {
        database = BudgetRoomDatabase.getDatabase(app);
        comingDao = database.comingDao();
        comingDao.getAllComing();
    }

    public LiveData<List<Coming>> getAllComing() {
        return allComing;
    }

    public void insert(Coming coming) {

        database.databaseWriteExecutor.execute(() -> {
            comingDao.insert(coming);
        });
    }

    public void update(Coming coming) {
        database.databaseWriteExecutor.execute(() -> {
            comingDao.update(coming);
        });
    }

    public void delete(Coming coming) {
        database.databaseWriteExecutor.execute(() -> {
            comingDao.delete(coming);
        });
    }
}
