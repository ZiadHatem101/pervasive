package com.example.pervasiveproj;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogActivity extends AppCompatActivity {

    private ListView logListView;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logListView = findViewById(R.id.logListView);
        dbHelper = new SQLiteHelper(this);

        // Fetch logs from SQLite (cached logs in case of no internet)
        Cursor cursor = dbHelper.getAllLogs();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"action", "timestamp"},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
        logListView.setAdapter(adapter);
    }
}

