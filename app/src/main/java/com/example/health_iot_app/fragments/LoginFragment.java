package com.example.health_iot_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.health_iot_app.HomeActivity;
import com.example.health_iot_app.R;
import com.example.health_iot_app.databinding.FragmentLoginBinding;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.network.LoginRequest;
import com.example.health_iot_app.network.LoginResponse;
import com.example.health_iot_app.network.RegisterRequest;
import com.example.health_iot_app.network.RegisterResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding loginBinding;
    private ImageView ivPlusRegister, ivIllustration;
    private Button btnLogin;
    private TextInputEditText tiedEmail, tiedPassword;
    private TextInputLayout tilEmail, tilPassword;
    Animation topAnim, bottomAnim;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        initComponents(loginBinding);
        return loginBinding.getRoot();
    }

    private void initComponents(FragmentLoginBinding loginBinding) {
        //register
        ivPlusRegister = loginBinding.getRoot().findViewById(R.id.iv_plus_register);
        ivPlusRegister.setOnClickListener(openRegisterFragmentClickListener());
        //login
        btnLogin = loginBinding.getRoot().findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(openHomeActivityAfterLoginListener(loginBinding));

        //
        tilEmail = loginBinding.getRoot().findViewById(R.id.til_email_login);
        tilPassword = loginBinding.getRoot().findViewById(R.id.til_password_login);
        tiedEmail = loginBinding.getRoot().findViewById(R.id.tied_email_login);
        tiedPassword = loginBinding.getRoot().findViewById(R.id.tied_password_login);
        ivIllustration = loginBinding.getRoot().findViewById(R.id.iv_login);
        //anim
        topAnim = AnimationUtils.loadAnimation(loginBinding.getRoot().getContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(loginBinding.getRoot().getContext(), R.anim.bottom_animation);

        initAnimations();
    }

    private View.OnClickListener openHomeActivityAfterLoginListener(FragmentLoginBinding loginBinding) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRequest loginRequest = new LoginRequest();

                loginRequest.setEmail(tiedEmail.getText().toString());
                loginRequest.setPassword(tiedPassword.getText().toString());
                loginUser(loginRequest, loginBinding);
//                Intent intent = new Intent(getActivity(), HomeActivity.class);
//                startActivity(intent);
            }
        };
    }

    private void initAnimations() {
        tilEmail.setTranslationX(800);
        tilPassword.setTranslationX(800);
        tilEmail.setAlpha(0);
        tilPassword.setAlpha(0);
        tilEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        tilPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        ivIllustration.setAnimation(topAnim);

    }

    private View.OnClickListener openRegisterFragmentClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_loginPage, registerFragment);
                transaction.commit();
            }
        };

    }

    public void loginUser(LoginRequest loginRequest, FragmentLoginBinding loginBinding) {
        Call<LoginResponse> loginResponseCall = ApiClient.getService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    String message = "Login successfully";
                    Toast.makeText(loginBinding.getRoot().getContext(),
                            message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);

                } else {
                    String message = "Unable to login user";
                    Toast.makeText(loginBinding.getRoot().getContext(),
                            message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(loginBinding.getRoot().getContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        });

    }

}