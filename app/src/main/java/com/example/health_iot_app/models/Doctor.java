package com.example.health_iot_app.models;

public class Doctor {
    private int profilePicture;
    private String name;
    private String category;
    private int age;
    private String phone;
    private double rating;
    private String description;
    private String profileImageURL;


    public Doctor() {
    }

    public Doctor(int profilePicture, String name, String category, int age, String phone, double rating, String description, String imageURL) {
        this.profilePicture = profilePicture;
        this.name = name;
        this.category = category;
        this.age = age;
        this.phone = phone;
        this.rating = rating;
        this.description = description;
        this.profileImageURL = imageURL;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public String getImageURL() {
        return profileImageURL;
    }

    public void setImageURL(String imageURL) {
        this.profileImageURL = imageURL;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
