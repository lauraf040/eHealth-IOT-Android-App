package com.example.health_iot_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DoctorAppointmentSchedule implements Parcelable {
    private String date;
    private ArrayList<String> times;

    public DoctorAppointmentSchedule(String date, ArrayList<String> times) {
        this.date = date;
        this.times = times;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }

    protected DoctorAppointmentSchedule(Parcel source) {
        this.date = source.readString();
        this.times = source.readArrayList(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.date);
        dest.writeStringList(this.times);
    }

    public static final Creator<DoctorAppointmentSchedule> CREATOR = new Creator<DoctorAppointmentSchedule>() {
        public DoctorAppointmentSchedule createFromParcel(Parcel source) {
            return new DoctorAppointmentSchedule(source);
        }

        public DoctorAppointmentSchedule[] newArray(int size) {
            return new DoctorAppointmentSchedule[size];
        }
    };

}
