package com.example.pervasiveproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "activity_log.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_LOGS = "logs";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACTION = "action";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL to create logs table
        String CREATE_LOGS_TABLE = "CREATE TABLE " + TABLE_LOGS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ACTION + "TEXT NOT NULL, "
                + COLUMN_TIMESTAMP + " TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_LOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table and recreate it if the schema changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    }

    // Insert a log action into the database
    public void insertLog(String action, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTION, action);
        values.put(COLUMN_TIMESTAMP, timestamp);

        db.insert(TABLE_LOGS, null, values);
        db.close();
    }

    // Retrieve all logs from the database
    public Cursor getAllLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LOGS, new String[]{COLUMN_ID, COLUMN_ACTION, COLUMN_TIMESTAMP},
                null, null, null, null, COLUMN_TIMESTAMP + " DESC");
    }
}
