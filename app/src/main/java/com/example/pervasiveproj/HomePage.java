package com.example.pervasiveproj;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private ListView actionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        TextView search_query = findViewById(R.id.textView);


        actionsListView = findViewById(R.id.actionsListView);
        ArrayList<Item> actionsList = new ArrayList<>();

        Item temperature = new Item("Temperature", "See the home temperature right now !!", R.drawable.temperature);
        Item light = new Item("Lights", "Turn on/off lights.", R.drawable.lamp);
        Item fan = new Item("Fan", "Turn on/off the fan.", R.drawable.fan);
        Item door = new Item("Garage Door", "Open/Close garage door.", R.drawable.garage);

        actionsList.add(temperature);
        actionsList.add(light);
        actionsList.add(fan);
        actionsList.add(door);

        ActionsListAdapter actionsListAdapter = new ActionsListAdapter(this, actionsList);

        actionsListView.setAdapter(actionsListAdapter);

        String query = search_query.getText().toString();
        if (query.toLowerCase().equals(temperature.getName())) {

        } else if (query.toLowerCase().equals(fan.getName())) {

        } else if (query.toLowerCase().equals(light.getName())) {

        } else if (query.toLowerCase().equals(door.getName())) {

        }
        else
        {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
    }
}
