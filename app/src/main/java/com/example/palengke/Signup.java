package com.example.palengke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.palengke.classes.Users;
import com.example.palengke.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    TextView haveAccTxt;
    EditText fName, email, password, confPassword;
    Button createAccBtn;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        haveAccTxt = findViewById(R.id.textViewLoginLink);
        fName = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confPassword = findViewById(R.id.editTextConfirmPassword);
        createAccBtn = findViewById(R.id.buttonSignUp);

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = email.getText().toString().trim();
                String userPass = password.getText().toString().trim();
                String userPassConf = confPassword.getText().toString().trim();
                String userFName = fName.getText().toString().trim();

                if(userFName.isEmpty()) {
                    fName.setError("This field is required");
                } else if (user.isEmpty()){
                    email.setError("Email cannot be empty");
                } else if(userPass.isEmpty() || userPassConf.isEmpty()){
                    password.setError("Password cannot be empty");
                    confPassword.setError("Password cannot be empty");
                } else if(!user.contains("@")){
                    email.setError("Please enter a valid email address");
                } else if(!userPass.equals(userPassConf)) {
                    confPassword.setError("Passwords do not matched");
                } else{
                    auth.createUserWithEmailAndPassword(user, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        if (firebaseUser != null) {
                                            // User created successfully
                                            String uid = firebaseUser.getUid();
                                            String userName = userFName;
                                            String formattedDate = new Utils().setDate(System.currentTimeMillis());
                                            DatabaseReference userRef = firebaseDatabase.getReference("users").child(uid);

                                            // Set the user's display name
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(userName)
                                                    .build();

                                            // Update the user's profile
                                            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                                                if (profileTask.isSuccessful()) {
                                                    Log.d("SignUpPage", "Display name updated successfully");

                                                    // Add the new user to Realtime Database
                                                    userRef.setValue(new Users(uid, userName, user, formattedDate))
                                                            .addOnSuccessListener(aVoid -> {
                                                                Log.d("SignUpPage", "User added to Realtime Database");
                                                                Toast.makeText(Signup.this, "Account created successfully", Toast.LENGTH_LONG).show();
                                                                finish();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.d("SignUpPage", "Error adding user to Realtime Database: " + e.getMessage());
                                                                Toast.makeText(Signup.this, "Error adding user to Realtime Database", Toast.LENGTH_SHORT).show();
                                                            });
                                                } else {
                                                    Log.d("SignUpPage", "Error updating display name: " + profileTask.getException().getMessage());
                                                    Toast.makeText(Signup.this, "Error updating display name", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Log.d("SignUpPage", "Error: Firebase user is null");
                                            Toast.makeText(Signup.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Authentication failed - get the specific error
                                        Exception exception = task.getException();
                                        if (exception != null) {
                                            Log.e("SignUpPage", "Authentication failed: " + exception.getMessage());
                                            Toast.makeText(Signup.this, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        haveAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent goBackToLoginPage = new Intent(Signup.this, Signup.class);
                startActivity(goBackToLoginPage);
            }
        });
    }
}