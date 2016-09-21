package com.les4elefantastiq.comeeting.dataaccess;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Initialize and provide a Retrofit instance
 */
public class CommonDataAccess {

    // -------------- Objects, Variables -------------- //

    public static final String API_URL = "http://comeeting-api.azurewebsites.net";

    private static Retrofit retrofit;


    // ---------------- Public Methods ---------------- //

    /**
     * @return An instance of Retrofit that can be used for the request to the App server
     */
    @NonNull
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}