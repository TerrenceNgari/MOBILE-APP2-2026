
package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ItemPage extends AppCompatActivity {

    private TextView textItemName;
    private TextView textItemDescription;
    private TextView textItemPrice;
    private TextView textCalories;
    private TextView textCarbs;
    private TextView textProtein;
    private TextView textFat;
    private TextView textQuantity;
    private TextView btnAddToCart;
    private TextView btnMinus;
    private TextView btnPlus;
    private TextView btnBack;
    private TextView btnCart;

    private ImageView itemFoodImage;

    // Selected food information
    private String foodName;
    private String description;
    private String imageName;

    private double price;
    private int calories;
    private int carbs;
    private int protein;
    private int fats;
    private int discount;

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_page);

        // 1. Connect Java variables to XML views
        initializeViews();

        // 2. Receive food details from FoodAdapter
        receiveFoodDetails();

        // 3. Display selected food information
        displayFoodDetails();

        // 4. Set up quantity buttons
        setupQuantityButtons();

        // 5. Set up navigation and cart button
        setupButtons();
    }

    private void initializeViews() {

        textItemName = findViewById(R.id.textItemName);
        textItemDescription = findViewById(R.id.textItemDescription);
        textItemPrice = findViewById(R.id.textItemPrice);

        textCalories = findViewById(R.id.textCalories);
        textCarbs = findViewById(R.id.textCarbs);
        textProtein = findViewById(R.id.textProtein);
        textFat = findViewById(R.id.textFat);

        textQuantity = findViewById(R.id.textQuantity);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);

        itemFoodImage = findViewById(R.id.itemFoodImage);
    }

    private void receiveFoodDetails() {

        Intent intent = getIntent();

        foodName = intent.getStringExtra("foodName");
        description = intent.getStringExtra("description");
        imageName = intent.getStringExtra("imageName");

        price = intent.getDoubleExtra("price", 0.0);

        calories = intent.getIntExtra("calories", 0);
        carbs = intent.getIntExtra("carbs", 0);
        protein = intent.getIntExtra("protein", 0);
        fats = intent.getIntExtra("fats", 0);
        discount = intent.getIntExtra("discount", 0);

        if (foodName == null) {
            foodName = "Unknown food";
        }

        if (description == null) {
            description = "";
        }
    }

    private double getSalePrice() {
        return price * (100 - discount) / 100.0;
    }

    private String formatPrice(double amount) {
        return String.format(Locale.CANADA, "$%.2f", amount);
    }

    private void displayFoodDetails() {

        textItemName.setText(foodName);
        textItemDescription.setText(description);

        textCalories.setText(calories + " kcal");
        textCarbs.setText(carbs + " g");
        textProtein.setText(protein + " g");
        textFat.setText(fats + " g");

        textItemPrice.setText(
                formatPrice(getSalePrice()) + " each"
        );

        // Load the local drawable using its name
        if (imageName != null && !imageName.isEmpty()) {

            int imageId = getResources().getIdentifier(
                    imageName,
                    "drawable",
                    getPackageName()
            );

            if (imageId != 0) {
                itemFoodImage.setImageResource(imageId);
            } else {
                itemFoodImage.setImageResource(
                        R.drawable.ic_launcher_background
                );
            }

        } else {
            itemFoodImage.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }

        updateQuantityDisplay();
    }

    private void setupQuantityButtons() {

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity > 1) {
                    quantity--;
                    updateQuantityDisplay();
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity < 25) {
                    quantity++;
                    updateQuantityDisplay();
                }
            }
        });
    }

    private void updateQuantityDisplay() {

        textQuantity.setText(String.valueOf(quantity));

        double totalPrice = getSalePrice() * quantity;

        btnAddToCart.setText(
                "Add to cart · " + formatPrice(totalPrice)
        );
    }

    private void setupButtons() {

        // Return to existing Order Page
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Open Cart Page
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        ItemPage.this,
                        CartPage.class
                );

                startActivity(intent);
            }
        });

        // Add to Cart
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FoodModal cartFood = new FoodModal(
                        foodName,
                        imageName,
                        description,
                        price,
                        calories,
                        carbs,
                        protein,
                        fats,
                        discount
                );

                cartFood.setQuantity(quantity);

                CartManager.addItem(cartFood);

                Toast.makeText(
                        ItemPage.this,
                        foodName + " added to cart",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        ItemPage.this,
                        CartPage.class
                );

                startActivity(intent);
            }
        });
    }
}
