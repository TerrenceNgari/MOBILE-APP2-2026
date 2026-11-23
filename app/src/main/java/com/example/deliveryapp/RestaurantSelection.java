package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantSelection extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_selection);

        // Navigation Buttons
        TextView cartButton = findViewById(R.id.textButtonCart);
        TextView profileButton = findViewById(R.id.textButtonProfile);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<RestaurantModal> arrayList = new ArrayList<>();

        RestaurantAdapter adapter = new RestaurantAdapter(this, arrayList);

        recyclerView.setAdapter(adapter);


        DatabaseReference restaurantRef = FirebaseDatabase.getInstance()
                .getReference("restaurants");


        restaurantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String image = dataSnapshot.child("imageName").getValue(String.class);

                    RestaurantModal restaurantModal = new RestaurantModal(
                            id,
                            name,
                            description,
                            address,
                            image
                    );

                    arrayList.add(restaurantModal);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving the restaurant data!" + error.getMessage());
            }
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