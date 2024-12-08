package com.example.pervasiveproj;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LightsActivity extends AppCompatActivity {
    private Button btnLightTurnOn ;
    private Button btnLightTurnOff ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lights_layout);

        // ربط الزرارين بالكود
        btnLightTurnOn = findViewById(R.id.btn_turn_on);
        btnLightTurnOff = findViewById(R.id.btn_turn_off);

        // الاتصال بـ Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference lightRef = database.getReference("Leds/status");

        // تشغيل الإضاءة
        btnLightTurnOn.setOnClickListener(v -> {
            lightRef.setValue("on").addOnSuccessListener(aVoid ->
                    Toast.makeText(this, "Light Turned On", Toast.LENGTH_SHORT).show()

            ).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            );
        });

        long currentTimestamp = System.currentTimeMillis();
        ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "Toggled Light : On"));

        // إطفاء الإضاءة
        btnLightTurnOff.setOnClickListener(v -> {
            lightRef.setValue("off").addOnSuccessListener(aVoid ->
                    Toast.makeText(this, "Light Turned Off", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            );
        });
        ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "Toggled Light : Off"));
    }
}