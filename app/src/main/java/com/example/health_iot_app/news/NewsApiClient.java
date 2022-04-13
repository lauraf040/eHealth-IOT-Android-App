package com.example.health_iot_app.news;

import android.content.Context;
import android.widget.Toast;

import com.example.health_iot_app.network.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class NewsApiClient {


    public static final String NEWS_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NEWS_URL)
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }


    public static NewsApiService getService() {
        NewsApiService newsApiService = getRetrofit().create(NewsApiService.class);
        return newsApiService;
    }


}
