package com.restorapos.waiters.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.model.loginModel.LoginResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView signInBtn,reset;
    private EditText emailET, passwordET;
    private SpotsDialog progressDialog;
    private String serviceChargeStatus;
    public static boolean service_status = false;
    private String TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initial();

        if (SharedPref.read("BASEURL", "").isEmpty()) {
            SharedPref.write("BASEURL", getString(R.string.BASE_URL));
        }

        try {
            FirebaseMessaging.getInstance().getToken() //get firebase token
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.wtf(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            TOKEN = task.getResult();
                            FirebaseMessaging.getInstance().subscribeToTopic("global");
                            Log.wtf("TOKEN",TOKEN);
                        }
                    });

            signInBtn.setOnClickListener(v -> {
                if (!Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()){
                    emailET.setError("Enter an email address");
                    emailET.requestFocus();
                    return;
                }
                if (passwordET.getText().toString().length() < 5){
                    passwordET.setError("Password is too short");
                    passwordET.requestFocus();
                    return;
                }
                if (TOKEN.isEmpty()){
                    Toasty.warning(this, "Something went wrong, in this case your device will not get notifications", Toast.LENGTH_SHORT).show();
                }
                progressDialog.show();
                signIN();
            });

            reset.setOnClickListener(view -> {
                SharedPref.write("BASEURL", "");
                startActivity(new Intent(LoginActivity.this, QrCodeActivity.class));
            });

            SharedPref.write("PP", "pp");
        } catch (Exception e) {
            warning();
        }


    }

    private void signIN() {
        WaitersService waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        waitersService.doSignIn(emailET.getText().toString(), passwordET.getText().toString(), TOKEN).
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
                                SharedPref.write("vat", loginResponse.getData().getVat());
                                serviceChargeStatus = loginResponse.getData().getServiceChargeType();
                                String serviceCharge = loginResponse.getData().getServicecharge();
                                double serviceChargechange = Double.parseDouble(serviceCharge);
                                if (serviceChargeStatus.contains("1")) {
                                    progressDialog.dismiss();
                                    service_status = true;
                                    serviceChargechange = serviceChargechange / 100;
                                    SharedPref.write("SC", String.valueOf(serviceChargechange));
                                    SharedPref.write("servicestatus", "true");
                                } else if (serviceChargeStatus.contains("0")) {
                                    progressDialog.dismiss();
                                    SharedPref.write("SCNOCHANGE", loginResponse.getData().getServicecharge());
                                    SharedPref.write("servicestatus", "false");
                                    SharedPref.write("SC", String.valueOf(serviceChargechange));
                                }
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else{
                                Toasty.warning(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toasty.error(LoginActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toasty.warning(LoginActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initial() {
        SharedPref.init(this);
        reset = findViewById(R.id.reset);
        signInBtn = findViewById(R.id.signInBtnId);
        emailET = findViewById(R.id.emailId);
        passwordET = findViewById(R.id.passwordId);
        progressDialog = new SpotsDialog(this, R.style.Custom);
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
