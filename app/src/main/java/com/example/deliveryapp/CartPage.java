package com.example.deliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

public class CartPage extends AppCompatActivity {

    private static final int SAMPLE_CALORIE_TARGET = 2000;

    private LinearLayout cartItemsContainer;
    private TextView textCartSubtitle;
    private TextView textTotalCalories;
    private TextView textGoalFeedback;
    private TextView textTotalCarbs;
    private TextView textTotalProtein;
    private TextView textTotalFats;
    private TextView textTotalPrice;
    private TextView btnPay;

    private ProgressBar progressCalories;

    private boolean isSubmittingOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupButtons();
        displayCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCart();
    }

    private void initializeViews() {

        cartItemsContainer = findViewById(R.id.cartItemsContainer);
        textCartSubtitle = findViewById(R.id.textCartSubtitle);
        textTotalCalories = findViewById(R.id.textTotalCalories);
        textGoalFeedback = findViewById(R.id.textGoalFeedback);
        textTotalCarbs = findViewById(R.id.textTotalCarbs);
        textTotalProtein = findViewById(R.id.textTotalProtein);
        textTotalFats = findViewById(R.id.textTotalFats);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        progressCalories = findViewById(R.id.progressCalories);
        btnPay = findViewById(R.id.btnPay);
    }

    private void setupButtons() {

        findViewById(R.id.btnBack).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        findViewById(R.id.btnShopMore).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                CartPage.this, OrderPage.class
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

        findViewById(R.id.btnProfile).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                CartPage.this,
                                ProfilePage.class
                        );

                        startActivity(intent);
                    }
                }
        );

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentDialog();
            }
        });
    }

    private void displayCart() {

        cartItemsContainer.removeAllViews();

        int itemCount = CartManager.getItemCount();

        textCartSubtitle.setText(
                itemCount + (itemCount == 1
                        ? " item ready to review"
                        : " items ready to review")
        );

        if (CartManager.isEmpty()) {

            TextView emptyMessage = new TextView(this);

            emptyMessage.setText(
                    "Your bag is empty. Add a meal to get started!"
            );

            emptyMessage.setTextSize(15);
            emptyMessage.setTextColor(0xFF697381);
            emptyMessage.setGravity(Gravity.CENTER);

            int padding = dp(20);
            emptyMessage.setPadding(
                    padding, dp(34), padding, dp(34)
            );

            emptyMessage.setBackgroundResource(
                    R.drawable.mb_card
            );

            cartItemsContainer.addView(emptyMessage);

        } else {

            ArrayList<FoodModal> items =
                    new ArrayList<>(CartManager.getCartItems());

            for (final FoodModal food : items) {
                addCartRow(food);
            }
        }

        updateSummary();
    }

    private void addCartRow(final FoodModal food) {

        View row = getLayoutInflater().inflate(
                R.layout.row_cart_item,
                cartItemsContainer,
                false
        );

        TextView textEmoji =
                row.findViewById(R.id.textCartFoodEmoji);

        TextView textName =
                row.findViewById(R.id.textCartItemName);

        TextView textInfo =
                row.findViewById(R.id.textCartItemInfo);

        TextView textPrice =
                row.findViewById(R.id.textCartItemPrice);

        TextView textQuantity =
                row.findViewById(R.id.textCartQty);

        textEmoji.setText(getFoodEmoji(food.getFoodName()));

        textName.setText(food.getFoodName());

        String info = food.getCalories() + " kcal / serving";

        if (food.getDiscount() > 0) {
            info += " \u00b7 " + food.getDiscount() + "% off";
        }

        textInfo.setText(info);

        textPrice.setText(formatMoney(food.getSubtotal()));

        textQuantity.setText(
                String.valueOf(food.getQuantity())
        );

        row.findViewById(R.id.btnMinus).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartManager.decreaseQuantity(food);
                        displayCart();
                    }
                }
        );

        row.findViewById(R.id.btnPlus).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartManager.increaseQuantity(food);
                        displayCart();
                    }
                }
        );

        row.findViewById(R.id.btnRemove).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartManager.removeItem(food);
                        displayCart();
                    }
                }
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.bottomMargin = dp(12);

        cartItemsContainer.addView(row, params);
    }

    private void updateSummary() {

        textTotalPrice.setText(
                formatMoney(CartManager.getTotalPrice())
        );

        textTotalCalories.setText(
                CartManager.getTotalCalories() + " kcal"
        );

        textTotalCarbs.setText(
                CartManager.getTotalCarbs() + " g"
        );

        textTotalProtein.setText(
                CartManager.getTotalProtein() + " g"
        );

        textTotalFats.setText(
                CartManager.getTotalFats() + " g"
        );

        int calories = CartManager.getTotalCalories();

        int percentage = Math.round(
                calories * 100f / SAMPLE_CALORIE_TARGET
        );

        textGoalFeedback.setText(
                "Meal is " + percentage
                        + "% of a sample "
                        + SAMPLE_CALORIE_TARGET
                        + " kcal daily goal."
        );

        progressCalories.setMax(100);
        progressCalories.setProgress(
                Math.min(100, percentage)
        );

        boolean hasItems = !CartManager.isEmpty();

        btnPay.setEnabled(hasItems);
        btnPay.setAlpha(hasItems ? 1f : 0.45f);
    }

    private void showPaymentDialog() {

        if (CartManager.isEmpty()) {
            Toast.makeText(
                    this,
                    "Your bag is empty",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        final String[] paymentOptions = {
                "Card (simulation)",
                "Cash (simulation)"
        };

        new AlertDialog.Builder(this)
                .setTitle("Select payment method")
                .setItems(
                        paymentOptions,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which
                            ) {
                                showConfirmationDialog(
                                        paymentOptions[which]
                                );
                            }
                        }
                )
                .show();
    }

    private void showConfirmationDialog(
            final String paymentMethod
    ) {

        String message =
                "Total: "
                        + formatMoney(CartManager.getTotalPrice())
                        + "\nMethod: " + paymentMethod
                        + "\n\nNo real money will be charged.";

        new AlertDialog.Builder(this)
                .setTitle("Confirm demo order")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                        "Place order",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which
                            ) {
                                submitOrder(paymentMethod);
                            }
                        }
                )
                .show();
    }


    private void submitOrder(final String paymentMethod) {

        if (isSubmittingOrder || CartManager.isEmpty()) {
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(
                    this,
                    "Please log in before placing an order",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        String userId = user.getUid();

        DatabaseReference ordersRef = FirebaseDatabase
                .getInstance()
                .getReference("orders")
                .child(userId);

        DatabaseReference newOrderRef = ordersRef.push();

        String orderId = newOrderRef.getKey();

        if (orderId == null) {
            Toast.makeText(
                    this,
                    "Unable to generate order ID",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Snapshot the cart so order contents do not change
        ArrayList<FoodModal> orderedItems = new ArrayList<>();

        for (FoodModal food : CartManager.getCartItems()) {

            FoodModal copy = new FoodModal(
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

            copy.setQuantity(food.getQuantity());
            orderedItems.add(copy);
        }

        OrderModal order = new OrderModal(
                orderId,
                userId,
                System.currentTimeMillis(),
                paymentMethod,
                "Placed",
                CartManager.getTotalPrice(),
                CartManager.getTotalCalories(),
                CartManager.getTotalCarbs(),
                CartManager.getTotalProtein(),
                CartManager.getTotalFats(),
                orderedItems
        );

        isSubmittingOrder = true;
        btnPay.setEnabled(false);
        btnPay.setAlpha(0.45f);

        newOrderRef.setValue(order)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<Void> task
                            ) {

                                isSubmittingOrder = false;

                                if (task.isSuccessful()) {

                                    CartManager.clearCart();
                                    displayCart();

                                    new AlertDialog.Builder(CartPage.this)
                                            .setTitle("Order placed!")
                                            .setMessage(
                                                    "Your order was saved successfully."
                                                            + "\n\nPayment is simulated."
                                            )
                                            .setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which
                                                        ) {
                                                            finish();
                                                        }
                                                    }
                                            )
                                            .show();

                                } else {

                                    displayCart();

                                    String errorMessage =
                                            task.getException() != null
                                                    ? task.getException().getMessage()
                                                    : "Unknown error";

                                    new AlertDialog.Builder(CartPage.this)
                                            .setTitle("Order not saved")
                                            .setMessage(
                                                    "Firebase could not save the order."
                                                            + "\n\n" + errorMessage
                                                            + "\n\nYour cart is still available."
                                            )
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            }
                        }
                );
    }

    private String formatMoney(double amount) {
        return String.format(
                Locale.CANADA,
                "$%.2f",
                amount
        );
    }

    private int dp(int value) {
        return Math.round(
                value * getResources()
                        .getDisplayMetrics().density
        );
    }

    private String getFoodEmoji(String foodName) {

        if (foodName == null) {
            return "\uD83C\uDF7D";
        }

        String name = foodName.toLowerCase(Locale.ROOT);

        if (name.contains("burger")) {
            return "\uD83C\uDF54";
        }

        if (name.contains("fries")) {
            return "\uD83C\uDF5F";
        }

        if (name.contains("pizza")) {
            return "\uD83C\uDF55";
        }

        if (name.contains("pie")) {
            return "\uD83E\uDD67";
        }

        return "\uD83C\uDF7D";
    }
}
