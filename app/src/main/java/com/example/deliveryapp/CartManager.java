package com.example.deliveryapp;

import java.util.ArrayList;

public class CartManager {

    // Shared cart accessible from all Activities
    private static final ArrayList<FoodModal> cartItems =
            new ArrayList<>();

    // Get all items currently in the cart
    public static ArrayList<FoodModal> getCartItems() {
        return cartItems;
    }

    // Add a food item to the cart
    public static void addItem(FoodModal newItem) {

        if (newItem == null || newItem.getQuantity() <= 0) {
            return;
        }

        // Check whether the food already exists in the cart
        for (FoodModal existing : cartItems) {

            if (existing.getFoodName().equals(newItem.getFoodName())) {

                // Increase quantity instead of adding duplicate
                existing.setQuantity(
                        Math.min(99, existing.getQuantity()
                                + newItem.getQuantity())
                );

                return;
            }
        }

        // Add a separate copy to avoid modifying menu objects
        FoodModal cartFood = new FoodModal(
                newItem.getFoodName(),
                newItem.getImageName(),
                newItem.getDescription(),
                newItem.getPrice(),
                newItem.getCalories(),
                newItem.getCarbs(),
                newItem.getProtein(),
                newItem.getFats(),
                newItem.getDiscount()
        );

        cartFood.setQuantity(Math.min(99, newItem.getQuantity()));

        cartItems.add(cartFood);
    }

    // Increase quantity of an item
    public static void increaseQuantity(FoodModal item) {

        if (cartItems.contains(item) && item.getQuantity() < 99) {
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    // Decrease quantity of an item
    public static void decreaseQuantity(FoodModal item) {

        if (!cartItems.contains(item)) {
            return;
        }

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            // Remove item if quantity reaches zero
            cartItems.remove(item);
        }
    }

    // Remove an item completely
    public static void removeItem(FoodModal item) {
        cartItems.remove(item);
    }

    // Total number of food units in the cart
    public static int getItemCount() {

        int count = 0;

        for (FoodModal item : cartItems) {
            count += item.getQuantity();
        }

        return count;
    }

    // Total price after applying item discounts
    public static double getTotalPrice() {

        double total = 0;

        for (FoodModal item : cartItems) {
            total += item.getSubtotal();
        }

        return total;
    }

    // Total calories
    public static int getTotalCalories() {

        int total = 0;

        for (FoodModal item : cartItems) {
            total += item.getCalories() * item.getQuantity();
        }

        return total;
    }

    // Total carbohydrates
    public static int getTotalCarbs() {

        int total = 0;

        for (FoodModal item : cartItems) {
            total += item.getCarbs() * item.getQuantity();
        }

        return total;
    }

    // Total protein
    public static int getTotalProtein() {

        int total = 0;

        for (FoodModal item : cartItems) {
            total += item.getProtein() * item.getQuantity();
        }

        return total;
    }

    // Total fats
    public static int getTotalFats() {

        int total = 0;

        for (FoodModal item : cartItems) {
            total += item.getFats() * item.getQuantity();
        }

        return total;
    }

    // Check whether cart is empty
    public static boolean isEmpty() {
        return cartItems.isEmpty();
    }

    // Clear cart after checkout
    public static void clearCart() {
        cartItems.clear();
    }
}
