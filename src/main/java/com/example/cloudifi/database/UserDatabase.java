package com.example.cloudifi.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cloudifi.DAO.UserDAO;
import com.example.cloudifi.entitiies.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = UserEntity.class, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDAO userDAO();

    private static volatile UserDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static UserDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "user_database")
                    .setJournalMode(JournalMode.TRUNCATE).build();
                }
            }
        }
        return INSTANCE;
    }

}
