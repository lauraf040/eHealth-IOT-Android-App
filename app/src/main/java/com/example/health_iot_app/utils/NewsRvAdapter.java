package com.example.health_iot_app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.NewsActivity;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRvAdapter extends RecyclerView.Adapter<NewsRvAdapter.ViewHolder> {

    private Context context;
    private List<Article> articles;
    private OnItemClickListener onItemClickListener;


    public NewsRvAdapter(List<Article> articles, Context context, OnItemClickListener onItemClickListener) {
        this.articles = articles;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        //return new holder instance
        NewsRvAdapter.ViewHolder newsRvHolder = new NewsRvAdapter.ViewHolder(view, onItemClickListener);

        return newsRvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Article currentArticle = articles.get(position);
        holder.tvNewsTitle.setText(currentArticle.getTitle());
        holder.tvNewsSource.setText(currentArticle.getSource().getName());

        if (currentArticle.getUrlToImage() != null) {
            Picasso.get().load(articles.get(position).getUrlToImage())
                    .into(holder.ivNews);
        }
        holder.cardViewNews.setOnClickListener((view) -> {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("url_web", articles.get(position).getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNewsTitle;
        TextView tvNewsSource;
        ImageView ivNews;
        CardView cardViewNews;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.news_title);
            tvNewsSource = itemView.findViewById(R.id.news_source);
            ivNews = itemView.findViewById(R.id.iv_news);
            cardViewNews = itemView.findViewById(R.id.cardview_news_item);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
