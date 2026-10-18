package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase Authentication: Initialization
        auth = FirebaseAuth.getInstance(); //Allow access to firebase Authentication services

        EditText emailInput = findViewById(R.id.editTextEmailLogin);

        EditText passwordInput = findViewById(R.id.editTextPasswordLogin);

        TextView loginButton = findViewById(R.id.textButtonLoginSubmit);

        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()){
                if (email.isEmpty()){
                    emailInput.setError("Email is empty.");
                }
                if (password.isEmpty()){
                    passwordInput.setError("Password is empty.");
                }
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        //Login Successful
                        if (task.isSuccessful()){
                            Toast.makeText(this, "Log In Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, OrderPage.class);
                            emailInput.setText(null);
                            passwordInput.setText(null);
                            startActivity(intent);
                        }

                        //Login Failed
                        else {
                            Toast.makeText(this, "Log In Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }
}
