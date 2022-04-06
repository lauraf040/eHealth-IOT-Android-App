package com.example.health_iot_app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;

import java.util.List;

public class CategoriesRvAdapter extends RecyclerView.Adapter<CategoriesRvAdapter.ViewHolder> {

    private List<CategoryRvModel> rvItems;
    int rowIndex = -1;

    public CategoriesRvAdapter(List<CategoryRvModel> rvItems) {
        this.rvItems = rvItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View itemView = inflater.inflate(R.layout.static_rv_item, parent, false);

        //return new holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    //populate data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //get the data model based on position
        CategoryRvModel currentItem = rvItems.get(position);

        //set item based on views and data model
        ImageView imageView = holder.imageViewRv;
        imageView.setImageResource(currentItem.getImage());
        TextView textView = holder.textViewRv;
        textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowIndex = position;
                notifyDataSetChanged();
            }
        });
        if (rowIndex == position) {
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
        }
    }

    @Override
    public int getItemCount() {
        return rvItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewRv;
        TextView textViewRv;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRv = itemView.findViewById(R.id.rv_img);
            textViewRv = itemView.findViewById(R.id.rv_text);
            linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }
}
