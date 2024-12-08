package com.example.pervasiveproj;

import static com.example.pervasiveproj.R.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TemperatureActivity extends AppCompatActivity {

    private FirebaseDatabase database ;
    private TextView textView ;
    private Button button ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature);

        textView = findViewById(id.tempTextView);
        button = findViewById(R.id.tempButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });


    }

    private void readData() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("TemperatureSensor");

        database.child("temperature").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String temp = dataSnapshot.getValue().toString();
                    Toast.makeText(TemperatureActivity.this, "Successfully Read the temperature.", Toast.LENGTH_SHORT).show();
                    textView.setText(temp);

                    long currentTimestamp = System.currentTimeMillis();
                    ActivityLogActivity.logItems.add(new LogActivityItem(currentTimestamp, "The Temperature now is : " + temp));


                } else {
                    Toast.makeText(TemperatureActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TemperatureActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
