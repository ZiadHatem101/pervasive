package com.example.pervasiveproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";

    private static UsersDatabase instance;

    // Singleton Instance
    public static synchronized UsersDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new UsersDatabase(context.getApplicationContext());
        }
        return instance;
    }

    // Constructor
    private UsersDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Fixed SQL Syntax
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                "name TEXT, " +
                "user_name TEXT, " +
                "mail TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "photo blob, " +
                "birth_date TEXT" +
                ")";
        database.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If schema changes, drop and recreate the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Insert User
    public boolean insertUser(String name, String user_name, String mail, String password, byte[] photo, String birth_date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("user_name", user_name);
        contentValues.put("mail", mail);
        contentValues.put("password", password);
        contentValues.put("photo", photo);
        contentValues.put("birth_date", birth_date);

        // Perform insertion and check result
        long result = database.insert(TABLE_USERS, null, contentValues);
        return result != -1;  // Returns true if insert was successful
    }

    // Check User Credentials
    public boolean checkCredentials(String mail, String password) {
        SQLiteDatabase database = this.getReadableDatabase();
        boolean result = false;

        String query = "SELECT * FROM users WHERE mail = ? AND password = ?";
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, new String[]{mail, password});

            if (cursor != null && cursor.moveToFirst()) {
                result = true; // Credentials match
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }

        return result;
    }


    // Retrieve User Photo
    public byte[] getUserPhoto(String mail) {
        SQLiteDatabase database = this.getReadableDatabase();
        byte[] photo = null;

        String query = "SELECT photo FROM users WHERE mail = ?";
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, new String[]{mail});
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("photo");
                if (columnIndex != -1) {
                    photo = cursor.getBlob(columnIndex); // Safely retrieve the photo value
                } else {
                    throw new IllegalStateException("Column 'photo' does not exist in the database schema.");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure the cursor is closed
            }
        }

        return photo; // Return the Base64-encoded photo string (or null if not found)
    }


}
