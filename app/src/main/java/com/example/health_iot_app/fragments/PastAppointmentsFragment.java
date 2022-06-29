package com.example.health_iot_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Appointment;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.models.PairAppDoctor;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.utils.AppointmentsRvAdapter;
import com.example.health_iot_app.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PastAppointmentsFragment extends Fragment implements AppointmentsRvAdapter.AppointmentListener {

    private RecyclerView rvPastAppointments;
    private AppointmentsRvAdapter adapter;
    private List<Appointment> unfilteredApp = new ArrayList<>();
    // private List<Appointment> pastApp = new ArrayList<>();
    private List<PairAppDoctor> pastAppList = new ArrayList<>();
    //List<Doctor> doctors = new ArrayList<>();
    public static final String USER_ID = "USER_ID";
    private static final String USER_SHARED_PREF = "userSharedPref";
    private SharedPreferences preferences;
    String patientId;
    Doctor doctor;
    LottieAnimationView animationView;


    public PastAppointmentsFragment() {
        // Required empty public constructor
    }


    public static PastAppointmentsFragment newInstance(String param1, String param2) {
        PastAppointmentsFragment fragment = new PastAppointmentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_appointments, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        patientId = preferences.getString(USER_ID, "");
        rvPastAppointments = view.findViewById(R.id.rv_past_appointments);
        animationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
        fetchAppointments();

        adapter = new AppointmentsRvAdapter(pastAppList, this);
        //adapter = new AppointmentsRvAdapter(pastApp, doctors);
        rvPastAppointments.setLayoutManager(
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false)
        );
        rvPastAppointments.setAdapter(adapter);
    }

    private void fetchAppointments() {
        ApiClient.getService().getAppointments(patientId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    unfilteredApp.addAll(response.body());
                    for (Appointment app :
                            unfilteredApp) {
                        if (DateConverter.fromString(app.getAppDate()).before(new Date())) {
                            Call<Doctor> callD = ApiClient.getService().getDoctorById(app.getDoctorID());
                            callD.enqueue(new Callback<Doctor>() {
                                @Override
                                public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                                    PairAppDoctor pair = null;
                                    if (response.isSuccessful() && response.body() != null) {
                                        doctor = response.body();
                                        if (app != null && doctor != null) {
                                            pair = new PairAppDoctor(app, doctor);
                                            pastAppList.add(pair);
                                            adapter.notifyDataSetChanged();
                                        }


                                    }
                                }

                                @Override
                                public void onFailure(Call<Doctor> call, Throwable t) {

                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR GETTINg APPS", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onAppointmentClicked(int position) {

    }
}