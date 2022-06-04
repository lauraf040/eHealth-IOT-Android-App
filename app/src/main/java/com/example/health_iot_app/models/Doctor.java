package com.example.health_iot_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Doctor implements Parcelable {
    private int profilePicture;
    private String name;
    private String category;
    private int age;
    private String phone;
    private double rating;
    private String description;
    private String profileImageURL;
    private String location;
    private String email;
    private String latitude;
    private String longitude;
    private double price;
    private ArrayList<DoctorAppointmentSchedule> appointments;

    public Doctor() {
    }

    public Doctor(int profilePicture, String name, String category, int age, String phone, double rating,
                  String description, String imageURL, String location, String email,
                  ArrayList<DoctorAppointmentSchedule> appointments, String latitude, String longitude, double price) {
        this.profilePicture = profilePicture;
        this.name = name;
        this.category = category;
        this.age = age;
        this.phone = phone;
        this.rating = rating;
        this.description = description;
        this.profileImageURL = imageURL;
        this.location = location;
        this.email = email;
        this.appointments = appointments;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<DoctorAppointmentSchedule> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<DoctorAppointmentSchedule> appointments) {
        this.appointments = appointments;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    //=================================PARCELABLE REQUIREMENTS================================
    private Doctor(Parcel source) {
        this.profilePicture = source.readInt();
        this.name = source.readString();
        this.category = source.readString();
        this.age = source.readInt();
        this.phone = source.readString();
        this.rating = source.readDouble();
        this.description = source.readString();
        this.profileImageURL = source.readString();
        this.location = source.readString();
        this.email = source.readString();
        this.latitude = source.readString();
        this.longitude = source.readString();
        this.price = source.readDouble();
        this.appointments = source.readArrayList(DoctorAppointmentSchedule.class.getClassLoader());
    }

    public static Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel source) {
            return new Doctor(source);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(profilePicture);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeInt(age);
        dest.writeString(phone);
        dest.writeDouble(rating);
        dest.writeString(description);
        dest.writeString(profileImageURL);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeDouble(price);
        dest.writeTypedList(appointments);
    }
}
