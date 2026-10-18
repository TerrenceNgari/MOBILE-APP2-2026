package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView loginButton = findViewById(R.id.textButtonLogin);
        TextView signupButton = findViewById(R.id.textButtonSignup);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(StartPage.this, Login.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(StartPage.this, SignUp.class);
            startActivity(intent);
        });
    }
}
