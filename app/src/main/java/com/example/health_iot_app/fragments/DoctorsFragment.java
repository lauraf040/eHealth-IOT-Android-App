package com.example.health_iot_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Doctor;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.utils.DoctorsRvAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoctorsFragment extends Fragment {

    private RecyclerView doctorsRecycleView;
    private DoctorsRvAdapter doctorsRvAdapter;
    private List<Doctor> doctors = new ArrayList<Doctor>();

    public DoctorsFragment() {
        // Required empty public constructor
    }


//    public static DoctorsFragment newInstance(String param1, String param2) {
//        DoctorsFragment fragment = new DoctorsFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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
        doctorsRvAdapter = new DoctorsRvAdapter(doctors);
        doctorsRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        doctorsRecycleView.setAdapter(doctorsRvAdapter);

        fetchDoctors();

    }

    private void fetchDoctors() {
        ApiClient.getService().getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doctors.addAll(response.body());
                    doctorsRvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}