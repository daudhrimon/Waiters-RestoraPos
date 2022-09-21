package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.ActivitySplashScreenBinding;
import com.restorapos.waiters.utils.SharedPref;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPref.init(this);

        try {
            PackageInfo pInfo =   this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String verName = pInfo.versionName;
            int verCode = pInfo.versionCode;
            binding.version.setText(String.valueOf("Version : "+verName+"."+verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
                DatabaseClient.getInstance(SplashScreenActivity.this).getAppDatabase()
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
            Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, 3000);
    }

    private void navigateToMainActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
           // SharedPref.write("PP", "ppp");
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, 3000);

    }
}
