package com.example.health_iot_app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.news.Articles;
import com.example.health_iot_app.news.SelectNewsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRvAdapter extends RecyclerView.Adapter<NewsRvAdapter.ViewHolder> {

    private Context context;
    private List<Articles> articles;
    private SelectNewsListener listener;

    public NewsRvAdapter(Context context, List<Articles> articles, SelectNewsListener listener) {
        this.context = context;
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.news_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNewsTitle.setText(articles.get(position).getTitle());
        holder.tvNewsSource.setText(articles.get(position).getSource().getName());

        if (articles.get(position).getUrlToImage() != null) {
            Picasso.get().load(articles.get(position).getUrlToImage())
                    .into(holder.ivNews);
        }

        holder.cardViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnNewsClicked(articles.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNewsTitle;
        TextView tvNewsSource;
        ImageView ivNews;
        CardView cardViewNews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.news_title);
            tvNewsSource = itemView.findViewById(R.id.news_source);
            ivNews = itemView.findViewById(R.id.iv_news);
            cardViewNews = itemView.findViewById(R.id.cardview_news_item);
        }
    }
}
