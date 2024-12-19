package com.example.pervasiveproj;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MotionDetection extends AppCompatActivity {

    private static DatabaseReference database;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Lifecycle", "onStart called");
        readData();
    }

    public static void readData() {

        Log.d("Database", "readData called");

        database = FirebaseDatabase.getInstance().getReference();

        database.child("Ultrasonic/status").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String status = dataSnapshot.getValue(String.class);
                if ("on".equalsIgnoreCase(status)) {
                    updateDeviceStatus("Fan/status", "on");
                    updateDeviceStatus("Leds/status", "on");
                }
            } else {
                updateDeviceStatus("Fan/status", "off");
                updateDeviceStatus("Leds/status", "off");
            }
        }).addOnFailureListener(e -> {
            // Toast.makeText(this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private static void updateDeviceStatus(String path, String value) {
        database.child(path).setValue(value);
    }
}
