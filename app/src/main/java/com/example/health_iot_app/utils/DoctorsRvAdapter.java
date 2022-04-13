package com.example.health_iot_app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.CategoryRvModel;
import com.example.health_iot_app.models.Doctor;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DoctorsRvAdapter extends RecyclerView.Adapter<DoctorsRvAdapter.DoctorsRvHolder> {

    public List<Doctor> doctors;

    public DoctorsRvAdapter(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorsRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View view = inflater.inflate(R.layout.doctors_list_item, parent, false);
        //return new holder instance
        DoctorsRvAdapter.DoctorsRvHolder doctorsRvHolder = new DoctorsRvAdapter.DoctorsRvHolder(view);

        return doctorsRvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsRvHolder holder, int position) {

        //get the data model based on position
        Doctor currentItem = doctors.get(position);

        //set item based on views and data model
        ImageView imageView = holder.ivDoctorPhoto;
        imageView.setImageResource(currentItem.getProfilePicture());
        TextView tvName = holder.tvName;
        tvName.setText(currentItem.getName());
        TextView tvCategory = holder.tvCategory;
        tvCategory.setText(currentItem.getCategory());
        TextView tvPhone = holder.tvPhone;
        tvPhone.setText(currentItem.getPhone());
        if (currentItem.getProfileImageURL() != null) {
            Picasso.get().load(doctors.get(position).getProfileImageURL())
                    .into(holder.ivDoctorPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorsRvHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvCategory;
        public TextView tvPhone;
        public TextView tvRating;
        public ImageView ivDoctorPhoto;
        public LinearLayout linearLayout;

        public DoctorsRvHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvCategory = itemView.findViewById(R.id.tv_doctor_category);
            tvPhone = itemView.findViewById(R.id.tv_doctor_phone);
            ivDoctorPhoto = itemView.findViewById(R.id.iv_doctor_photo);
            tvRating = itemView.findViewById(R.id.tv_doctor_rating);
            linearLayout = itemView.findViewById(R.id.linear_doctor_item);
        }
    }

}
