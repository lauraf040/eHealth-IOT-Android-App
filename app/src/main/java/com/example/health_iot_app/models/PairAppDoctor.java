package com.example.health_iot_app.models;

public class PairAppDoctor {
    private Appointment appointment;
    private Doctor doctor;

    public PairAppDoctor(Appointment appointment, Doctor doctor) {
        this.appointment = appointment;
        this.doctor = doctor;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
