package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RestaurantSelection extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_selection);

        //Only menuButton is used here for now.
        TextView cartButton = findViewById(R.id.textButtonCart);
        TextView profileButton = findViewById(R.id.textButtonProfile);
        ImageView restaurantImage = findViewById(R.id.imageRestaurant);
        TextView restaurantName = findViewById(R.id.textRestaurantName);
        TextView restaurantDescription = findViewById(R.id.textRestaurantDescription);
        TextView restaurantAddress = findViewById(R.id.textRestaurantAddress);
        TextView menuButton = findViewById(R.id.textButtonRestaurantMenu);

        menuButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrderPage.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                RestaurantSelection.this,
                                ProfilePage.class
                        );

                        startActivity(intent);
                    }
                }
        );

        cartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                RestaurantSelection.this,
                                CartPage.class
                        );

                        startActivity(intent);
                    }
                }
        );
    }
}