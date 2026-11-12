package com.example.deliveryapp;

public class FoodModal {
    String foodName;
    int foodImg;

    //constructor
    public FoodModal(){

    }

    //Constructor with parameters
    public FoodModal(String foodName, int foodImg){
        this.foodName = foodName;
        this.foodImg = foodImg;
    }

    //getters

    public int getFoodImg() {
        return foodImg;
    }

    public String getFoodName() {
        return foodName;
    }

    //setters

    public void setFoodImg(int foodImg) {
        this.foodImg = foodImg;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
