package com.example.budgetmanagement.database.rooms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.budgetmanagement.R;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaction.class, Category.class, Coming.class}, version = 141, exportSchema = false)
public abstract class BudgetRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();
    public abstract ComingDao comingDao();

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
                Category category = new Category(1, "Różne", 955, R.color.mat_green, "0", LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                Category category2 = new Category(2, "Spożywcze", 255, R.color.mat_blue, "0", LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                Category category3 = new Category(3, "Pensje", 256, R.color.mat_red, "1500", LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay());
                categoryDao.insert(category);
                categoryDao.insert(category2);
                categoryDao.insert(category3);
//
                TransactionDao transactionDao = INSTANCE.transactionDao();
                Transaction transaction = new Transaction(1, 1,
                        "Samochód", "-200", 1,
                         0, true);
                Transaction transaction2 = new Transaction(2, 2,
                        "Kawa", "100", 1,
                        0, false);
                Transaction transaction3 = new Transaction(3, 2,
                        "Wypłata", "-300.00", LocalDate.now().toEpochDay(),
                       0, false);
                Transaction transaction4 = new Transaction(4, 1,
                        "Wypłata", "1000.00", LocalDate.now().toEpochDay(),
                        0, false);
                Transaction transaction5 = new Transaction(5, 3,
                        "Wypłata", "-500.00", LocalDate.now().toEpochDay(),
                        0, false);
                Transaction transaction6 = new Transaction(6, 3,
                        "Wypłata", "500.00", LocalDate.now().toEpochDay(),
                        0, false);
                transactionDao.insert(transaction);
                transactionDao.insert(transaction2);
                transactionDao.insert(transaction3);
                transactionDao.insert(transaction4);
                transactionDao.insert(transaction5);
                transactionDao.insert(transaction6);
//
//                Transaction transaction4 = new Transaction(4, 2,
//                        "Abonament na telefon", "99.00", LocalDate.now().toEpochDay(),
//                        0, false);
//                transactionDao.insert(transaction4);
//
//                Transaction transaction5 = new Transaction(5, 2,
//                        "Czynsz za mieszkanie", "99.00", LocalDate.now().toEpochDay(),
//                        0, false);
//                transactionDao.insert(transaction5);
//
//                Transaction transaction6 = new Transaction(6, 2,
//                        "Spożywcze", "99.00", LocalDate.now().toEpochDay(),
//                        0, false);
//                transactionDao.insert(transaction6);
//
//                Transaction transaction7 = new Transaction(7, 2,
//                        "Oddać rodzicom", "99.00", LocalDate.now().toEpochDay(),
//                        0, false);
//                transactionDao.insert(transaction7);
//
//                Transaction transaction8 = new Transaction(8, 2,
//                        "Pensja", "3012.55", LocalDate.now().toEpochDay(),
//                        0, true);
//                transactionDao.insert(transaction8);
//
//                byte validity = 2;
//
                ComingDao comingDao = INSTANCE.comingDao();
                Coming coming = new Coming(1, 1, false,
                        1670194800000L, 2022, 0, LocalDate.now().toEpochDay(), 0);
                Coming coming2 = new Coming(2, 2, false,
                        1670281200000L, 2022, 0, LocalDate.now().toEpochDay(), 0);
                Coming coming3 = new Coming(3, 3, false,
                        1667516400000L, 2022, 0, LocalDate.now().toEpochDay(), 0);
                Coming coming4 = new Coming(4, 4, false,
                        1667602800000L, 2022, 0, LocalDate.now().toEpochDay(), 0);
                Coming coming5 = new Coming(5, 5, false,
                        1664834400000L, 2022, 0, LocalDate.now().toEpochDay(), 0);
                Coming coming6 = new Coming(6, 6, false,
                        1664920800000L, 2022, 0, LocalDate.now().toEpochDay(), 0);
                comingDao.insert(coming);
                comingDao.insert(coming2);
                comingDao.insert(coming3);
                comingDao.insert(coming4);
                comingDao.insert(coming5);
                comingDao.insert(coming6);
//
//                byte element = 2;
//
//                Coming coming3 = new Coming(3, 3, element, false,
//                        1671577200000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming3);
//
//                Coming coming4 = new Coming(4, 4, validity, false,
//                        1646494800000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming4);
//
//                Coming coming5 = new Coming(5, 5, validity, false,
//                        1649169600000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming5);
//
//                Coming coming6 = new Coming(6, 6, validity, true,
//                        1671577200000L, 0, LocalDate.now().toEpochDay(), 1658008800000L);
//                comingDao.insert(coming6);
//
//                Coming coming7 = new Coming(7, 7, validity, false,
//                        1649169600000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming7);
//
//                Coming coming8 = new Coming(8, 8, validity, true,
//                        1649169600000L, 0, LocalDate.now().toEpochDay(), 1660687200000L);
//                comingDao.insert(coming8);
//
//
//                Coming coming11 = new Coming(11, 6, validity, false,
//                        1675378800000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming11);
//
//                Coming coming12 = new Coming(12, 7, validity, false,
//                        1672527600000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming12);
//
//                Coming coming13 = new Coming(13, 7, validity, false,
//                        1680559200000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming13);
//
//
//                Transaction repeat = new Transaction(10, 1,
//                        "Prezenty", "1.00", LocalDate.now().toEpochDay(),
//                        0, false);
//                transactionDao.insert(repeat);
//
//                Coming coming9 = new Coming(9, 10, validity, false,
//                        1662933600000L, 3232, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming9);
//
//                Coming coming10 = new Coming(10, 10, validity, false,
//                        1665525600000L, 0, LocalDate.now().toEpochDay(), 0);
//                comingDao.insert(coming10);
            });
        }
    };
}
