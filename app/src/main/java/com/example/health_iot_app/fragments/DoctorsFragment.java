package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.CategoryRvModel;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.utils.DoctorsRvAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsFragment extends Fragment implements DoctorsRvAdapter.OnDoctorListener {
    private static final String CATEGORY_KEY = "CATEGORY_KEY";

    private RecyclerView doctorsRecycleView;
    private DoctorsRvAdapter doctorsRvAdapter;
    private List<Doctor> doctors = new ArrayList<>();
    private List<Doctor> filteredDoctors = new ArrayList<>();
    private Fragment fragment;
    private CategoryRvModel category;
    private boolean filter = false;
    LottieAnimationView animationView;

    public DoctorsFragment() {
        // Required empty public constructor
    }

    public static DoctorsFragment newInstance(CategoryRvModel category) {
        DoctorsFragment fragment = new DoctorsFragment();
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = (CategoryRvModel) getArguments().getSerializable(CATEGORY_KEY);
            filter = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        doctorsRecycleView = view.findViewById(R.id.recycler_doctors);
        animationView = (LottieAnimationView) view.findViewById(R.id.animation_view_doctors);
        if (filter) {
            fetchDoctorsByCategory();
            Log.i("rv", filteredDoctors.toString());
            doctorsRvAdapter = new DoctorsRvAdapter(filteredDoctors, this);
            doctorsRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            doctorsRecycleView.setAdapter(doctorsRvAdapter);

        } else {
            fetchDoctors();
            doctorsRvAdapter = new DoctorsRvAdapter(doctors, this);
            doctorsRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            doctorsRecycleView.setAdapter(doctorsRvAdapter);
        }
    }

    //============================LOAD DOCTORS FROM SERVER=================================
    private void fetchDoctors() {
        ApiClient.getService().getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doctors.addAll(response.body());
                    if (doctors.isEmpty()) {
                        doctorsRecycleView.setVisibility(View.GONE);
                        animationView.setVisibility(View.VISIBLE);
                    }
                    doctorsRvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchDoctorsByCategory() {
        ApiClient.getService().getDoctorsByCategory(category.getText()).enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    filteredDoctors.addAll(response.body());
                    if (filteredDoctors.isEmpty()) {
                        doctorsRecycleView.setVisibility(View.GONE);
                        animationView.setVisibility(View.VISIBLE);
                    }
                    doctorsRvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDoctorClicked(int position) {
        Doctor doctor;
        if (filter) {
            doctor = filteredDoctors.get(position);
        } else {
            doctor = doctors.get(position);
        }
        fragment = DoctorDetailsFragment.newInstance(doctor);
        openFragment();
    }

    private void openFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_homePage, fragment)
                .commit();
    }

}