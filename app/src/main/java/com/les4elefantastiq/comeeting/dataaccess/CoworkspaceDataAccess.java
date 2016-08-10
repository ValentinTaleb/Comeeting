package com.les4elefantastiq.comeeting.dataaccess;

import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Math on 05/08/16.
 */
public class CoworkspaceDataAccess {
    private static final String API_URL = "http://comeeting-api.azurewebsites.net";
    private static Retrofit retrofit;

    public static List<Coworkspace> getAllCoworkspace() {
        Call<List<Coworkspace>> coworkspaces = CommonDataAccess.getRetrofit().create(CoworkspacesInterface.class).coworkspaces();

        try {
            return coworkspaces.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Coworker> getCoworkers(Coworkspace coworkspace) {
        Call<List<Coworker>> coworkers = CommonDataAccess.getRetrofit().create(CoworkspaceInterface.class).cowokers(coworkspace.id);

        try {
            return coworkers.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setCheckIn(Coworkspace coworkspace, Coworker coworker, Boolean checkIn) {
        CoworkspaceCheckInCheckOutInterface checkInInterface = CommonDataAccess.getRetrofit().create(CoworkspaceCheckInCheckOutInterface.class);

        Call<Void> checkInRequest;
        if (checkIn) {
            checkInRequest = checkInInterface.checkin(coworkspace.id, coworker.linkedInId);
        } else {
            checkInRequest = checkInInterface.checkout(coworkspace.id, coworker.linkedInId);
        }

        try {
            checkInRequest.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private interface CoworkspacesInterface {
        @GET("/api/coworkspaces")
        Call<List<Coworkspace>> coworkspaces();
    }

    private interface CoworkspaceInterface {
        @GET("/api/coworkspace/{coworkspaceId}/coworkers")
        Call<List<Coworker>> cowokers(@Path("coworkspaceId") String coworkspaceId);
    }

    private interface CoworkspaceCheckInCheckOutInterface {
        @DELETE("/api/coworkspace/{coworkspaceId}/coworker/{linkedInId}")
        Call<Void> checkout(
                @Path("coworkspaceId") String coworkspaceId,
                @Path("linkedInId") String linkedInId);

        @POST("/api/coworkspace/{coworkspaceId}/coworker/{linkedInId}")
        Call<Void> checkin(
                @Path("coworkspaceId") String coworkspaceId,
                @Path("linkedInId") String linkedInId);
    }

}