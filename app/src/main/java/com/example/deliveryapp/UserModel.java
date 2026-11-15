package com.example.deliveryapp;

public class UserModel {
    String username;
    String email;
    String height;
    String weight;
    String age;

    //Empty constructor
    public UserModel(){

    }
    //parameterized constructor
    public UserModel(String username, String email, String height, String weight, String age){
        this.username = username;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    //Getter methods
     public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getAge() {
        return age;
    }

    //Setter methods

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
