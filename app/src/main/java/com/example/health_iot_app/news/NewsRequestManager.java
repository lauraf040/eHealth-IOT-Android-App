package com.example.health_iot_app.news;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class NewsRequestManager {

    Context context;

    private static final String NEWS_URL = "https://newsapi.org/v2/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNewsHeadlines(OnFetchDataListener listener, String category) {
        NewsApiService newsApiService = retrofit.create(NewsApiService.class);
        Call<NewsApiResponse> call = newsApiService.getNews();
        try {
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context,
                                "Error fetching news",
                                Toast.LENGTH_LONG);
                    }
                    listener.onFetchData(response.body().getArticles(), response.message());
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                    listener.onError("Request failed!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsRequestManager(Context context) {
        this.context = context;
    }

    public interface NewsApiService {

        @GET("top-headlines?country=ro&category=health&apiKey=168d8d94275a42d687bd81951d869389")
        Call<NewsApiResponse> getNews();

    }
}
