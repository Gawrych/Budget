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

@Database(entities = {Transaction.class, Category.class}, version = 6, exportSchema = false)
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
                Category category4 = new Category(4, "Podatki", 252, -65281, "-2500", System.currentTimeMillis(), 0);
                Category category5 = new Category(5, "Mieszkanie", 471, -7432974, "-3200", System.currentTimeMillis(), 0);
                Category category6 = new Category(6, "Subskrypcje", 278, -549531, "-300", System.currentTimeMillis(), 0);
                Category category7 = new Category(7, "Koszta JDG", 261, -49023, "-3000", System.currentTimeMillis(), 0);
                categoryDao.insert(category);
                categoryDao.insert(category2);
                categoryDao.insert(category3);
                categoryDao.insert(category4);
                categoryDao.insert(category5);
                categoryDao.insert(category6);
                categoryDao.insert(category7);

                TransactionDao transactionDao = INSTANCE.transactionDao();
                Calendar calendar = Calendar.getInstance();
                calendar.set(2023, 0, 10);

                for (int i=1; i<13; i++) {
                    Calendar fewDaysBeforeTheDeadline = Calendar.getInstance();
                    fewDaysBeforeTheDeadline.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    fewDaysBeforeTheDeadline.add(Calendar.DAY_OF_MONTH, -5);

                    Transaction transaction = new Transaction(i,
                            3,
                            "Wypłata",
                            "6000",
                            System.currentTimeMillis(),
                            0,
                            System.currentTimeMillis() > calendar.getTimeInMillis(),
                            calendar.getTimeInMillis(),
                            calendar.get(Calendar.YEAR),
                            fewDaysBeforeTheDeadline.getTimeInMillis());
                    transactionDao.insert(transaction);

                    if ((calendar.get(Calendar.MONTH) % 2) == 1) {

                        calendar.add(Calendar.DAY_OF_MONTH, 6);
                        Transaction transaction2 = new Transaction(i+12,
                                1,
                                "Paliwo",
                                "-450",
                                System.currentTimeMillis(),
                                0,
                                System.currentTimeMillis() > calendar.getTimeInMillis(),
                                calendar.getTimeInMillis(),
                                calendar.get(Calendar.YEAR),
                                fewDaysBeforeTheDeadline.getTimeInMillis());
                        transactionDao.insert(transaction2);
                    }

                    calendar.add(Calendar.DAY_OF_MONTH, 3);

                    Transaction transaction3 = new Transaction(i+24,
                            4,
                            "Mały ZUS",
                            "-341.72",
                            System.currentTimeMillis(),
                            0,
                            System.currentTimeMillis() > calendar.getTimeInMillis(),
                            calendar.getTimeInMillis(),
                            calendar.get(Calendar.YEAR),
                            fewDaysBeforeTheDeadline.getTimeInMillis());
                    transactionDao.insert(transaction3);

                    calendar.add(Calendar.DAY_OF_MONTH, 4);

                    Transaction transaction4 = new Transaction(i+36,
                            5,
                            "Czynsz",
                            "-2700",
                            System.currentTimeMillis(),
                            0,
                            System.currentTimeMillis() > calendar.getTimeInMillis(),
                            calendar.getTimeInMillis(),
                            calendar.get(Calendar.YEAR),
                            fewDaysBeforeTheDeadline.getTimeInMillis());
                    transactionDao.insert(transaction4);

                    calendar.set(Calendar.DAY_OF_MONTH, 10);
                    fewDaysBeforeTheDeadline.add(Calendar.DAY_OF_MONTH, 5);
                    Transaction transaction5 = new Transaction(i+48,
                            6,
                            "Storytel",
                            "-39.90",
                            System.currentTimeMillis(),
                            0,
                            System.currentTimeMillis() > calendar.getTimeInMillis(),
                            calendar.getTimeInMillis(),
                            calendar.get(Calendar.YEAR),
                            fewDaysBeforeTheDeadline.getTimeInMillis());
                    transactionDao.insert(transaction5);
                    fewDaysBeforeTheDeadline.add(Calendar.DAY_OF_MONTH, -5);

                    calendar.set(Calendar.DAY_OF_MONTH, 15);

                    Transaction transaction6 = new Transaction(i+60,
                            7,
                            "Rata za sprzęt",
                            "-1472.82",
                            System.currentTimeMillis(),
                            0,
                            System.currentTimeMillis() > calendar.getTimeInMillis(),
                            calendar.getTimeInMillis(),
                            calendar.get(Calendar.YEAR),
                            fewDaysBeforeTheDeadline.getTimeInMillis());
                    transactionDao.insert(transaction6);

                    calendar.set(Calendar.MONTH, i);
                    calendar.set(Calendar.DAY_OF_MONTH, 10);
                }
            });
        }
    };
}
