package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.models.DoctorAppointmentSchedule;
import com.example.health_iot_app.utils.DateConverter;
import com.example.health_iot_app.utils.HoursAdapter;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;


public class MakeAppointmentFragment extends Fragment {

    private static final String DOCTOR_KEY = "DOCTOR_KEY";
    private DayScrollDatePicker datePicker;
    private RecyclerView rvAppointmentHours;
    private HoursAdapter hoursAdapter;
    private ArrayList<String> hoursList, freeHoursList;
    ArrayList<String> times = new ArrayList<>();
    private Doctor doctor = null;

    private String mParam1;
    private String mParam2;

    public MakeAppointmentFragment() {
        // Required empty public constructor
    }


    public static MakeAppointmentFragment newInstance(Doctor doctor) {
        MakeAppointmentFragment fragment = new MakeAppointmentFragment();
        Bundle args = new Bundle();
        args.putParcelable(DOCTOR_KEY, doctor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctor = getArguments().getParcelable(DOCTOR_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_appointment, container, false);
        initComponents(view);
        getValueDayPicker(view);
        return view;
    }

    private void initComponents(View view) {
        datePicker = (DayScrollDatePicker) view.findViewById(R.id.day_date_picker);
        datePicker.setStartDate(01, 07, 2022);
        datePicker.setEndDate(01, 12, 2022);
        //hours
        rvAppointmentHours = view.findViewById(R.id.hour_picker_rv);

    }


    //====================================================DAY PICKER=================================================

    private void getValueDayPicker(View view) {
        datePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                times = new ArrayList<>();
                if (date != null) {
                    Toast.makeText(getContext(),
                            date.toString(),
                            Toast.LENGTH_SHORT).show();
                    if (doctor.getAppointments() != null) {
                        for (DoctorAppointmentSchedule appointment : doctor.getAppointments()) {
                            if (DateConverter.fromDate(date).equals(appointment.getDate())) {
                                times = appointment.getTimes();
                                break;
                            }
                        }
                        if (times != null) {
                            addHours();
                            for (String time : times) {
                                hoursList.remove(time);
                            }
                            populateHoursRecyclerView(view);
                        } else {
                            addHours();
                            populateHoursRecyclerView(view);
                        }
                    }
                }
            }
        });
    }

    //=================================================HOURS RECYCLERVIEW====================================================
    private void addHours() {
        hoursList = new ArrayList<>();
        hoursList.add("08:00");
        hoursList.add("08:40");
        hoursList.add("09:10");
        hoursList.add("09:40");
        hoursList.add("10:00");
        hoursList.add("10:50");
        hoursList.add("11:30");
        hoursList.add("12:10");
        hoursList.add("12:50");
        hoursList.add("14:00");
        hoursList.add("14:40");
        hoursList.add("15:20");
        hoursList.add("16:00");
        hoursList.add("16:40");
        hoursList.add("17:30");
        hoursList.add("18:10");
        hoursList.add("18:50");
    }

    private void populateHoursRecyclerView(View view) {

        hoursAdapter = new HoursAdapter(hoursList);
        rvAppointmentHours.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        rvAppointmentHours.setAdapter(hoursAdapter);
        hoursAdapter.notifyDataSetChanged();
    }
}