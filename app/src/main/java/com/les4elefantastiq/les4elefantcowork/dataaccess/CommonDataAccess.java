package com.les4elefantastiq.les4elefantcowork.dataaccess;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommonDataAccess {

    // -------------- Objects, Variables -------------- //

    public static final String API_URL = "http://comeeting-api.azurewebsites.net";

    private static Retrofit retrofit;


    // ---------------- Public Methods ---------------- //

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }


    // ---------------- Private Methods --------------- //

}