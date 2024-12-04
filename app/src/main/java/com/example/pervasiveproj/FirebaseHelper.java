package com.example.pervasiveproj;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference logRef;

    public FirebaseHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        logRef = firebaseDatabase.getReference("activity_logs");  // Node in Firebase Realtime Database
    }

    // Store log in Firebase
    public void storeLog(String action, String timestamp) {
        String logId = logRef.push().getKey();  // Generate unique log ID
        if (logId != null) {
            Log log = new Log(action, timestamp);
            logRef.child(logId).setValue(log);
        }
    }

    // Log model class for Firebase
    public static class Log {
        public String action;
        public String timestamp;

        public Log(String action, String timestamp) {
            this.action = action;
            this.timestamp = timestamp;
        }
    }
}
