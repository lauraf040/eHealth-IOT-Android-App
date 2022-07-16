package com.example.health_iot_app;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.health_iot_app.fragments.AppointmentsFragment;
import com.example.health_iot_app.fragments.DoctorsFragment;
import com.example.health_iot_app.fragments.HomeFragment;
import com.example.health_iot_app.fragments.ProfileFragment;
import com.example.health_iot_app.fragments.SensorsDataFragment;
import com.example.health_iot_app.models.UserModel;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationBar bottomNavigationBar;
    private Fragment currentFragment;

    private SharedPreferences preferences;
    private static final String USER_SHARED_PREF = "userSharedPref";
    String userId;
    private static final String USER_ID = "USER_ID";
    UserModel loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        loggedUser = bundle.getParcelable(USER_ID);
        openDefaultFragment(savedInstanceState);
        initComponents();
    }


    private void initComponents() {

        bottomNavigationBar = findViewById(R.id.bottom_navigation);
        setNavBar();

        preferences = getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        userId = preferences.getString(USER_ID, "");
    }

    private void setNavBar() {
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_home, "Home"));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.medical_icon, "Doctors"));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_appointment, "Apps"));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_profile, "Profile"));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_sensors, "Data"));
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
        bottomNavigationBar.setTabSelectedListener(getSelectedTabListener());
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
    }

    private BottomNavigationBar.OnTabSelectedListener getSelectedTabListener() {
        return new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        currentFragment = HomeFragment.newInstance(loggedUser);
                        break;
                    case 1:
                        currentFragment = new DoctorsFragment();
                        break;
                    case 2:
                        currentFragment = new AppointmentsFragment();
                        break;
                    case 3:
                        currentFragment = new ProfileFragment();
                        break;
                    case 4:
                        currentFragment = new SensorsDataFragment();
                        break;
                }
                openFragment();

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        }

                ;
    }


    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = HomeFragment.newInstance(loggedUser);
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