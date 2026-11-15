
package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class OrderPage extends AppCompatActivity {

    // Items displayed in RecyclerView
    ArrayList<FoodModal> arrLst = new ArrayList<>();

    // All food items retrieved from Firebase
    ArrayList<FoodModal> allFoodItems = new ArrayList<>();

    FoodAdapter adapter;
    DatabaseReference databaseReference;

    private ValueEventListener foodListener;

    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_page);

        // Profile button
        findViewById(R.id.btnOrderProfile).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                OrderPage.this,
                                ProfilePage.class
                        );

                        startActivity(intent);
                    }
                }
        );

        // Cart button
        findViewById(R.id.cartBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                OrderPage.this,
                                CartPage.class
                        );

                        startActivity(intent);
                    }
                }
        );

        // RecyclerView
        RecyclerView recView =
                findViewById(R.id.restItemsRecView);

        recView.setLayoutManager(
                new GridLayoutManager(this, 2)
        );

        adapter = new FoodAdapter(this, arrLst);
        recView.setAdapter(adapter);

        // Search bar
        searchBar = findViewById(R.id.searchBar);

        searchBar.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        // Filter immediately as user types
                        filterFoodItems(s.toString());
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );

        // Connect to Firebase
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("restaurants")
                .child("restaurant_01")
                .child("items");

        loadFoodItems();
    }

    private void loadFoodItems() {

        foodListener = new ValueEventListener() {

            @Override
            public void onDataChange(
                    @NonNull DataSnapshot snapshot
            ) {

                // Clear old Firebase data
                allFoodItems.clear();

                // Retrieve every food item
                for (DataSnapshot itemSnapshot
                        : snapshot.getChildren()) {

                    FoodModal food =
                            itemSnapshot.getValue(
                                    FoodModal.class
                            );

                    if (food != null) {
                        allFoodItems.add(food);
                    }
                }

                // Apply current search query
                // even if Firebase data changes
                filterFoodItems(
                        searchBar.getText().toString()
                );
            }

            @Override
            public void onCancelled(
                    @NonNull DatabaseError error
            ) {

                Toast.makeText(
                        OrderPage.this,
                        "Failed to load food: "
                                + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        };

        databaseReference.addValueEventListener(
                foodListener
        );
    }

    // Filter the displayed food items
    private void filterFoodItems(String query) {

        String searchText = query
                .trim()
                .toLowerCase(Locale.ROOT);

        // Clear currently displayed items
        arrLst.clear();

        for (FoodModal food : allFoodItems) {

            String foodName = food.getFoodName() == null
                    ? ""
                    : food.getFoodName()
                    .toLowerCase(Locale.ROOT);

            String description =
                    food.getDescription() == null
                            ? ""
                            : food.getDescription()
                            .toLowerCase(Locale.ROOT);

            // Empty search displays everything
            if (searchText.isEmpty()
                    || foodName.contains(searchText)
                    || description.contains(searchText)) {

                arrLst.add(food);
            }
        }

        // Refresh RecyclerView
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseReference != null
                && foodListener != null) {

            databaseReference.removeEventListener(
                    foodListener
            );
        }
    }
}
