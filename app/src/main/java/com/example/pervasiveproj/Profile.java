package com.example.pervasiveproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
public class Profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView usernameText;
    private Button logoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

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
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        usernameText.setText(Login.mail);

        logoutButton.setOnClickListener(v -> logoutUser());



        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Photos");


        if(isNetworkConnected()) {
            database.child("user" + Registeration.UserPhotosCount + "Photo").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String encodedPhoto = dataSnapshot.getValue().toString();
                        setProfileImage(encodedPhoto);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        else
        {
            UsersDatabase sqlDatabase = UsersDatabase.getInstance(this);
            String encodedPhoto = sqlDatabase.getUserPhoto(Login.mail);
            setProfileImage(encodedPhoto);
        }

//        firestore.collection("users")
//                .document(user.getUid())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            String encodedImage = document.getString("profilePicture");
//                            if (encodedImage != null && !encodedImage.isEmpty()) {
//                                setProfileImage(encodedImage);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        UsersDatabase database1 = UsersDatabase.getInstance(this) ;
//                        setProfileImage(database1.getUserPhoto(Login.mail));
//                    }
//                });

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


    private void setProfileImage(String encodedImage) {
        // Decode the Base64 encoded string to bytes
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

        // Convert the byte array to a Bitmap
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Set the Bitmap to the ImageView
        profileImage.setImageBitmap(decodedBitmap);
    }
}