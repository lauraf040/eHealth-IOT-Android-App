package com.example.health_iot_app.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.health_iot_app.fragments.PastAppointmentsFragment;
import com.example.health_iot_app.fragments.UpcomingAppointmentsFragment;

public class AppointmentsFragmentAdapter extends FragmentStateAdapter {
    public AppointmentsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new PastAppointmentsFragment();
        }
        return new UpcomingAppointmentsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
