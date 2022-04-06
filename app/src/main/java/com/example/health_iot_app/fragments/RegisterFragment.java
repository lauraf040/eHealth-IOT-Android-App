package com.example.health_iot_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.health_iot_app.HomeActivity;
import com.example.health_iot_app.R;
import com.example.health_iot_app.databinding.FragmentRegisterBinding;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.network.RegisterRequest;
import com.example.health_iot_app.network.RegisterResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private TextView tvSignin;
    private TextInputLayout tilName, tilEmail, tilAge, tilPhone, tilPassword;
    private TextInputEditText tiedName, tiedEmail, tiedAge, tiedPhone, tiedPassword;
    private ImageView ivRegister;
    private Button btnRegister;
    private @NonNull
    FragmentRegisterBinding registerBinding;
    Animation topAnim, bottomAnim;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        initComponents(registerBinding);
        return registerBinding.getRoot();
    }

    private void initComponents(FragmentRegisterBinding registerBinding) {
        tvSignin = registerBinding.getRoot().findViewById(R.id.tv_signin);
        tvSignin.setOnClickListener(openSigninFragmentClickListener(registerBinding));
        //TIL
        tilName = registerBinding.getRoot().findViewById(R.id.til_name_register);
        tilEmail = registerBinding.getRoot().findViewById(R.id.til_email_register);
        tilAge = registerBinding.getRoot().findViewById(R.id.til_age_register);
        tilPhone = registerBinding.getRoot().findViewById(R.id.til_phone_register);
        tilPassword = registerBinding.getRoot().findViewById(R.id.til_password_register);

        //TIED
        tiedName = registerBinding.getRoot().findViewById(R.id.tied_name_register);
        tiedEmail = registerBinding.getRoot().findViewById(R.id.tied_email_register);
        tiedAge = registerBinding.getRoot().findViewById(R.id.tied_age_register);
        tiedPhone = registerBinding.getRoot().findViewById(R.id.tied_phone_register);
        tiedPassword = registerBinding.getRoot().findViewById(R.id.tied_password_register);


        ivRegister = registerBinding.getRoot().findViewById(R.id.iv_register);
        topAnim = AnimationUtils.loadAnimation(registerBinding.getRoot().getContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(registerBinding.getRoot().getContext(), R.anim.bottom_animation);
        initAnimations();

        btnRegister = registerBinding.getRoot().findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(openHomeActivityClickListener(registerBinding));
    }

    private View.OnClickListener openHomeActivityClickListener(FragmentRegisterBinding registerBinding) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setName(tiedName.getText().toString());
                registerRequest.setEmail(tiedEmail.getText().toString());
                registerRequest.setAge(Integer.parseInt(tiedAge.getText().toString()));
                registerRequest.setPhone(Integer.parseInt(tiedPhone.getText().toString()));
                registerRequest.setPassword(tiedPassword.getText().toString());
                registerUser(registerRequest,registerBinding);
//                Intent intent = new Intent(getActivity(), HomeActivity.class);
//                startActivity(intent);
            }
        };
    }

    private void initAnimations() {
        tilName.setTranslationX(800);
        tilEmail.setTranslationX(800);
        tilAge.setTranslationX(800);
        tilPhone.setTranslationX(800);
        tilPassword.setTranslationX(800);

        tilName.setAlpha(0);
        tilEmail.setAlpha(0);
        tilAge.setAlpha(0);
        tilPhone.setAlpha(0);
        tilPassword.setAlpha(0);

        tilName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        tilEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(250).start();
        tilAge.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        tilPhone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(550).start();
        tilPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        ivRegister.setAnimation(topAnim);
//        ivRegister.setAnimation(bottomAnim);
    }

    private View.OnClickListener openSigninFragmentClickListener(FragmentRegisterBinding registerBinding) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setName(tiedName.getText().toString());
                registerRequest.setEmail(tiedEmail.getText().toString());
                registerRequest.setAge(Integer.parseInt(tiedAge.getText().toString()));
                registerRequest.setPhone(Integer.parseInt(tiedPhone.getText().toString()));
                registerRequest.setPassword(tiedPassword.getText().toString());
                registerUser(registerRequest,registerBinding);
            }
        };
    }

    public void registerUser(RegisterRequest registerRequest, FragmentRegisterBinding registerBinding) {
        Call<RegisterResponse> registerResponseCall = ApiClient.getService().registerUser(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    String message = "Registered successfully";
                    Toast.makeText(registerBinding.getRoot().getContext(),
                            message, Toast.LENGTH_LONG).show();
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout_loginPage, loginFragment);
                    transaction.commit();

                } else {
                    String message = "Unable to register user";
                    Toast.makeText(registerBinding.getRoot().getContext(),
                            message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(registerBinding.getRoot().getContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        });
    }
}