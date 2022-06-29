package com.example.health_iot_app.network;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:3000/api/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60 * 2, TimeUnit.SECONDS)
                .connectTimeout(60 * 2, TimeUnit.SECONDS)
                .writeTimeout(60 * 2, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor).build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit;
    }


    public static ApiService getService() {
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService;
    }
}
