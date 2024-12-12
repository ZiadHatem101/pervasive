package com.example.pervasiveproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class UsersDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static UsersDatabase instance;

    public UsersDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static synchronized UsersDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new UsersDatabase(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("CREATE TABLE users (TEXT name , TEXT user_name , TEXT mail PRIMARY KEY , TEXT password , TEXT photo , TEXT birth_date )");

    }
    public void insertUser(String name , String user_name , String mail , String password , String photo , String birth_date)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name" , name);
        contentValues.put("user_name" , user_name) ;
        contentValues.put("mail" , mail) ;
        contentValues.put("password" , password) ;
        contentValues.put("photo" , photo) ;
        contentValues.put("birth_date" , birth_date) ;

        database.insert("users" , null , contentValues) ;
        database.close();
    }

    public boolean checkCredentials(String mail , String password)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        String query = "select mail , password from users where mail = ? AND password = ?";
        Cursor cursor = database.rawQuery(query, new String[]{mail, password});

        if(cursor != null && cursor.getCount() > 0)
        {
            return true ;
        }
        else
        {
            return false ;
        }

    }

    public String getUserPhoto(String mail) {
        SQLiteDatabase database = this.getReadableDatabase();
        String photo = null;

        // Query to get the user's photo (encoded image) based on email
        String query = "SELECT photo FROM users WHERE mail = ?";
        Cursor cursor = database.rawQuery(query, new String[]{mail});

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the encoded photo string from the cursor
            photo = cursor.getString(4);
        }

        cursor.close();
        database.close();

        return photo;  // Return the Base64-encoded photo string (or null if not found)
    }

}
