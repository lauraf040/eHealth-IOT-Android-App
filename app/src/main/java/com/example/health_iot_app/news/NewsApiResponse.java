package com.example.health_iot_app.news;

import com.example.health_iot_app.models.Article;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsApiResponse implements Serializable {
    private String status;
    private int totalResults;
    List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
