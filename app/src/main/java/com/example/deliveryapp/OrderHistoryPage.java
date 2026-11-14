package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class OrderHistoryPage extends AppCompatActivity {

    private LinearLayout historyContainer;
    private TextView textHistorySubtitle;
    private TextView btnHistoryCart;
    private ProgressBar historyProgress;

    private DatabaseReference ordersRef;
    private ValueEventListener ordersListener;

    private final ArrayList<OrderModal> orders =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history_page);

        initializeViews();
        setupButtons();
        loadOrderHistory();
    }

    private void initializeViews() {

        historyContainer =
                findViewById(R.id.historyContainer);

        textHistorySubtitle =
                findViewById(R.id.textHistorySubtitle);

        btnHistoryCart =
                findViewById(R.id.btnHistoryCart);

        historyProgress =
                findViewById(R.id.historyProgress);
    }

    private void setupButtons() {

        findViewById(R.id.btnHistoryBack)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        btnHistoryCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                OrderHistoryPage.this,
                                CartPage.class
                        );

                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartButton();
    }

    private void updateCartButton() {

        btnHistoryCart.setText(
                "Bag · " + CartManager.getItemCount()
        );
    }

    private void loadOrderHistory() {

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            showMessage(
                    "Please log in to view your order history."
            );

            return;
        }

        historyProgress.setVisibility(View.VISIBLE);

        ordersRef = FirebaseDatabase
                .getInstance()
                .getReference("orders")
                .child(user.getUid());

        ordersListener = new ValueEventListener() {

            @Override
            public void onDataChange(
                    @NonNull DataSnapshot snapshot
            ) {

                historyProgress.setVisibility(View.GONE);

                orders.clear();

                for (DataSnapshot orderSnapshot
                        : snapshot.getChildren()) {

                    OrderModal order =
                            orderSnapshot.getValue(
                                    OrderModal.class
                            );

                    if (order != null) {
                        orders.add(order);
                    }
                }

                // Newest orders first
                Collections.sort(
                        orders,
                        new Comparator<OrderModal>() {
                            @Override
                            public int compare(
                                    OrderModal first,
                                    OrderModal second
                            ) {

                                return Long.compare(
                                        second.getTimestamp(),
                                        first.getTimestamp()
                                );
                            }
                        }
                );

                displayOrders();
            }

            @Override
            public void onCancelled(
                    @NonNull DatabaseError error
            ) {

                historyProgress.setVisibility(View.GONE);

                showMessage(
                        "Unable to load orders: "
                                + error.getMessage()
                );
            }
        };

        ordersRef.addValueEventListener(ordersListener);
    }

    private void displayOrders() {

        historyContainer.removeAllViews();

        if (orders.isEmpty()) {

            textHistorySubtitle.setText(
                    "No previous orders found."
            );

            showEmptyHistory();
            return;
        }

        textHistorySubtitle.setText(
                orders.size() == 1
                        ? "1 previous order"
                        : orders.size() + " previous orders"
        );

        for (OrderModal order : orders) {
            addOrderCard(order);
        }
    }

    private void addOrderCard(final OrderModal order) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(18), dp(18), dp(18), dp(18)
        );

        card.setBackgroundResource(R.drawable.mb_card);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.bottomMargin = dp(14);

        // Order date
        TextView dateText = createText(
                "Order · " + formatDate(order.getTimestamp()),
                18,
                "#1C2533",
                true
        );

        card.addView(dateText);

        // Order status
        TextView statusText = createText(
                "Status: " + safeText(order.getStatus()),
                13,
                "#24794B",
                true
        );

        statusText.setPadding(
                0, dp(6), 0, dp(10)
        );

        card.addView(statusText);

        // Ordered foods
        ArrayList<FoodModal> items = order.getItems();

        if (items != null) {

            for (FoodModal food : items) {

                if (food == null) {
                    continue;
                }

                TextView foodText = createText(
                        food.getQuantity()
                                + " × "
                                + safeText(food.getFoodName()),
                        15,
                        "#697381",
                        false
                );

                foodText.setPadding(
                        0, dp(3), 0, dp(3)
                );

                card.addView(foodText);
            }
        }

        // Total price
        TextView priceText = createText(
                "Total: " + formatMoney(order.getTotalPrice()),
                18,
                "#F66A2A",
                true
        );

        priceText.setPadding(
                0, dp(14), 0, dp(6)
        );

        card.addView(priceText);

        // Nutrition
        TextView nutritionText = createText(
                "Calories: " + order.getTotalCalories()
                        + " kcal"
                        + "\nCarbs: " + order.getTotalCarbs()
                        + " g  ·  Protein: "
                        + order.getTotalProtein()
                        + " g  ·  Fats: "
                        + order.getTotalFats() + " g",
                13,
                "#697381",
                false
        );

        nutritionText.setLineSpacing(dp(4), 1f);

        card.addView(nutritionText);

        // Payment method
        TextView paymentText = createText(
                "Payment: "
                        + safeText(order.getPaymentMethod()),
                13,
                "#697381",
                false
        );

        paymentText.setPadding(
                0, dp(10), 0, 0
        );

        card.addView(paymentText);

        // Reorder button
        TextView reorderButton = createText(
                "Reorder",
                15,
                "#FFFFFF",
                true
        );

        reorderButton.setGravity(Gravity.CENTER);

        reorderButton.setBackgroundResource(
                R.drawable.mb_primary
        );

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(50)
                );

        buttonParams.topMargin = dp(16);

        card.addView(reorderButton, buttonParams);

        reorderButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reorder(order);
                    }
                }
        );

        historyContainer.addView(card, cardParams);
    }

    private void reorder(OrderModal order) {

        ArrayList<FoodModal> items = order.getItems();

        if (items == null || items.isEmpty()) {

            Toast.makeText(
                    this,
                    "No items available to reorder",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        for (FoodModal food : items) {

            if (food == null || food.getQuantity() <= 0) {
                continue;
            }

            // Create a copy of the saved food item
            FoodModal cartFood = new FoodModal(
                    food.getFoodName(),
                    food.getImageName(),
                    food.getDescription(),
                    food.getPrice(),
                    food.getCalories(),
                    food.getCarbs(),
                    food.getProtein(),
                    food.getFats(),
                    food.getDiscount()
            );

            cartFood.setQuantity(food.getQuantity());

            CartManager.addItem(cartFood);
        }

        Toast.makeText(
                this,
                "Previous items added to cart",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                OrderHistoryPage.this,
                CartPage.class
        );

        startActivity(intent);
    }

    private void showEmptyHistory() {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(20), dp(28), dp(20), dp(28)
        );

        card.setBackgroundResource(R.drawable.mb_card);

        TextView title = createText(
                "No orders yet",
                20,
                "#1C2533",
                true
        );

        title.setGravity(Gravity.CENTER);

        card.addView(title);

        TextView description = createText(
                "Your completed orders will appear here.",
                14,
                "#697381",
                false
        );

        description.setGravity(Gravity.CENTER);

        description.setPadding(
                0, dp(10), 0, dp(16)
        );

        card.addView(description);

        TextView browseButton = createText(
                "Browse food",
                15,
                "#FFFFFF",
                true
        );

        browseButton.setGravity(Gravity.CENTER);

        browseButton.setBackgroundResource(
                R.drawable.mb_primary
        );

        card.addView(
                browseButton,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(50)
                )
        );

        browseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                OrderHistoryPage.this,
                                OrderPage.class
                        );

                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        );

                        startActivity(intent);
                        finish();
                    }
                }
        );

        historyContainer.addView(card);
    }

    private void showMessage(String message) {

        historyContainer.removeAllViews();

        TextView messageText = createText(
                message,
                15,
                "#697381",
                false
        );

        messageText.setGravity(Gravity.CENTER);

        messageText.setPadding(
                dp(16), dp(30), dp(16), dp(30)
        );

        historyContainer.addView(messageText);
    }

    private TextView createText(
            String value,
            int size,
            String color,
            boolean bold
    ) {

        TextView text = new TextView(this);

        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(
                android.graphics.Color.parseColor(color)
        );

        if (bold) {
            text.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );
        }

        return text;
    }

    private String formatDate(long timestamp) {

        SimpleDateFormat format = new SimpleDateFormat(
                "MMM dd, yyyy · hh:mm a",
                Locale.CANADA
        );

        return format.format(new Date(timestamp));
    }

    private String formatMoney(double amount) {

        return String.format(
                Locale.CANADA,
                "$%.2f",
                amount
        );
    }

    private String safeText(String value) {

        return value == null ? "" : value;
    }

    private int dp(int value) {

        return Math.round(
                value * getResources()
                        .getDisplayMetrics().density
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ordersRef != null && ordersListener != null) {
            ordersRef.removeEventListener(ordersListener);
        }
    }
}
