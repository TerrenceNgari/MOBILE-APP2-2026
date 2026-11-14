package com.example.deliveryapp;

public class FoodModal {

    // Food information
    private String foodName;
    private String imageName;
    private String description;

    // Price and nutrition
    private double price;
    private int calories;
    private int carbs;
    private int protein;
    private int fats;
    private int discount;
    private int quantity = 1;

    // Empty constructor required by Firebase
    public FoodModal() {
    }

    // Constructor with all parameters
    public FoodModal(String foodName, String imageName,
                     String description, double price,
                     int calories, int carbs, int protein,
                     int fats, int discount) {

        this.foodName = foodName;
        this.imageName = imageName;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fats = fats;
        this.discount = discount;
    }

    // Getters

    public String getFoodName() {
        return foodName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getProtein() {
        return protein;
    }

    public int getFats() {
        return fats;
    }

    public int getDiscount() {
        return discount;
    }

    // Setters

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    // Calculate discounted price
    public double getSalePrice() {
        return price * (100 - discount) / 100.0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return getSalePrice() * quantity;
    }
}
