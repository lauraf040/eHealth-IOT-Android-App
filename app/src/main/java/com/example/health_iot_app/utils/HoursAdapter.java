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

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HoursViewHolder> {

    private ArrayList<String> hoursList;
    private int selectedHour = -1;

    public HoursAdapter(ArrayList<String> hoursList) {
        this.hoursList = hoursList;
    }

    @NonNull
    @Override
    public HoursAdapter.HoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View itemView = inflater.inflate(R.layout.item_hours_recyclerview, parent, false);

        //return new holder instance
        HoursViewHolder viewHolder = new HoursViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HoursAdapter.HoursViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String hour = hoursList.get(position);
        holder.tvHour.setText(hour);
        holder.tvHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        TextView tvHour;

        public HoursViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour = itemView.findViewById(R.id.tv_item_hours_rv);
        }
    }
}
