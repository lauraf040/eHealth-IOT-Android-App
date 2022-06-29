package com.example.health_iot_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.health_iot_app.HomeActivity;
import com.example.health_iot_app.R;
import com.example.health_iot_app.databinding.FragmentLoginBinding;
import com.example.health_iot_app.models.UserModel;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.network.LoginRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    //sharedPreferences
    public static final String USER_ID = "USER_ID";
    private static final String USER_SHARED_PREF = "userSharedPref";
    private SharedPreferences preferences;
    private UserModel loggedUser;
    //
    private FragmentLoginBinding loginBinding;
    private ImageView ivPlusRegister, ivIllustration;
    private Button btnLogin;
    private TextInputEditText tietEmail, tietPassword;
    private TextInputLayout tilEmail, tilPassword;
    private TextView tvForgotPassword;
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
        tietEmail = loginBinding.getRoot().findViewById(R.id.tied_email_login);
        tietPassword = loginBinding.getRoot().findViewById(R.id.tied_password_login);
        ivIllustration = loginBinding.getRoot().findViewById(R.id.iv_login);
        tvForgotPassword = loginBinding.getRoot().findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setOnClickListener(forgotPasswordClickListener());
        //anim
        topAnim = AnimationUtils.loadAnimation(loginBinding.getRoot().getContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(loginBinding.getRoot().getContext(), R.anim.bottom_animation);

        //
        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        initAnimations();
    }

    private View.OnClickListener forgotPasswordClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGetEmailAlertDialog();
            }
        };
    }

    private void showGetEmailAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.get_email_reset_password, null);
        MaterialButton button = view.findViewById(R.id.btn_check_email);
        TextInputEditText tietEmail = view.findViewById(R.id.tiet_email_reset);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tietEmail.getText().toString();
                JsonObject object = new JsonObject();
                object.addProperty("email",email);
                ApiClient.getService().checkEmailForReset(object).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel user = response.body();
                            showResetPasswordAlertDialog(user);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });

            }
        });
        dialog.show();
    }

    private void showResetPasswordAlertDialog(UserModel user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.reset_password, null);
        MaterialButton button = view.findViewById(R.id.btn_reset_password);
        TextInputEditText tietNewPass = view.findViewById(R.id.tiet_new_pass);
        TextInputEditText tietNewPassConfirm = view.findViewById(R.id.tiet_new_pass);
        builder.setView(view);
        AlertDialog dialog2 = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = tietNewPass.getText().toString();
                JsonObject object = new JsonObject();
                object.addProperty("password",password);
                ApiClient.getService().resetPassword(user.get_id(),object).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel user = response.body();
                            dialog2.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });

            }
        });
        dialog2.show();
    }

    private View.OnClickListener openHomeActivityAfterLoginListener(FragmentLoginBinding loginBinding) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setEmail(tietEmail.getText().toString());
                    loginRequest.setPassword(tietPassword.getText().toString());
                    loginUser(loginRequest, loginBinding);
                }
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
        Call<UserModel> loginResponseCall = ApiClient.getService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    String message = "Login successfully";
                    Toast.makeText(loginBinding.getRoot().getContext(),
                            message, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(USER_ID, response.body().get_id());
                    editor.commit();
                    String _id = response.body().get_id();
                    String name = response.body().getName();
                    String email = response.body().getEmail();
                    int age = response.body().getAge();
                    String phone = response.body().getPhone();
                    String createdAt = response.body().getCreatedAt();
                    String updatedAt = response.body().getUpdatedAt();
                    int __v = response.body().get__v();
                    String password = response.body().getPassword();
                    String address = response.body().getAddress();
                    loggedUser = new UserModel(_id, name, email, age, phone,
                            createdAt, updatedAt, __v, password, address);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra(USER_ID, loggedUser);
                    startActivity(intent);

                } else {
                    String message = "Unable to login user";
                    Toast.makeText(loginBinding.getRoot().getContext(),
                            message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(loginBinding.getRoot().getContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isValid() {

        if (tietEmail.getText() == null || tietEmail.getText().toString().length() == 0) {
            tietEmail.setError("Introduceti emailul!");
            return false;
        }
        if (tietPassword.getText() == null || tietPassword.getText().toString().length() == 0) {
            tietPassword.setError("Introduceti parola!");
            return false;
        }
        return true;
    }

}