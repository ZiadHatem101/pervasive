package com.example.pervasiveproj;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registeration extends AppCompatActivity {

    private EditText etName, etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnUploadPicture, btnSelectBirthdate, btnRegister;
    private Uri profileImageUri;
    private String birthdate;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        // Initialize Views
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUploadPicture = findViewById(R.id.btnUploadPicture);
        btnSelectBirthdate = findViewById(R.id.btnSelectBirthdate);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // Upload Picture Button
        btnUploadPicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Select Birthdate Button
        btnSelectBirthdate.setOnClickListener(v -> showDatePicker());

        // Register Button
        btnRegister.setOnClickListener(v -> registerUser());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.getData();
            Toast.makeText(this, "Profile picture selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    birthdate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    Toast.makeText(Registeration.this, "Birthdate: " + birthdate, Toast.LENGTH_SHORT).show();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (profileImageUri == null) {
            Toast.makeText(this, "Please upload a profile picture!", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadProfilePictureAndSaveUser(name, username, email, password);
    }

    private void uploadProfilePictureAndSaveUser(String name, String username, String email, String password) {
        StorageReference storageReference = firebaseStorage.getReference("profile_pictures/" + username + ".jpg");
        storageReference.putFile(profileImageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                            if (uriTask.isSuccessful()) {
                                String profilePictureUrl = uriTask.getResult().toString();
                                saveUserToFirestore(name, username, email, password, profilePictureUrl);
                            }
                        });
                    } else {
                        Toast.makeText(this, "Failed to upload profile picture!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String name, String username, String email, String password, String profilePictureUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);
        user.put("profilePicture", profilePictureUrl);
        user.put("birthdate", birthdate);

        firestore.collection("users").document(username)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to register user!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
