package com.example.pervasiveproj;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class UsersRoomDatabase extends RoomDatabase {

    // Singleton pattern to prevent multiple instances of the database
    private static UsersRoomDatabase INSTANCE;

    public abstract UserDao userDao();

    public static synchronized UsersRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UsersRoomDatabase.class, "app_database")
                    .fallbackToDestructiveMigration().allowMainThreadQueries() // To handle migrations
                    .build();
        }
        return INSTANCE;
    }
}
