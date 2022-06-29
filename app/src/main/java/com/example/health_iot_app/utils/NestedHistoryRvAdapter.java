package com.example.health_iot_app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.NestedHistoryItemModel;

import java.util.ArrayList;
import java.util.List;

public class NestedHistoryRvAdapter extends RecyclerView.Adapter<NestedHistoryRvAdapter.NestedViewHolder> {
    private List<NestedHistoryItemModel> values = new ArrayList<>();

    public NestedHistoryRvAdapter(List<NestedHistoryItemModel> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View view = inflater.inflate(R.layout.item_nested_history, parent, false);
        //return new holder instance
        NestedHistoryRvAdapter.NestedViewHolder historyViewHolder = new NestedHistoryRvAdapter.NestedViewHolder(view);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        holder.tvValue.setText(values.get(position).getValue());
        holder.tvUnit.setText(values.get(position).getUnit());
        holder.tvQualify.setText(values.get(position).getQualify());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder {
        private TextView tvValue;
        private TextView tvUnit;
        private TextView tvQualify;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tv_value);
            tvUnit = itemView.findViewById(R.id.tv_unit);
            tvQualify = itemView.findViewById(R.id.tv_qualify);
        }
    }
}
