package com.example.health_iot_app.news;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApiService {
    @GET("top-headlines?country=ro&category=health&apiKey=168d8d94275a42d687bd81951d869389")
    Call<NewsApiResponse> fetchNews();
}
