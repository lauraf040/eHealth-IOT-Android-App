package com.example.health_iot_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.health_iot_app.fragments.HomeFragment;
import com.example.health_iot_app.fragments.LoginFragment;

public class HomeActivity extends AppCompatActivity {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openDefaultFragment(savedInstanceState);
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