package com.example.budgetmanagement.database.rooms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaction.class, Category.class}, version = 263, exportSchema = false)
public abstract class BudgetRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();

    private static volatile BudgetRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BudgetRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BudgetRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BudgetRoomDatabase.class, "BudgetManagement")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                CategoryDao categoryDao = INSTANCE.categoryDao();
                Category category = new Category(1, "Różne", 955, -12679077, "-1", System.currentTimeMillis(), 0);
                Category category2 = new Category(2, "Spożywcze", 255, -14123782, "-1", System.currentTimeMillis(), 0);
                Category category3 = new Category(3, "Pensje", 256, -1554347, "1", System.currentTimeMillis(), 0);
                categoryDao.insert(category);
                categoryDao.insert(category2);
                categoryDao.insert(category3);

                TransactionDao transactionDao = INSTANCE.transactionDao();
                Calendar calendar = Calendar.getInstance();

                for (int i=0; i<5; i++) {
                    Transaction transaction = new Transaction(i, 2,
                            "Samochód", "-200", System.currentTimeMillis(),
                            0, false, calendar.getTimeInMillis(), calendar.get(Calendar.YEAR), 1241155550L);
                    transactionDao.insert(transaction);
                    calendar.add(Calendar.DAY_OF_MONTH, 12);
                }
            });
        }
    };
}
