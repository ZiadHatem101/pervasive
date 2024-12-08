package com.example.pervasiveproj;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GarageDoorActivity extends AppCompatActivity {


    private Button btnGarageOpen;
    private Button btnGarageClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_layout);

        // ربط الزرارين بالكود
        btnGarageOpen = findViewById(R.id.btn_garage_open);
        btnGarageClose = findViewById(R.id.btn_garage_close);

        // الاتصال بـ Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference garageRef = database.getReference("GarageDoor/status");

        // فتح الجراج
        btnGarageOpen.setOnClickListener(v -> {
            garageRef.setValue("open").addOnSuccessListener(aVoid ->
                    Toast.makeText(this, "Garage Door Opened", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            );
        });

        long currentTimestamp = System.currentTimeMillis();
        ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "Opened the garage door"));

        // إغلاق الجراج
        btnGarageClose.setOnClickListener(v -> {
            garageRef.setValue("closed").addOnSuccessListener(aVoid ->
                    Toast.makeText(this, "Garage Door Closed", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            );
        });

        ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "Closed the garage door"));
    }
}