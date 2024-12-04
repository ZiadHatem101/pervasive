package com.example.pervasiveproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
public class profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView usernameText;
    private Button logoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize Views
        profileImage = findViewById(R.id.profile_image);
        usernameText = findViewById(R.id.username_text);
        logoutButton = findViewById(R.id.logout_button);

        // Get the current user from Firebase Authentication
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            // Fetch user profile information from Firestore
            String userId = user.getUid();
            firestore.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                String profilePictureUrl = document.getString("profilePictureUrl");

                                // Set the username and profile picture
                                usernameText.setText(username);
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Picasso.get().load(profilePictureUrl).into(profileImage); // Load profile picture
                                }
                            }
                        } else {
                            Toast.makeText(profile.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Logout button functionality
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        // Clear saved credentials in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear saved login data
        editor.apply();

        // Log out from Firebase
        firebaseAuth.signOut();

        // Navigate back to the login screen
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}