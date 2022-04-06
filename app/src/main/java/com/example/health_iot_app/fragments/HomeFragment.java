package com.example.health_iot_app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.health_iot_app.HomeActivity;
import com.example.health_iot_app.NewsActivity;
import com.example.health_iot_app.R;
import com.example.health_iot_app.news.Articles;
import com.example.health_iot_app.news.NewsApiResponse;
import com.example.health_iot_app.news.NewsRequestManager;
import com.example.health_iot_app.news.OnFetchDataListener;
import com.example.health_iot_app.news.SelectNewsListener;
import com.example.health_iot_app.utils.CategoriesRvAdapter;
import com.example.health_iot_app.utils.CategoryRvModel;
import com.example.health_iot_app.utils.NewsRvAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements SelectNewsListener {

    public static final String ARTICLE_KEY = "ARTICLE_KEY";
    private RecyclerView recyclerViewCategories, newsRecyclerView;
    private NewsRvAdapter newsRvAdapter;
    private CategoriesRvAdapter staticRvAdapter;
    private ArrayList<CategoryRvModel> items;


    public HomeFragment() {
        // Required empty public constructor
    }

    //Fragment Logic
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        items = new ArrayList<>();
        items.add(new CategoryRvModel(R.drawable.cardiology, "Cardiologie"));
        items.add(new CategoryRvModel(R.drawable.cardiology, "Pneumologie"));
        items.add(new CategoryRvModel(R.drawable.cardiology, "Reumatologie"));
        items.add(new CategoryRvModel(R.drawable.cardiology, "Cardiologie"));
        items.add(new CategoryRvModel(R.drawable.cardiology, "Cardiologie"));

        recyclerViewCategories = view.findViewById(R.id.recycler_categories);
        staticRvAdapter = new CategoriesRvAdapter(items);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategories.setAdapter(staticRvAdapter);

        newsRecyclerView = view.findViewById(R.id.recycler_news);

        NewsRequestManager newsRequestManager = new NewsRequestManager(getContext());
        newsRequestManager.getNewsHeadlines(listener, "health");
    }

    //News Api Logic
    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<Articles> list, String message) {
            showNews(list);
        }

        @Override
        public void onError(String message) {

        }
    };

    private void showNews(List<Articles> list) {
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        newsRvAdapter = new NewsRvAdapter(getContext(), list, this);
        newsRecyclerView.setAdapter(newsRvAdapter);

    }

    @Override
    public void OnNewsClicked(Articles articles) {
        Intent intent = new Intent(getActivity(), NewsActivity.class);
        intent.putExtra(ARTICLE_KEY, articles.getUrl());
        startActivity(intent);
    }
}