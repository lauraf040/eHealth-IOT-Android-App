package com.example.health_iot_app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Appointment;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.models.PairAppDoctor;
import com.example.health_iot_app.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsRvAdapter extends RecyclerView.Adapter<AppointmentsRvAdapter.AppointmentsRvHolder> {

    //    private List<Appointment> appointments = new ArrayList<>();
//    private List<Doctor> doctors = new ArrayList<>();
    // private Doctor doctor = new Doctor();
    private List<PairAppDoctor> list = new ArrayList<>();
    private UserModel user;
    private AppointmentListener appointmentListener;

    public AppointmentsRvAdapter(List<PairAppDoctor> list, AppointmentListener appointmentListener) {
        this.list = list;
        this.appointmentListener = appointmentListener;
    }

    @NonNull
    @Override
    public AppointmentsRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_appointment, parent, false);
        AppointmentsRvAdapter.AppointmentsRvHolder appointmentsRvHolder = new AppointmentsRvHolder(view, appointmentListener);
        return appointmentsRvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsRvHolder holder, int position) {
        PairAppDoctor pair = list.get(position);
        Doctor doctor = pair.getDoctor();
        Appointment currentAppointment = pair.getAppointment();
        holder.appointment = currentAppointment;
        TextView tvDate = holder.tvDate;
        tvDate.setText(currentAppointment.getAppDate());
        TextView tvTime = holder.tvTime;
        tvTime.setText(currentAppointment.getTime());
        TextView tvName = holder.tvName;
        tvName.setText(doctor.getName());
        TextView tvCategory = holder.tvCategory;
        tvCategory.setText(doctor.getCategory());
        TextView tvLocation = holder.tvLocation;
        tvLocation.setText(doctor.getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AppointmentsRvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDate;
        public TextView tvTime;
        public TextView tvName;
        public TextView tvCategory;
        public TextView tvLocation;
        public Button btnCancel;
        Appointment appointment;
        AppointmentListener appointmentListener;

        public AppointmentsRvHolder(@NonNull View itemView, AppointmentListener appointmentListener) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_app_date);
            tvTime = itemView.findViewById(R.id.tv_app_time);
            tvName = itemView.findViewById(R.id.tv_app_name);
            tvCategory = itemView.findViewById(R.id.tv_app_category);
            tvLocation = itemView.findViewById(R.id.tv_app_location);
            btnCancel = itemView.findViewById(R.id.btn_app_cancel);
            this.appointmentListener = appointmentListener;
            btnCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            appointmentListener.onAppointmentClicked(getAbsoluteAdapterPosition());
        }
    }

    public interface AppointmentListener {
        void onAppointmentClicked(int position);
    }
}
