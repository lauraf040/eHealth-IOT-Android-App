package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Doctor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class DefaultDoctorDetailsFragment extends Fragment implements OnMapReadyCallback {


    private static final String DOC_KEY = "DOC_KEY";
    private Doctor doctor;

    //ui
    private RatingBar rbRating;
    private TextView tvDescription, tvPhone, tvEmail, tvPrice, tvLocation;
    private ExtendedFloatingActionButton fabAppointment;

    //private GoogleMap map;


    public DefaultDoctorDetailsFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static DefaultDoctorDetailsFragment newInstance(Doctor doctor) {
        DefaultDoctorDetailsFragment fragment = new DefaultDoctorDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(DOC_KEY, doctor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctor = getArguments().getParcelable(DOC_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        try {
            view = inflater.inflate(R.layout.fragment_default_doctor_details, container, false);
            initComponents(view);
            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_google_maps);
            if (supportMapFragment != null) {
                supportMapFragment.getMapAsync(this);
            }
        } catch (Exception e) {
            Log.e("default", "onCreateView", e);
            throw e;
        }

        return view;
    }

    private void initComponents(View view) {

        rbRating = view.findViewById(R.id.default_rb_rating);
        rbRating.setEnabled(false);
        rbRating.setRating((float) doctor.getRating());
        tvPhone = view.findViewById(R.id.tv_contact_phone);
        tvPhone.setText(doctor.getPhone());

        tvEmail = view.findViewById(R.id.tv_contact_email);
        tvEmail.setText(doctor.getEmail());

        tvDescription = view.findViewById(R.id.default_tv_description);
        tvDescription.setText(doctor.getDescription());

        tvPrice = view.findViewById(R.id.linear_price_tv_sum);
        tvPrice.setText(String.valueOf(doctor.getPrice()));

        tvLocation = view.findViewById(R.id.default_tv_location_doctor);
        tvLocation.setText(doctor.getLocation());

        fabAppointment = view.findViewById(R.id.default_fab_start_appointment);
        fabAppointment.setOnClickListener(openAppointmentFragmenClickListener());

    }

    //=======================================OPEN FRAGMENT=====================
    private View.OnClickListener openAppointmentFragmenClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = MakeAppointmentFragment.newInstance(doctor);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_doctor_description, fragment)
                        .commit();
            }
        };
    }

    //============================================MAPS=============================
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // map = googleMap;
        LatLng location = new LatLng(Double.valueOf(doctor.getLatitude()), Double.valueOf(doctor.getLongitude()));
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(doctor.getLocation()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}