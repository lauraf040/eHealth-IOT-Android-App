package com.example.health_iot_app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.HistoryItemModel;
import com.example.health_iot_app.models.NestedHistoryItemModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryRvAdapter extends RecyclerView.Adapter<HistoryRvAdapter.HistoryViewHolder> {
    private List<HistoryItemModel> parametersNames;
    private List<NestedHistoryItemModel> list = new ArrayList<>();

    public HistoryRvAdapter(List<HistoryItemModel> parametersNames) {
        this.parametersNames = parametersNames;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View view = inflater.inflate(R.layout.item_history, parent, false);
        //return new holder instance
        HistoryRvAdapter.HistoryViewHolder historyViewHolder = new HistoryRvAdapter.HistoryViewHolder(view);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItemModel historyModel = parametersNames.get(position);
        holder.parameterName.setText(historyModel.getParameterText());
        holder.ivParameter.setImageResource(historyModel.getImageId());
        boolean isExpandable = historyModel.isExpandable();
        if (isExpandable) {
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.arrowImage.setImageResource(R.drawable.up_arrow);
        } else {
            holder.relativeLayout.setVisibility(View.GONE);
            holder.arrowImage.setImageResource(R.drawable.down_arrow);
        }
        NestedHistoryRvAdapter nestedAdapter = new NestedHistoryRvAdapter(list);
        holder.nestedRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext())
        );
//        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(nestedAdapter);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyModel.setExpandable(!historyModel.isExpandable());
                list = historyModel.getNestedList();
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return parametersNames.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout constraintLayout;
        private RelativeLayout relativeLayout;
        private TextView parameterName;
        private ImageView arrowImage;
        private RecyclerView nestedRecyclerView;
        private ImageView ivParameter;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.parent_constraint);
            relativeLayout = itemView.findViewById(R.id.relative_history_rv);
            parameterName = itemView.findViewById(R.id.tv_parameter_name);
            arrowImage = itemView.findViewById(R.id.arrow_imageview);
            nestedRecyclerView = itemView.findViewById(R.id.rv_nested);
            ivParameter = itemView.findViewById(R.id.iv_icon);
        }
    }
}
