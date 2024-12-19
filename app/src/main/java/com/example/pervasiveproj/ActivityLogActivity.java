package com.example.pervasiveproj;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class ActivityLogActivity extends AppCompatActivity {

    public ListView logListView;
    public static ArrayList<LogActivityItem> logItems = new ArrayList<>(); // Static ArrayList to store log items



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logListView = findViewById(R.id.logListView); // Find the ListView in the layout

        ActivityLogDatabase activityDatabase ;
        activityDatabase = ActivityLogDatabase.getInstance(this) ;

        activityDatabase.getAllTheLogs();

        ActivityListAdapter adapter = new ActivityListAdapter(this, logItems);
        logListView.setAdapter(adapter);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference logRef = database.getReference("Log"); // Reference to "Log" node in the database


//        // Loop through logItems and add them to the database
//        for (LogActivityItem logItem : logItems) {
//            // Create a unique key for each log item using push() (this will avoid overwriting previous logs)
//            String logId = logRef.push().getKey();
//
//            if (logId != null) {
//                // Set the log item in Firebase at the unique logId path
//                logRef.child(logId).setValue(logItem)
//                        .addOnSuccessListener(aVoid -> {
//                            // Notify that the data was successfully stored in Firebase
//                            Toast.makeText(ActivityLogActivity.this, "Log saved successfully", Toast.LENGTH_SHORT).show();
//                            String message = logItem.getText();
//                            String timestamp = convertTimestampToString(logItem.getTimestamp());
//                            //activityDatabase.insertLog(message, );
//
//                        })
//                        .addOnFailureListener(e -> {
//                            // Handle failure in case of any issues with Firebase
//                            Toast.makeText(ActivityLogActivity.this, "Failed to save log", Toast.LENGTH_SHORT).show();
//                        });
            }



    public static String convertTimestampToString(long timestamp) {
        // Define the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        // Create a Date object from the timestamp
        Date date = new Date(timestamp);
        // Format the Date object into a readable string
        return sdf.format(date);
    }
}