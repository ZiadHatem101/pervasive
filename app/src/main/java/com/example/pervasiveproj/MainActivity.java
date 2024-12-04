package com.example.pervasiveproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView actionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionsListView = findViewById(R.id.actionsListView);

        // List of actions
        String[] actions = {
                "View Profile",
                "Signin",
                "Signup",
        };

        // Adapter to populate ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, actions);
        actionsListView.setAdapter(adapter);

        // Set item click listener for ListView
        actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                // Handle action selection
                String selectedAction = actions[position];

                switch (selectedAction) {
                    case "View Profile":
                        // Navigate to Profile Activity
                        Toast.makeText(MainActivity.this, "Navigating to Profile", Toast.LENGTH_SHORT).show();
                        // Start profile activity here
                        Intent intent = new Intent(MainActivity.this, profile.class);
                        startActivity(intent);
                        break;
                    case "Signin":
                        // Navigate to Settings Activity
                        Toast.makeText(MainActivity.this, "Opening Settings", Toast.LENGTH_SHORT).show();
                        // Start settings activity here
                        Intent intent1 = new Intent(MainActivity.this, Login.class);
                        startActivity(intent1);
                        break;
                    case "Signup":
                        // Handle logout logic
                        Toast.makeText(MainActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                        // Perform logout here
                        Intent intent2 = new Intent(MainActivity.this, Registeration.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
