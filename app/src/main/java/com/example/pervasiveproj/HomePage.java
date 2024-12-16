package com.example.pervasiveproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private ListView actionsListView;
    private SearchView searchView;
    private FirebaseAuth firebaseAuth;

    ArrayList<Item> actionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        MotionDetection.readData();

        actionsListView = findViewById(R.id.actionsListView);

        // Create action items
        Item temperature = new Item("Temperature", "Display home temperature.", R.drawable.temperature);
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

        actionsListView.setOnItemClickListener((parent, view, position, id) -> {
          // Check network connectivity
        if (!isNetworkConnected()) {
               Toast.makeText(this, "Please check your network connection!", Toast.LENGTH_SHORT).show();
              return;
            }

            // Retrieve the clicked item
            Item clickedItem = actionsList.get(position);

            // Use an intent to navigate to the corresponding activity
            Intent intent;
            switch (clickedItem.getName()) {
                case "Temperature":
                    intent = new Intent(HomePage.this, TemperatureActivity.class);
                    break;
                case "Lights":
                    intent = new Intent(HomePage.this, LightsActivity.class);
                    break;
                case "Fan":
                    intent = new Intent(HomePage.this, FanActivity.class);
                    break;
                case "Garage Door":
                    intent = new Intent(HomePage.this, GarageDoorActivity.class);
                    break;
                default:
                    intent = null;
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        });

        // Search functionality
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String newText) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : actionsList) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            ActionsListAdapter adapter = new ActionsListAdapter(this, filteredList);
            actionsListView.setAdapter(adapter);
        }
    }

    // Inflate the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);  // Link to the options_menu.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_activity_log) {
            // Navigate to Activity Log
            Intent activityLogIntent = new Intent(this, ActivityLogActivity.class);
            startActivity(activityLogIntent);
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            // Navigate to Profile
            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        if (isNetworkConnected()) {
            firebaseAuth.signOut();
        }

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
