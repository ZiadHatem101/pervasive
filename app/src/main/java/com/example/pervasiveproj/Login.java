package com.example.pervasiveproj;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;


public class Login extends AppCompatActivity {

    EditText etEmail ;
    private EditText etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin, btnRegister, btnForgotPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    public static String mail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Views
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Login Button
        btnLogin.setOnClickListener(v -> loginUser());

        // Register Button
        btnRegister.setOnClickListener(v -> navigateToRegister());

        // Forgot Password Button
        btnForgotPassword.setOnClickListener(v -> resetPassword());
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadSavedCredentials();
    }

    private void loadSavedCredentials() {
        // Access SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false); // Check if Remember Me was selected

        if (rememberMe) {
            // Retrieve saved email and password
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");

            // Auto-fill the fields
            etEmail.setText(savedEmail);
            etPassword.setText(savedPassword);

            // Check the "Remember Me" checkbox
            cbRememberMe.setChecked(true);
        }
    }


    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        UsersDatabase database = UsersDatabase.getInstance(this) ;

        if(isNetworkConnected()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // Save credentials if "Remember Me" is checked
                                if (cbRememberMe.isChecked()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", email);
                                    editor.putString("password", password); // Or use Firebase token
                                    editor.putBoolean("rememberMe", true); // Save the Remember Me state
                                    editor.apply();
                                }
                                mail = email;
                                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Intent main = new Intent(this, HomePage.class);
                                startActivity(main);
                            }
                        } else {
                            Toast.makeText(this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            if (database.checkCredentials(email, password)) {
                mail = email;
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent main = new Intent(this, HomePage.class);
                startActivity(main);
            } else {
                Toast.makeText(this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("users")
//                .whereEqualTo("email", email)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        boolean userFound = false;
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String storedPassword = document.getString("password");
//                            if (storedPassword != null && storedPassword.equals(password)) {
//                                userFound = true;
//                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(this, HomePage.class);
//                                startActivity(intent);
//
//                                break;
//                            }
//                        }
//
//                        if (!userFound) {
//                            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(this, "Error connecting to database: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }


    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void navigateToRegister() {
        Intent intent = new Intent(this, Registeration.class);
        startActivity(intent);
    }

//    private void navigateToMain() {
//        Intent intent = new Intent(this, MainActivity.class); // Replace with your main activity
//        startActivity(intent);
//        finish(); // Close the login activity
//    }
}

