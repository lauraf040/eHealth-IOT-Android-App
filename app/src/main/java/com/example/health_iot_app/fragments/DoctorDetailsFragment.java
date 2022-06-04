package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Doctor;
import com.squareup.picasso.Picasso;

public class DoctorDetailsFragment extends Fragment {

    private static final String DOCTOR_KEY = "DOCTOR_KEY";
    private Doctor doctor = null;
    private Fragment currentFragment;

    //UI
    TextView tvName, tvCategory, tvRating, tvAge;
    ImageView ivBack, ivProfilePicture;

    public DoctorDetailsFragment() {
        // Required empty public constructor
    }


    public static DoctorDetailsFragment newInstance(Doctor doctor) {
        DoctorDetailsFragment fragment = new DoctorDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        openDefaultFragment(savedInstanceState);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        tvName = view.findViewById(R.id.tv_doctor_details_name);
        tvName.setText(doctor.getName());
        tvCategory = view.findViewById(R.id.tv_doctor_details_category);
        tvCategory.setText(doctor.getCategory());
        tvRating = view.findViewById(R.id.tv_doctor_details_rating);
        tvRating.setText(String.valueOf(doctor.getRating()));
        tvAge = view.findViewById(R.id.tv_doctor_details_age);
        tvAge.setText("Varsta: " + String.valueOf(doctor.getAge()));
        ivBack = view.findViewById(R.id.iv_doctor_back);
        ivBack.setOnClickListener(goBackOnClickListener());

        ivProfilePicture = view.findViewById(R.id.doctor_details_iv_photo);
        if (doctor.getProfileImageURL() != null) {
            Picasso.get().load(doctor.getProfileImageURL())
                    .into(ivProfilePicture);
        }
    }

    private View.OnClickListener goBackOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DoctorsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_homePage, fragment)
                        .commit();
            }
        };
    }


    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = DefaultDoctorDetailsFragment.newInstance(doctor);
            // currentFragment = new DefaultDoctorDetailsFragment();
            openFragment();
        }
    }

    private void openFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_doctor_description, currentFragment)
                .commit();
    }
}