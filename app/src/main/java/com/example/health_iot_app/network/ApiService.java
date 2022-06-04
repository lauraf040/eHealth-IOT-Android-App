package com.example.health_iot_app.network;

import com.example.health_iot_app.models.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("doctors/doctor")
    Call<List<Doctor>> getDoctors();

    @GET("doctors/filteredDoctor")
    Call<List<Doctor>> getDoctorsByCategory(@Query("category") String category);
}
