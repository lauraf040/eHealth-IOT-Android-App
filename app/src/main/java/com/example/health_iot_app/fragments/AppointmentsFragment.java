package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.health_iot_app.R;
import com.example.health_iot_app.utils.AppointmentsFragmentAdapter;
import com.google.android.material.tabs.TabLayout;


public class AppointmentsFragment extends Fragment {

    private static final String USER_ID = "USER_ID";
    private static String userId;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AppointmentsFragmentAdapter adapter;

    public AppointmentsFragment() {
        // Required empty public constructor
    }


    public static AppointmentsFragment newInstance(String param1, String param2) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        tabLayout = view.findViewById(R.id.tablayout_appointments);
        viewPager = view.findViewById(R.id.viewpager_appointments);

        tabLayout.addTab(tabLayout.newTab().setText("Viitoare"));
        tabLayout.addTab(tabLayout.newTab().setText("Trecute"));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapter = new AppointmentsFragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}