package com.example.pervasiveproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityLogDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "activity_log_database.db";
    private static final int DATABASE_VERSION = 1;

    public static ActivityLogDatabase instance;

    public ActivityLogDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ActivityLogDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityLogDatabase(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onCreate(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE activity_log (message TEXT, timestamp TEXT)");

    }

    public void insertLog(String message, long timestamp) {
        String timestampString = ActivityLogActivity.convertTimestampToString(timestamp);


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("message", message);
        contentValues.put("timestamp", timestampString);

        database.insert("activity_log", null, contentValues);
        database.close();
    }

    public void getAllTheLogs() {
        SQLiteDatabase database = this.getReadableDatabase();


        String query = "select * from activity_log ";
        Cursor cursor = database.rawQuery(query,null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String timestamp = cursor.getString(1);
                String message = cursor.getString(0);

                LogActivityItem logItem = new LogActivityItem(convertStringToTimestamp(timestamp), message);

                ActivityLogActivity.logItems.add(logItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

    }

    public static long convertStringToTimestamp(String dateString) {
        // Define the date format corresponding to the string format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // Example format

        try {

            Date date = dateFormat.parse(dateString);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
