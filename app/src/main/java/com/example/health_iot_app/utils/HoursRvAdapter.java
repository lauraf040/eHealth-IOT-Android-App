package com.example.health_iot_app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;

import java.util.ArrayList;

public class HoursRvAdapter extends RecyclerView.Adapter<HoursRvAdapter.HoursViewHolder> {

    private ArrayList<String> hoursList;
    private int selectedHour = -1;
    private SelectListener selectListener;

    public HoursRvAdapter(ArrayList<String> hoursList, SelectListener selectListener) {
        this.hoursList = hoursList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public HoursRvAdapter.HoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View itemView = inflater.inflate(R.layout.item_hour, parent, false);

        //return new holder instance
        HoursViewHolder viewHolder = new HoursViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull HoursRvAdapter.HoursViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String hour = hoursList.get(position);
        holder.tvHour.setText(hour);
        holder.tvHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(hour);
                selectedHour = position;
                notifyDataSetChanged();

            }
        });
        if (selectedHour == position) {
            holder.tvHour.setBackgroundResource(R.drawable.rectangle_fill);
        } else {
            holder.tvHour.setBackgroundResource(R.drawable.rectangle_outline);
        }
    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }


    public static class HoursViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHour;

        public HoursViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour = itemView.findViewById(R.id.tv_item_hours_rv);

        }
    }
}



