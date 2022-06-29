package com.example.health_iot_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Appointment implements Parcelable {
    private String doctorID;
    private String patientID;
    private String appDate;
    private String time;
    private String _id;

    public Appointment() {
    }

    public Appointment(String doctorID, String patientID, String appDate, String time) {
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.appDate = appDate;
        this.time = time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //=======================PARCELABLE ==========================
    private Appointment(Parcel source) {
        this._id = source.readString();
        this.doctorID = source.readString();
        this.patientID = source.readString();
        this.appDate = source.readString();
        this.time = source.readString();
    }

    public static Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel source) {
            return new Appointment(source);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(doctorID);
        dest.writeString(patientID);
        dest.writeString(appDate);
        dest.writeString(time);
    }
}
