package com.example.pervasiveproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UsersDB";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "photo BLOB)";
        db.execSQL(createTable);
    }

    private static SQLiteHelper instance;

    // Singleton Instance
    public static synchronized SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String password , byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public byte[] getUserPhoto(String mail) {
        SQLiteDatabase database = this.getReadableDatabase();
        byte[] photo = null;

        String query = "SELECT photo FROM users WHERE email = ?";
        Cursor cursor = null;

        try {
            Log.d("Ali", mail);
            cursor = database.rawQuery(query, new String[]{mail});
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("photo");
                if (columnIndex != -1) {
                    photo = cursor.getBlob(columnIndex); // Safely retrieve the photo value
                } else {
                    Log.d("Ali" , mail) ;
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
    public void getAllUserPhotos() {
        List<byte[]> photoList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        // SQL query to select all photos
        String query = "SELECT photo FROM users";
        Cursor cursor = null;

        try {
            // Execute the query
            cursor = database.rawQuery(query, null);

            // Loop through the results and add each photo to the list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int columnIndex = cursor.getColumnIndex("photo");
                    if (columnIndex != -1) {
                        byte[] photo = cursor.getBlob(columnIndex); // Get the photo as a byte array
                        Log.d("Ziad" , photo.toString() ) ; // Add the photo to the list
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure the cursor is closed after usage
            }
        }

    }

}

