package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.databinding.ActivitySplashBinding;
import com.restorapos.waiters.utils.SharedPref;

public class SplashActivity extends AppCompatActivity {
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPref.init(this);

        try {
            String versionName  = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            binding.version.setText("Version : " + versionName);
        } catch (PackageManager.NameNotFoundException e) {/**/}

        try {
            SharedPref.write("FOOD", "");
            SharedPref.write("name", "");
            String id = SharedPref.read("ID", "");

        } catch (Exception e) {/**/}

        if (SharedPref.read("BASEURL", "").isEmpty()) {
            navigateToLoginActivity();
        } else {
            if (SharedPref.read("LOGGEDIN", "NO").equals("YES")) {
                navigateToMainActivity();
            } else {
                navigateToLoginActivity();
            }
        }
    }

    /*private void deleteTable() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(SplashActivity.this).getAppDatabase()
                        .taskDao()
                        .deleteFoodTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
        SharedPref.write("CartCount","0");
    }*/

    private void navigateToLoginActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, 3000);
    }

    private void navigateToMainActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            // SharedPref.write("PP", "ppp");
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, 3000);

    }
}
