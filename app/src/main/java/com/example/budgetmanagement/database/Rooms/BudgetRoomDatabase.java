package com.example.budgetmanagement.database.Rooms;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaction.class, Category.class, Coming.class, History.class, CategoryTransactionCrossRef.class}, version = 32, exportSchema = false)
public abstract class BudgetRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();
    public abstract ComingDao comingDao();
    public abstract HistoryDao historyDao();

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

    //  Populate database at first run
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                CategoryDao categoryDao = INSTANCE.categoryDao();
                Category category = new Category(1, "Różne", "ic_baseline_shopping_basket_24", 0, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                Category category2 = new Category(2, "Spożywcze", "ic_baseline_category_24", 0, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                Category category3 = new Category(3, "Paliwo", "ic_baseline_category_24", 1500, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                categoryDao.insert(category);
                categoryDao.insert(category2);
                categoryDao.insert(category3);

                TransactionDao transactionDao = INSTANCE.transactionDao();
                Transaction transaction = new Transaction(1, 1,
                        "RandomName", 200, 1,
                         LocalDate.now().toEpochDay(), true);
                Transaction transaction2 = new Transaction(2, 2,
                        "Kawa", 22, 1,
                        LocalDate.now().toEpochDay(), true);
                transactionDao.insert(transaction);
                transactionDao.insert(transaction2);

                ComingDao comingDao = INSTANCE.comingDao();
                Coming coming = new Coming(1, 1, 2, 5,
                        5454, 3232, LocalDate.now().toEpochDay());
                Coming coming2 = new Coming(2, 2, 2, 5,
                        5454, 3232, LocalDate.now().toEpochDay());
                comingDao.insert(coming2);
                comingDao.insert(coming);

                HistoryDao historyDao = INSTANCE.historyDao();
                History history = new History(1, 2, LocalDate.now().toEpochDay());
                History history2 = new History(2, 1, LocalDate.now().toEpochDay());
                historyDao.insert(history);
                historyDao.insert(history2);

            });
        }
    };
}
