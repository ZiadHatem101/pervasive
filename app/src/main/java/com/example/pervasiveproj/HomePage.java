package com.example.pervasiveproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private ListView actionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TextView search_query = findViewById(R.id.textView);

        actionsListView = findViewById(R.id.actionsListView);
        ArrayList<Item> actionsList = new ArrayList<>();

        // Create action items
        Item temperature = new Item("Temperature", "See the home temperature right now !!", R.drawable.temperature);
        Item light = new Item("Lights", "Turn on/off lights.", R.drawable.lamp);
        Item fan = new Item("Fan", "Turn on/off the fan.", R.drawable.fan);
        Item door = new Item("Garage Door", "Open/Close garage door.", R.drawable.garage);

        actionsList.add(temperature);
        actionsList.add(light);
        actionsList.add(fan);
        actionsList.add(door);

        // Setup the adapter for the ListView
        ActionsListAdapter actionsListAdapter = new ActionsListAdapter(this, actionsList);
        actionsListView.setAdapter(actionsListAdapter);

        // Check the search query for matching items
        String query = search_query.getText().toString();
        if (query.toLowerCase().equals(temperature.getName().toLowerCase())) {
            // Handle temperature action
        } else if (query.toLowerCase().equals(fan.getName().toLowerCase())) {
            // Handle fan action
        } else if (query.toLowerCase().equals(light.getName().toLowerCase())) {
            // Handle light action
        } else if (query.toLowerCase().equals(door.getName().toLowerCase())) {
            // Handle door action
        } else {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    // Inflate the options menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.options_menu, menu);  // Link to the options_menu.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection using if-else instead of switch
        if (item.getItemId() == R.id.action_activity_log) {
            // Navigate to Activity Log
            Intent activityLogIntent = new Intent(this, ActivityLogActivity.class);
            startActivity(activityLogIntent);
            return true;

        } else if (item.getItemId() == R.id.action_profile) {
            // Navigate to Profile
            Intent profileIntent = new Intent(this, profile.class);
            startActivity(profileIntent);
            return true;

        } else if (item.getItemId() == R.id.action_logout) {
            // Log out logic
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(this, Login.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);  // Default case, return super behavior
    }
}

