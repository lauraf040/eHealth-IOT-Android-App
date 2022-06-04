package com.example.health_iot_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.health_iot_app.fragments.HomeFragment;

public class NewsActivity extends AppCompatActivity {

    private WebView webViewNews;
    private Intent intent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        webViewNews = findViewById(R.id.webview_news);
        intent = getIntent();
        url = intent.getStringExtra("url_web");
        webViewNews.loadUrl(url);
        webViewNews.getSettings().setJavaScriptEnabled(true);
        webViewNews.setWebViewClient(new WebViewClient());
    }
}