package com.example.health_iot_app.utils;

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
import com.example.health_iot_app.models.Doctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorsRvAdapter extends RecyclerView.Adapter<DoctorsRvAdapter.DoctorsRvHolder> {

    private List<Doctor> doctors = new ArrayList<>();
    private OnDoctorListener onDoctorListener;

    public DoctorsRvAdapter(List<Doctor> doctors, OnDoctorListener onDoctorListener) {
        this.doctors = doctors;
        this.onDoctorListener = onDoctorListener;
    }

    @NonNull
    @Override
    public DoctorsRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View view = inflater.inflate(R.layout.doctors_list_item, parent, false);
        //return new holder instance
        DoctorsRvAdapter.DoctorsRvHolder doctorsRvHolder = new DoctorsRvAdapter.DoctorsRvHolder(view, onDoctorListener);

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


        TextView tvLocation = holder.tvLocation;
        tvLocation.setText(currentItem.getLocation());

        TextView tvRating = holder.tvRating;
        tvRating.setText(String.valueOf(currentItem.getRating()));
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorsRvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName;
        public TextView tvCategory;
        public TextView tvPhone;
        public TextView tvRating;
        public TextView tvLocation;
        public ImageView ivDoctorPhoto;
        public LinearLayout linearLayout;
        public OnDoctorListener onDoctorListener;

        public DoctorsRvHolder(@NonNull View itemView, OnDoctorListener onDoctorListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvCategory = itemView.findViewById(R.id.tv_doctor_category);
            tvPhone = itemView.findViewById(R.id.tv_doctor_phone);
            ivDoctorPhoto = itemView.findViewById(R.id.iv_doctor_photo);
            tvRating = itemView.findViewById(R.id.tv_doctor_rating);
            tvLocation = itemView.findViewById(R.id.tv_doctor_location);
            linearLayout = itemView.findViewById(R.id.linear_doctor_item);
            this.onDoctorListener = onDoctorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDoctorListener.onDoctorClicked(getAdapterPosition());
        }
    }

    public interface OnDoctorListener {
        void onDoctorClicked(int position);
    }

}
