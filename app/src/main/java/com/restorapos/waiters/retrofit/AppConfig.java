package com.restorapos.waiters.retrofit;

import android.content.Context;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    public static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getRetrofit(Context context) {
        SharedPref.init(context);
        return new Retrofit.Builder()
                .baseUrl(SharedPref.read("BASEURL", ""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
