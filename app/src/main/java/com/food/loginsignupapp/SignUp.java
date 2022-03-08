package com.food.loginsignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    EditText fullNameSgn, ageSgn, emailSgn, passwordSgn;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullNameSgn = findViewById(R.id.nameSgnView);
        ageSgn = findViewById(R.id.ageSgnView);
        emailSgn = findViewById(R.id.emailLgnView);
        passwordSgn = findViewById(R.id.passwordLgnView);
        signUp = findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, WelcomeToTheApp.class);
                startActivity(intent);
                finish();
            }
        });

    }
}