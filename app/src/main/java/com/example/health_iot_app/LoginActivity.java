package com.example.health_iot_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.health_iot_app.fragments.LoginFragment;
import com.example.health_iot_app.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openDefaultFragment(savedInstanceState);
        initComponents();
    }

    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = new LoginFragment();
            openFragment();
        }
    }

    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_loginPage, currentFragment)
                .commit();
    }

    private void initComponents() {
    }
}