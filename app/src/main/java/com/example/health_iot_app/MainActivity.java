package com.example.health_iot_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIMER = 3000;

    Animation topAnim, bottomAnim;
    ImageView imgWelcome;
    TextView tvLogo, tvSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        topAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_animation);
        imgWelcome = findViewById(R.id.img_welcome);
        tvLogo = findViewById(R.id.tv_name);
        tvSlogan = findViewById(R.id.tv_welcome);
        setAnimations();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIMER);
    }

    private void setAnimations() {
        imgWelcome.setAnimation(topAnim);
        imgWelcome.setAnimation(bottomAnim);
        tvLogo.setAnimation(topAnim);
        tvSlogan.setAnimation(topAnim);
    }
}