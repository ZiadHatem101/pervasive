package com.example.pervasiveproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registeration extends AppCompatActivity {
    private EditText Name, Username, Email, Password, ConfirmPassword;
    private Button btnRegister, btnSelectBirthdate;
    private ImageView ivProfilePicture;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    private SQLiteDatabase localDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        // Initialize Views
        Name = findViewById(R.id.Name);
        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.signup);

        // Firebase Setup
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("users");

        // SQLite Setup
        localDatabase = openOrCreateDatabase("SmartControlDB", MODE_PRIVATE, null);
        localDatabase.execSQL("CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY, name TEXT, username TEXT, email TEXT);");

        // Register Button Click
        btnRegister.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String name = Name.getText().toString();
        String username = Username.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (password.equals(Password)) {
            // Save to Firebase
            String userId = usersRef.push().getKey();
            User user = new User(name, username, email);
            usersRef.child(userId).setValue(user);

            // Cache in SQLite
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("username", username);
            values.put("email", email);
            localDatabase.insert("Users", null, values);

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
    }
}
