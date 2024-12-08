//package com.example.pervasiveproj;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class FanActivity extends AppCompatActivity {
//    private Button btnFanTurnOff;
//    private Button btnFanTurnOn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fan_layout);
//
//        // ربط الزرارين بالكود
//        btnFanTurnOn = findViewById(R.id.btn_fan_turn_on);
//        btnFanTurnOff = findViewById(R.id.btn_fan_turn_off);
//
//        // إضافة الأحداث عند الضغط
//        btnFanTurnOn.setOnClickListener(v ->
//                Toast.makeText(this, "Fan Turned On", Toast.LENGTH_SHORT).show()
//        );
//
//        btnFanTurnOff.setOnClickListener(v ->
//                        Toast.makeText(this, "Fan Turned Off", Toast.LENGTH_SHORT).show());
//    }
//}


package com.example.pervasiveproj;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fan_layout);

        // ربط الزرارين بالكود
        Button btnFanTurnOn = findViewById(R.id.btn_fan_turn_on);
        Button btnFanTurnOff = findViewById(R.id.btn_fan_turn_off);

        // الاتصال بـ Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fanRef = database.getReference("Fan/status");

        // تشغيل المروحة
        btnFanTurnOn.setOnClickListener(v -> {
            fanRef.setValue("on").addOnSuccessListener(aVoid ->
                    Toast.makeText(this, "Fan Turned On", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            );
        });

        long currentTimestamp = System.currentTimeMillis();
        ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "Toggled Fan : On"));

        // إطفاء المروحة
        btnFanTurnOff.setOnClickListener(v -> {
            fanRef.setValue("off").addOnSuccessListener(aVoid ->
                    Toast.makeText(this, "Fan Turned Off", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            );
        });

        ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "Toggled Fan : Off"));

    }
}