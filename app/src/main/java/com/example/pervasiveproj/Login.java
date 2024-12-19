package com.example.pervasiveproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin, btnRegister, btnForgotPassword;
    private FirebaseAuth firebaseAuth;
    private SQLiteHelper sqliteHelper;
    public static String mail = "";



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



        // Initialize Firebase Auth and SQLite Database
        firebaseAuth = FirebaseAuth.getInstance();
        sqliteHelper = new SQLiteHelper(this);

        // Load saved credentials if "Remember Me" was checked
        loadSavedCredentials();

        // Button Click Listeners
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> navigateToRegister());
        btnForgotPassword.setOnClickListener(v -> resetPassword());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedCredentials();
    }

    private void loadSavedCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");

            etEmail.setText(savedEmail);
            etPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();



        mail = email;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        UsersDatabase database = UsersDatabase.getInstance(this);
        if (isNetworkConnected()) {
            // Online login using Firebase
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                saveCredentialsIfRememberMe(email, password);
                                Login.mail = email;
                                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                navigateToHomePage();
                            }
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(this, "Login Failed: " + errorMessage, Toast.LENGTH_LONG).show();

                        }
                    });
        } else {
            // Check credentials in SQLite database
            boolean isValid = sqliteHelper.checkUserCredentials(email, password);
            UsersRoomDatabase roomDatabase = UsersRoomDatabase.getInstance(this) ;
            User user = roomDatabase.userDao().getUserByEmail(mail) ;
            if(user!=null)
            {
                if(user.getPassword().equals(password))
                {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    navigateToHomePage();
                }
                else
                {
                    Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                }
            }
//            if (isValid) {
//                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                navigateToHomePage();
//            } else {
//                Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
//            }
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private void saveCredentialsIfRememberMe(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (cbRememberMe.isChecked()) {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("rememberMe", true);
        } else {
            editor.clear(); // Remove credentials if "Remember Me" is unchecked
        }

        editor.apply();
    }


    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
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

    private void navigateToHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

}
