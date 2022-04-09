package com.example.health_iot_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.health_iot_app.fragments.DoctorDetailsFragment;
import com.example.health_iot_app.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private Fragment currentFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openDefaultFragment(savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(changeFragmentOnItemSelectedListener());
    }

    private NavigationBarView.OnItemSelectedListener changeFragmentOnItemSelectedListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu_item:
                        currentFragment = new HomeFragment();
                        break;
                    case R.id.doctors_menu_item:
                        currentFragment = new DoctorDetailsFragment();
                        break;
                    case R.id.profile_menu_item:
                        currentFragment = new ProfileFragment();
                        break;
                }
                openFragment();
                return true;
            }
        };
    }

    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = new HomeFragment();
            openFragment();
        }
    }

    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_homePage, currentFragment)
                .commit();
    }
}