package com.techwonders.doday.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.techwonders.doday.model.Category;
import com.techwonders.doday.model.Note;
import com.techwonders.doday.model.FoodExpense;
import com.techwonders.doday.model.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, Category.class, FoodExpense.class, Transaction.class}, version = 2)
public abstract class DoDayDatabase extends RoomDatabase {

    private static DoDayDatabase instance;
    private static String DB_NAME = "doDay_database";

    public abstract NoteDao noteDao();

    public abstract FoodExpenseDao foodExpenseDao();

    public abstract TransactionDao transactionDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized DoDayDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DoDayDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(dbCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more data, just add them.
                NoteDao dao = instance.noteDao();
                dao.deleteAllNotes();
                dao.insert(new Category("General"));
                dao.insert(new Category("Daily"));
                dao.insert(new Category("Work"));
            });
        }
    };

}
