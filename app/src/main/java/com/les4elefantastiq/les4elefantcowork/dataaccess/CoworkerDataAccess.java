package com.les4elefantastiq.les4elefantcowork.dataaccess;

import com.les4elefantastiq.les4elefantcowork.models.Coworker;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Math on 05/08/16.
 */
public class CoworkerDataAccess {
    public static final String API_URL = "http://comeeting-api.azurewebsites.net";

    public static Coworker getCoworker(String linkedInId) {
        CoworkerInterface coworkerInterface = CommonDataAccess.getRetrofit().create(CoworkerInterface.class);
        Call<Coworker> coworkerCall = coworkerInterface.getProfile(linkedInId);

        try {
            return coworkerCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean login(Coworker coworker) {
        CoworkerInterface coworkerInterface = CommonDataAccess.getRetrofit().create(CoworkerInterface.class);
        Call<Void> loginCall = coworkerInterface.login(coworker);

        try {
            Response<Void> response = loginCall.execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public interface CoworkerInterface {
        @POST("/api/coworker")
        Call<Void> login(@Body Coworker coworker);

        @GET("/api/coworker/{linkedInId}")
        Call<Coworker> getProfile(@Path("linkedInId") String linkedInId);
    }

}
