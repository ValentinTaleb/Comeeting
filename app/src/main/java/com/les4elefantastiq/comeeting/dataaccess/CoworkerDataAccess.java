package com.les4elefantastiq.comeeting.dataaccess;

import android.support.annotation.NonNull;

import com.les4elefantastiq.comeeting.models.Coworker;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public class CoworkerDataAccess {

    // ---------------- Public Methods ---------------- //

    /**
     * @param linkedInId The id of the Coworker
     * @return A Coworker based on the specified id (initialy defined by linkedIn)
     */
    @NonNull
    public static Observable<Coworker> getCoworker(String linkedInId) {
        return CommonDataAccess.getRetrofit().create(CoworkerInterface.class).getCoworker(linkedInId);
    }

    /**
     * Log the user in with the information provided by LinkedIn
     *
     * @param coworker The current user.
     * @return Nothing. An exception is thrown if the log in fail
     */
    @NonNull
    public static Observable<Void> login(Coworker coworker) {
        return CommonDataAccess.getRetrofit().create(CoworkerInterface.class).login(coworker);
    }


    // -------------- Retrofit Interfaces -------------- //

    private interface CoworkerInterface {
        @POST("api/coworker")
        Observable<Void> login(@Body Coworker coworker);

        @GET("api/coworker/{linkedInId}")
        Observable<Coworker> getCoworker(@Path("linkedInId") String linkedInId);
    }

}