package com.example.health_iot_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Appointment;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.models.PairAppDoctor;
import com.example.health_iot_app.models.UserModel;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.utils.AppointmentsRvAdapter;
import com.example.health_iot_app.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpcomingAppointmentsFragment extends Fragment implements AppointmentsRvAdapter.AppointmentListener {
    private RecyclerView rvUpcomingAppointments;
    private AppointmentsRvAdapter adapter;
    private List<Appointment> unfilteredApp = new ArrayList<>();
    //    private List<Appointment> upcomingApp = new ArrayList<>();
//    private List<Doctor> doctors = new ArrayList<>();
    private List<PairAppDoctor> upcomgAppList = new ArrayList<>();
    public static final String USER_ID = "USER_ID";
    private static final String USER_SHARED_PREF = "userSharedPref";
    private SharedPreferences preferences;
    String patientId;
    Doctor doctor;
    Boolean flag = false;
    LottieAnimationView animationView;

    public UpcomingAppointmentsFragment() {
        // Required empty public constructor
    }


    public static UpcomingAppointmentsFragment newInstance(String param1, String param2) {
        UpcomingAppointmentsFragment fragment = new UpcomingAppointmentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {

        animationView = (LottieAnimationView) view.findViewById(R.id.animation_view_upcoming);
        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        patientId = preferences.getString(USER_ID, "");

        rvUpcomingAppointments = view.findViewById(R.id.rv_upcoming_appointments);
        fetchAppointments();

        adapter = new AppointmentsRvAdapter(upcomgAppList, this);
        //adapter = new AppointmentsRvAdapter(upcomingApp, doctors);
        rvUpcomingAppointments.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        rvUpcomingAppointments.setAdapter(adapter);

    }

    private void fetchAppointments() {
        ApiClient.getService().getAppointments(patientId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    unfilteredApp.addAll(response.body());
                    for (Appointment app :
                            unfilteredApp) {
                        doctor = null;
                        if (DateConverter.fromString(app.getAppDate()).after(new Date())) {
                            Call<Doctor> callD = ApiClient.getService().getDoctorById(app.getDoctorID());
                            callD.enqueue(new Callback<Doctor>() {
                                @Override
                                public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                                    PairAppDoctor pair = null;
                                    if (response.isSuccessful()) {
                                        doctor = response.body();
                                        if (app != null && doctor != null) {
                                            pair = new PairAppDoctor(app, doctor);
                                            upcomgAppList.add(pair);
                                        }
                                        if (upcomgAppList == null) {
                                            rvUpcomingAppointments.setVisibility(View.INVISIBLE);
                                            animationView.setVisibility(View.VISIBLE);
                                        }
                                        adapter.notifyDataSetChanged();

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
        PairAppDoctor pair = upcomgAppList.get(position);
        Appointment app = pair.getAppointment();
        Button buttonYES = new Button(getContext());
        buttonYES.setText("CONFIRM");
        buttonYES.setTextColor(getResources().getColor(R.color.purple));
        int btn1Color = ContextCompat.getColor(getContext(), R.color.primar_color);
        buttonYES.setBackgroundTintList(ColorStateList.valueOf(btn1Color));

        Button buttonNO = new Button(getContext());
        buttonNO.setText("REFUZ");
        buttonNO.setTextColor(getResources().getColor(R.color.purple));
        buttonNO.setAllCaps(false);
        int btn2Color = ContextCompat.getColor(getContext(), R.color.primar_color);
        buttonNO.setBackgroundTintList(ColorStateList.valueOf(btn2Color));
        LottieDialog dialog = new LottieDialog(getContext())
                .setAnimationFromUrl("https://assets5.lottiefiles.com/packages/lf20_eIXuIz.json")
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setAutoPlayAnimation(true)
                .setTitleColor(Color.BLUE)
                .setTitleTextSize(20)
                .setTitle("Confirmare")
                .setMessage("Sunteti sigur ca doriti sa anulati programarea?")
                .setMessageColor(R.color.primar_color)
                .setDialogBackground(Color.WHITE)
                .setCanceledOnTouchOutside(true)
                .addActionButton(buttonYES)
                .addActionButton(buttonNO);
        buttonYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAppointment(app, dialog, position);


            }
        });
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

        Toast.makeText(getContext(), app.getAppDate(), Toast.LENGTH_SHORT).show();
    }


    private void cancelAppointment(Appointment app, LottieDialog dialog, int position) {

        ApiClient.getService().deleteAppHourFromDoctor(doctor.get_id(), app.getAppDate(), app.getTime()).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    deleteAppointment(app, dialog, position);
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {

            }
        });
    }

    private void deleteAppointment(Appointment app, LottieDialog dialog, int position) {
        ApiClient.getService().deleteAppointment(app.get_id()).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                getUser(app, dialog, position);
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {

            }
        });
    }

    private void getUser(Appointment app, LottieDialog dialog, int position) {
        ApiClient.getService().getUserById(patientId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    updateUserPoints(dialog, user, app, position);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void updateUserPoints(LottieDialog dialog, UserModel user, Appointment app, int position) {
        user.setPointsFromApp(user.getPointsFromApp() - 20);
        ApiClient.getService().updateUser(patientId, user).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    upcomgAppList.remove(position);
                    adapter.notifyDataSetChanged();
                    dialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
}