package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.utils.SharedPref;

public class SplashScreenActivity extends AppCompatActivity {
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPref.init(this);

        try {
            //WaitersService waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
            SharedPref.write("FOOD", "");
            SharedPref.write("name", "");
            /*SharedPref.write("ORDERID", "");
            SharedPref.write("UPDATETABLE", "");*/
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
