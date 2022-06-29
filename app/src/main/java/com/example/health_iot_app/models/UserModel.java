package com.example.health_iot_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String _id;
    private String name;
    private String email;
    private int age;
    private String phone;
    private String address;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private String password;
    private Integer nbOfApp;
    private Integer pointsFromApp;

    public UserModel() {
    }

    public UserModel(Integer nbOfApp, Integer pointsFromApp) {
        this.nbOfApp = nbOfApp;
        this.pointsFromApp = pointsFromApp;
    }

    public UserModel(String name, String email, int age, String phone, String address) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }

    public UserModel(String _id, String name, String email, int age, String phone, String createdAt, String updatedAt, int __v, String password, String address) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
        this.password = password;
        this.address = address;
    }

    public Integer getNbOfApp() {
        return nbOfApp;
    }

    public void setNbOfApp(Integer nbOfApp) {
        this.nbOfApp = nbOfApp;
    }

    public Integer getPointsFromApp() {
        return pointsFromApp;
    }

    public void setPointsFromApp(Integer pointsFromApp) {
        this.pointsFromApp = pointsFromApp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //=============================================PARCELABLE
    private UserModel(Parcel source) {
        this._id = source.readString();
        this.name = source.readString();
        this.email = source.readString();
        this.age = source.readInt();
        this.phone = source.readString();
        this.createdAt = source.readString();
        this.updatedAt = source.readString();
        this.__v = source.readInt();
        this.password = source.readString();
    }

    public static Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(age);
        dest.writeString(phone);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(__v);
        dest.writeString(password);
    }
}
