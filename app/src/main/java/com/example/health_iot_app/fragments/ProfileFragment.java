package com.example.health_iot_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.health_iot_app.LoginActivity;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.UserModel;
import com.example.health_iot_app.network.ApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    public static final String USER_ID = "USER_ID";
    private static final String USER_SHARED_PREF = "userSharedPref";
    private SharedPreferences preferences;
    String patientId;

    //private UserModel user;
    private TextView tvName;
    private TextView tvAge;
    private TextView tvPoints, tvAppointments, tvLogout;

    private TextInputEditText tietAddressTown;
    private TextInputEditText tietAddressStreet;
    private TextInputEditText tietPhone;
    private TextInputEditText tietEmail;

    private MaterialButton btnUpdateProfile, btnFinishUpdate;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        patientId = preferences.getString(USER_ID, "");
        tvName = view.findViewById(R.id.tv_profile_name);
        tvAge = view.findViewById(R.id.tv_profile_age);

        tietAddressTown = view.findViewById(R.id.tiet_address_town);
        tietAddressStreet = view.findViewById(R.id.tiet_address_street);
        tietPhone = view.findViewById(R.id.tiet_phone_number);
        tietEmail = view.findViewById(R.id.tiet_email);

        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        btnUpdateProfile.setOnClickListener(updateProfileClickListener());

        btnFinishUpdate = view.findViewById(R.id.btn_finish_update);
        btnFinishUpdate.setOnClickListener(finishUpdateOnClickListener());
        getUser();

        tvPoints = view.findViewById(R.id.tv_profile_points);
        tvAppointments = view.findViewById(R.id.tv_profile_bookings);

        tvLogout = view.findViewById(R.id.tv_profile_logout);
        tvLogout.setOnClickListener(logoutClickListener());

        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
    }

    private View.OnClickListener logoutClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().clear().commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private void getUser() {

        ApiClient.getService().getUserById(patientId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    populateViews(user);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

    }

    private View.OnClickListener finishUpdateOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tietAddressStreet.setEnabled(false);
                tietAddressTown.setEnabled(false);
                tietEmail.setEnabled(false);
                tietPhone.setEnabled(false);
                String name = tvName.getText().toString();
                int age = Integer.parseInt(tvAge.getText().toString());
                String address = tietAddressTown.getText().toString() + "/" + tietAddressStreet.getText().toString();
                String phone = tietPhone.getText().toString();
                String email = tietEmail.getText().toString();

                UserModel updatedUser = new UserModel(name, email, age, phone, address);
                ApiClient.getService().updateUser(patientId, updatedUser).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            btnUpdateProfile.setVisibility(View.VISIBLE);
                            btnFinishUpdate.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });


            }
        };
    }

    private View.OnClickListener updateProfileClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tietAddressStreet.setEnabled(true);
                tietAddressTown.setEnabled(true);
                tietEmail.setEnabled(true);
                tietPhone.setEnabled(true);
                btnUpdateProfile.setVisibility(View.INVISIBLE);
                btnFinishUpdate.setVisibility(View.VISIBLE);
            }
        };
    }

    private void populateViews(UserModel user) {
        tvName.setText(user.getName());
        tvAge.setText(String.valueOf(user.getAge()));
        tietPhone.setText(user.getPhone());
        tietEmail.setText(user.getEmail());
        if (user.getAddress() != null && user.getAddress().contains("/")) {
            tietAddressTown.setText(user.getAddress().split("/")[0]);
            tietAddressStreet.setText(user.getAddress().split("/")[1]);
        }
        tvPoints.setText(user.getPointsFromApp().toString());
        tvAppointments.setText(user.getNbOfApp().toString());
    }
}