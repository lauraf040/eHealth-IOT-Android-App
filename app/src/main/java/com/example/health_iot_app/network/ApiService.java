package com.example.health_iot_app.network;

import com.example.health_iot_app.models.Appointment;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.models.DoctorAppointmentSchedule;
import com.example.health_iot_app.models.SensorData;
import com.example.health_iot_app.models.UserModel;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<UserModel> loginUser(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("users/{id}")
    Call<UserModel> getUserById(@Path("id") String userId);

    @PUT("users/{id}")
    Call<UserModel> updateUser(@Path("id") String id, @Body UserModel user);

    @GET("doctors/doctor")
    Call<List<Doctor>> getDoctors();

    @GET("doctors/{id}")
    Call<Doctor> getDoctorById(@Path("id") String id);

    @GET("doctors/filteredDoctor")
    Call<List<Doctor>> getDoctorsByCategory(@Query("category") String category);

    @GET("doctors/orderedDoctor")
    Call<List<Doctor>> getSortedDoctors();

    @GET("doctors/filteredAndOrderedDoctor")
    Call<List<Doctor>> getFilteredAndOrderedDoctors(@Query("category") String category);

    @PUT("doctors/{id}")
    Call<String> updateDoctorAppointment(@Path("id") String doctorsId, @Body DoctorAppointmentSchedule dateSchedule);

    @DELETE("doctors/{id}")
    Call<Doctor> deleteAppHourFromDoctor(@Path("id") String doctorId, @Query("date") String appDate, @Query("hour") String appHour);

    @GET("arduino/lastData")
    Observable<SensorData> getLastData();

    @GET("arduino/getLastTenHumidityData")
    Observable<List<SensorData.SensorHumidityData>> getLastTenHumidityData();

    @GET("arduino/getLastTenBodyTempData")
    Observable<List<SensorData.SensorBodyTemperatureData>> getLastTenBodyTempData();

    @GET("arduino/getLastTenRoomTempData")
    Observable<List<SensorData.SensorRoomTemperatureData>> getLastTenRoomTempData();

    @GET("arduino/getLastTenPulseData")
    Observable<List<SensorData.SensorPulseData>> getLastTenPulseData();

    @GET("arduino/getLastTenBloodOxygenData")
    Observable<List<SensorData.SensorBloodOxygenData>> getLastTenBloodOxygenData();

    @POST("appointments/newApp")
    Call<Appointment> makeAppointment(@Body Appointment appointment);

    @GET("appointments/{id}")
    Call<List<Appointment>> getAppointments(@Path("id") String patientId);

    @DELETE("appointments/{id}")
    Call<Appointment> deleteAppointment(@Path("id") String appId);

    @POST("password/forgotPassword")
    Call<UserModel> checkEmailForReset(@Body JsonObject email);

    @PUT("password/resetPassword/{userId}")
    Call<UserModel> resetPassword(@Path("userId") String userId, @Body JsonObject password);
}
