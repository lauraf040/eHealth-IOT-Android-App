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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Appointment;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.models.DoctorAppointmentSchedule;
import com.example.health_iot_app.models.UserModel;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.utils.DateConverter;
import com.example.health_iot_app.utils.HoursRvAdapter;
import com.example.health_iot_app.utils.SelectListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeAppointmentFragment extends Fragment implements SelectListener {

    public static final String USER_ID = "USER_ID";
    private static final String USER_SHARED_PREF = "userSharedPref";
    private SharedPreferences preferences;
    String patientId;
    String appDate;
    String time;

    private static final String DOCTOR_KEY = "DOCTOR_KEY";
    private DayScrollDatePicker datePicker;
    private RecyclerView rvAppointmentHours;
    private HoursRvAdapter hoursRvAdapter;
    private ArrayList<String> hoursList, freeHoursList;
    ArrayList<String> times = new ArrayList<>();
    private Doctor doctor = null;
    ExtendedFloatingActionButton btnWantAppointment;


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
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        datePicker.setStartDate(day - 1, month + 1, 2022);
        datePicker.setEndDate(01, 12, 2022);
        //hours
        rvAppointmentHours = view.findViewById(R.id.hour_picker_rv);
        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        patientId = preferences.getString(USER_ID, "");

        btnWantAppointment = view.findViewById(R.id.btn_want_appointment);
        btnWantAppointment.setOnClickListener(makeAppointmentClickListener());
        ApiClient.getService().getDoctorById(doctor.get_id()).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    doctor = response.body();
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {

            }
        });
    }

    private View.OnClickListener makeAppointmentClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appDate != null && patientId != null && time != null) {
                    createAlertDialog();
                } else {
                    Toast.makeText(getContext(), "Selectati ziua si ora!", Toast.LENGTH_SHORT).show();
                }


            }
        };
    }


    //====================================================DAY PICKER=================================================

    private void getValueDayPicker(View view) {
        datePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                times = new ArrayList<>();
                if (date != null) {
                    if (date.before(new Date())) {
                        times = null;
                        Toast.makeText(getContext(), "Data programarii trebuie sa fie ulterioara datei curente!", Toast.LENGTH_SHORT).show();
                        btnWantAppointment.setEnabled(false);
                    } else {
                        btnWantAppointment.setEnabled(true);
                        appDate = DateConverter.fromDate(date);
                        if (doctor.getAppointments() != null) {
                            for (DoctorAppointmentSchedule appointment : doctor.getAppointments()) {
                                if (DateConverter.fromDate(date).equals(appointment.getDate())) {
                                    times = appointment.getTimes();
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

        hoursRvAdapter = new HoursRvAdapter(hoursList, this);
        rvAppointmentHours.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        rvAppointmentHours.setAdapter(hoursRvAdapter);
        hoursRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Object object) {
        time = object.toString();
    }


    //========================================================
    private void createAlertDialog() {
        Button buttonYES = new Button(getContext());
        buttonYES.setText(R.string.dialog_confirm_btn_yes);
        buttonYES.setTextColor(getResources().getColor(R.color.purple));
        int btn1Color = ContextCompat.getColor(getContext(), R.color.primar_color);
        buttonYES.setBackgroundTintList(ColorStateList.valueOf(btn1Color));

        Button buttonNO = new Button(getContext());
        buttonNO.setText(R.string.dialog_confirm_btn_no);
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
                .setTitle(getString(R.string.dialog_title_confirm))
                .setMessage(getString(R.string.dialog_confirm_appointment))
                .setMessageColor(R.color.primar_color)
                .setDialogBackground(Color.WHITE)
                .setCanceledOnTouchOutside(true)
                .addActionButton(buttonYES)
                .addActionButton(buttonNO);
        buttonYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAppointmentOnServer(dialog);
            }
        });
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void makeAppointmentOnServer(LottieDialog dialog) {

        Appointment appointment = new Appointment(doctor.get_id(), patientId, appDate, time);
        Call<Appointment> makeAppointmentCall = ApiClient.getService().makeAppointment(appointment);
        makeAppointmentCall.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    updateDoctorsFreeHours(dialog);

                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {

            }
        });

    }

    private void getUser(LottieDialog dialog) {
        ApiClient.getService().getUserById(patientId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    updateUserPoints(dialog, user);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void updateUserPoints(LottieDialog dialog, UserModel user) {

        user.setNbOfApp(user.getNbOfApp() + 1);
        user.setPointsFromApp(user.getPointsFromApp() + 50);
        ApiClient.getService().updateUser(patientId, user).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Puncte adaugate cu succes!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void updateDoctorsFreeHours(LottieDialog dialog) {
        ArrayList<String> times = new ArrayList<>();
        times.add(time);
        DoctorAppointmentSchedule dateSchedule = new DoctorAppointmentSchedule(appDate, times);
        Call<String> updateCall = ApiClient.getService().updateDoctorAppointment(doctor.get_id(), dateSchedule);
        updateCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getUser(dialog);
                    dialog.cancel();
                    BottomNavigationBar btm = getActivity().findViewById(R.id.bottom_navigation);
                    btm.selectTab(2);
                    Fragment fragment = new AppointmentsFragment();
                    openFragment(fragment);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_homePage, fragment)
                .commit();
    }
}
