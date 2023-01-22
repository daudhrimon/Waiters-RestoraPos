package com.restorapos.waiters.activities;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.ActivityLoginBinding;
import com.restorapos.waiters.model.loginModel.LoginResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import com.google.gson.Gson;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private SpotsDialog progressDialog;
    private String serviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPref.init(this);
        progressDialog = new SpotsDialog(this, R.style.Custom);


        if (SharedPref.read("BASEURL", "") == null ||
                SharedPref.read("BASEURL", "").isEmpty()) {
            SharedPref.write("BASEURL","https://restorapos.com/newrpos/V1/");
        }

        try {
            binding.loginBtn.setOnClickListener(v -> {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.getText().toString()).matches()) {
                    binding.emailEt.setError("Enter an email address");
                    binding.emailEt.requestFocus();
                    return;
                }
                if (binding.passwordEt.getText().toString().length() < 5) {
                    binding.passwordEt.setError("Password is too short");
                    binding.passwordEt.requestFocus();
                    return;
                }

                if (SharedPref.read("TOKEN","") == null || SharedPref.read("TOKEN","").isEmpty()) {
                    Toasty.warning(this, "Something went wrong, in this case you will not be able to get notifications", Toast.LENGTH_SHORT).show();
                }

                progressDialog.show();
                signIN();
            });

            binding.resetBtn.setOnClickListener(view -> {
                SharedPref.write("BASEURL","https://restorapos.com/newrpos/V1/");
                startActivity(new Intent(LoginActivity.this, QrCodeActivity.class));
            });

            SharedPref.write("PP", "pp");
        } catch (Exception e) {
            warning();
        }
    }

    private void signIN() {
        Log.wtf("TOKEN", SharedPref.read("TOKEN",""));

        WaitersService waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        waitersService.doSignIn(binding.emailEt.getText().toString(), binding.passwordEt.getText().toString(), SharedPref.read("TOKEN","")).
                enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        try {
                            Log.d("checkMsg", "onResponse: " + response.body().getMessage());
                            LoginResponse loginResponse = response.body();
                            Gson gson = new Gson();
                            String loginResponses = gson.toJson(loginResponse);
                            if (response.body().getStatus().equals("success")) {
                                Toasty.success(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                SharedPref.write("LOGINRESPONSE", loginResponses);
                                SharedPref.write("LOGGEDIN", "YES");
                                SharedPref.write("ID", loginResponse.getData().getId());
                                SharedPref.write("POWERBY", loginResponse.getData().getPowerBy());
                                SharedPref.write("CURRENCY", loginResponse.getData().getCurrencysign());
                                SharedPref.write("tableMap", loginResponse.getData().getTablemaping());
                                SharedPref.write("vat", String.valueOf(Double.parseDouble(loginResponse.getData().getVat()) / 100));
                                Log.wtf("VAT", String.valueOf(Double.parseDouble(loginResponse.getData().getVat()) / 100));
                                serviceType = loginResponse.getData().getServiceChargeType();
                                String serviceCharge = loginResponse.getData().getServicecharge();
                                double serviceChargechange = Double.parseDouble(serviceCharge);

                                if (serviceType.equals("1")) {
                                    progressDialog.dismiss();
                                    serviceChargechange /= 100;
                                    SharedPref.write("SC", String.valueOf(serviceChargechange));
                                    SharedPref.write("SCT", "1");
                                } else if (serviceType.contains("0")) {
                                    progressDialog.dismiss();
                                    SharedPref.write("SCNOCHANGE", loginResponse.getData().getServicecharge());
                                    SharedPref.write("SCT", "0");
                                    SharedPref.write("SC", String.valueOf(serviceChargechange));
                                }

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toasty.warning(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toasty.error(LoginActivity.this, "Something went wrong, in this case you will not be able to get notifications", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toasty.warning(LoginActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void warning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Base Url is not valid.\nPlease again Scan your valid QR Code");
        builder.setPositiveButton("Rescan QR Code", (dialog, id) -> {
            Intent intent = new Intent(LoginActivity.this, QrCodeActivity.class);
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utils.hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }
}
