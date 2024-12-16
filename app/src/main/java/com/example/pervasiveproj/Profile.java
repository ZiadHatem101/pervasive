package com.example.pervasiveproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView usernameText;
    private Button logoutButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        profileImage = findViewById(R.id.profile_image);
        usernameText = findViewById(R.id.username_text);
        logoutButton = findViewById(R.id.logout_button);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Photos");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            usernameText.setText(Login.mail);
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        }

        // Logout Button
        logoutButton.setOnClickListener(v -> logoutUser());

        // Fetch Profile Image
        fetchProfileImage();
    }

    private void fetchProfileImage() {
        if (isNetworkConnected()) {
            // Fetch profile image from Firebase Realtime Database
            firebaseDatabaseReference.child("user" + Registeration.UserPhotosCount + "Photo")
                    .get()
                    .addOnSuccessListener(this::handleProfileImageFetchSuccess)
                    .addOnFailureListener(this::handleProfileImageFetchFailure);
        } else {
            // Fetch profile image from local SQLite database
            UsersDatabase sqlDatabase = UsersDatabase.getInstance(this);
            String encodedPhoto = sqlDatabase.getUserPhoto(Login.mail);
            if (encodedPhoto != null) {
                setProfileImage(encodedPhoto);
            } else {
                Toast.makeText(this, "No offline profile image found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleProfileImageFetchSuccess(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            String encodedPhoto = dataSnapshot.getValue(String.class);
            if (encodedPhoto != null) {
                setProfileImage(encodedPhoto);
            } else {
                Toast.makeText(this, "No profile image found in the database!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No profile image found in the database!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleProfileImageFetchFailure(@NonNull Exception e) {
        Toast.makeText(this, "Failed to fetch profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setProfileImage(String encodedImage) {
        try {
            // Decode the Base64 encoded string to bytes
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

            // Convert the byte array to a Bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Set the Bitmap to the ImageView
            profileImage.setImageBitmap(decodedBitmap);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error decoding profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        // Clear saved credentials in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear saved login data
        editor.apply();

        if (isNetworkConnected()) {
            firebaseAuth.signOut();
        }

        // Navigate back to the login screen
        navigateToLogin();
    }

    private void navigateToLogin() {
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
