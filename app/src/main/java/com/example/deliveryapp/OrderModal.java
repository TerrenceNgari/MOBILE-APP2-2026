package com.example.deliveryapp;

import java.util.ArrayList;

public class OrderModal {

    private String orderId;
    private String userId;
    private long timestamp;
    private String paymentMethod;
    private String status;
    private double totalPrice;
    private int totalCalories;
    private int totalCarbs;
    private int totalProtein;
    private int totalFats;
    private ArrayList<FoodModal> items;

    // Required by Firebase
    public OrderModal() {
    }

    public OrderModal(String orderId, String userId,
                      long timestamp, String paymentMethod,
                      String status, double totalPrice,
                      int totalCalories, int totalCarbs,
                      int totalProtein, int totalFats,
                      ArrayList<FoodModal> items) {

        this.orderId = orderId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.totalPrice = totalPrice;
        this.totalCalories = totalCalories;
        this.totalCarbs = totalCarbs;
        this.totalProtein = totalProtein;
        this.totalFats = totalFats;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public int getTotalCarbs() {
        return totalCarbs;
    }

    public int getTotalProtein() {
        return totalProtein;
    }

    public int getTotalFats() {
        return totalFats;
    }

    public ArrayList<FoodModal> getItems() {
        return items;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public void setTotalCarbs(int totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public void setTotalProtein(int totalProtein) {
        this.totalProtein = totalProtein;
    }

    public void setTotalFats(int totalFats) {
        this.totalFats = totalFats;
    }

    public void setItems(ArrayList<FoodModal> items) {
        this.items = items;
    }
}
