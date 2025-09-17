package com.example.chemapp30.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Chemical.class, DangerousMix.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChemicalDao chemicalDao();
    public abstract DangerousMixDao dangerousMixDao();

    private static volatile AppDatabase INSTANCE;

    // เธรดพูลสำหรับงาน I/O ของ Room
    private static final int NUMBER_OF_THREADS = 2;
    public static final ExecutorService DB_IO = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "chemapp.db"
                            )
                            // ห้ามบล็อก Main Thread
                            // .allowMainThreadQueries()  // ❌ เอาออก
                            .setQueryExecutor(DB_IO)
                            .setTransactionExecutor(DB_IO)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
