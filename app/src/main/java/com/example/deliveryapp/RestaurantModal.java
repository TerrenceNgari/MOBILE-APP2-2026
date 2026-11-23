package com.example.deliveryapp;

public class RestaurantModal {

    // Restaurant Information
    private String id;
    private String name;
    private String description;
    private String address;
    private String imageName;

    // Empty constructor required by Firebase
    public RestaurantModal(){
    }

    // Constructor with all parameters
    public RestaurantModal(String id, String name, String description,
                           String address, String imageName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.imageName = imageName;
    }


    // Getters

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getAddress(){
        return address;
    }

    public String getImageName(){
        return imageName;
    }



    // Setters

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

}
