package com.food.loginsignupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    TextView signUp, name, age, email, password;
    protected FirebaseAuth mAuth;
    protected DatabaseReference dRef;
    FirebaseDatabase database;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Write a message to the database
        dRef = FirebaseDatabase.getInstance().getReference("Users");

        // Fetching values to save in database
        name = findViewById(R.id.nameView);
        age = findViewById(R.id.ageView);
        email = findViewById(R.id.emailView);
        password = findViewById(R.id.passwordView);


        // getting sign up button an setting on click listener on it
        signUp = findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(view -> {

            //checking the password
            if (TextUtils.isEmpty(password.getText().toString())) {
                Toast.makeText(this, "Empty Password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignUpActivity", "createUserWithEmail:success");
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                if (currentUser != null) {
                                    databaseCreation();
                                    startActivity(new Intent(SignUp.this, Login.class));
                                    finish();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });
    }

    private void databaseCreation() {
        user = new User(name.getText().toString(),age.getText().toString(),email.getText().toString(),password.getText().toString());
        database = FirebaseDatabase.getInstance();
        dRef = database.getReference("User");
        dRef.setValue(user);
        Toast.makeText(SignUp.this, "Databse Created!", Toast.LENGTH_SHORT).show();
    }
}