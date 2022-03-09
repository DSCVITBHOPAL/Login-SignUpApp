package com.food.loginsignupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.food.loginsignupapp.authentications.FacebookAuthActivity;
import com.food.loginsignupapp.authentications.GoogleAuthActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Login extends AppCompatActivity {

    protected TextView newUser, login;
    protected CardView google, facebook, github;
    protected EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) startActivity(new Intent(Login.this, WelcomeToTheApp.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // This will remove the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Finding Login Button and setting up onclick listener to it
        login = findViewById(R.id.loginBtn);

        // getting email and password
        email = findViewById(R.id.emailLgnView);
        password = findViewById(R.id.passwordLgnView);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LoginActivity", "signInWithEmail:success");
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if(currentUser!=null) startActivity(new Intent(Login.this, WelcomeToTheApp.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Finding Facebook Icon and setting up click listener to it
        facebook = findViewById(R.id.loginWithFacebookBtn);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching to Google Authentication Activity
                startActivity(new Intent(Login.this, FacebookAuthActivity.class));
            }
        });

        // Finding Google Icon and setting up click listener to it
        google = findViewById(R.id.loginWithGoogleBtn);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching to Google Authentication Activity
                startActivity(new Intent(Login.this, GoogleAuthActivity.class));
            }
        });

        // Finding new user text and setting up click listener to it

        newUser = findViewById(R.id.newUserRegister);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching to Signup Activity
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        // GitHub authentication
        github = findViewById(R.id.loginWithGitHubBtn);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                    provider.addCustomParameter("login", email.getText().toString());

                    List<String> scopes =
                            new ArrayList<String>() {
                                {
                                    add("user:email");
                                }
                            };
                    provider.setScopes(scopes);

                    Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
                    if (pendingResultTask != null) {
                        // There's something already here! Finish the sign-in for your user.
                        pendingResultTask
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                // User is signed in.
                                                // IdP data available in
                                                // authResult.getAdditionalUserInfo().getProfile().
                                                // The OAuth access token can also be retrieved:
                                                // authResult.getCredential().getAccessToken().
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Login.this, "Authentication Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    } else {
                        mAuth
                                .startActivityForSignInWithProvider(/* activity= */ Login.this, provider.build())
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                Intent intent = new Intent(Login.this, WelcomeToTheApp.class);
                                                Toast.makeText(Login.this, "Login with GitHub successful", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Login.this, "Error occurred!! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    }

                }
            }
        });

    }
}