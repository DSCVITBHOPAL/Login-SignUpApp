package com.food.loginsignupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText emailLgn, passwordLgn;
    TextView newUser, lgnBtn;
    CardView googleLgn, facebookLgn, githubLgn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // This will remove the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        emailLgn = findViewById(R.id.emailLgnView);
        passwordLgn = findViewById(R.id.passwordLgnView);
        lgnBtn = findViewById(R.id.loginBtn);
        googleLgn = findViewById(R.id.loginWithGoogleBtn);
        facebookLgn = findViewById(R.id.loginWithFacebookBtn);
        githubLgn = findViewById(R.id.loginWithGitHubBtn);
        newUser = findViewById(R.id.newUserRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // GitHub authentication
        githubLgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLgn.getText().toString();
                if (email.isEmpty()) {
                    emailLgn.setError("Invalid Email");
                    emailLgn.requestFocus();
                }
                else {
                    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                    provider.addCustomParameter("login", email);

                    List<String> scopes =
                            new ArrayList<String>() {
                                {
                                    add("user:email");
                                }
                            };
                    provider.setScopes(scopes);

                    Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
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
                        firebaseAuth
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