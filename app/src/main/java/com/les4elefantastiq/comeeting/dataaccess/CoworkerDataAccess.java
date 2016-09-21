package com.les4elefantastiq.comeeting.dataaccess;

import android.support.annotation.NonNull;

import com.les4elefantastiq.comeeting.models.Coworker;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public class CoworkerDataAccess {

    /**
     * @param linkedInId The id of the Coworker
     * @return A Coworker based on the specified id (initialy defined by linkedIn)
     */
    @NonNull
    public static Observable<Coworker> getCoworker(String linkedInId)  {
        return CommonDataAccess.getRetrofit().create(CoworkerInterface.class).getProfile(linkedInId);
    }

    // TODO : Add Javadoc
    @NonNull
    public static Observable<Void> login(Coworker coworker) {
        return CommonDataAccess.getRetrofit().create(CoworkerInterface.class).login(coworker);
    }

    private interface CoworkerInterface {
        @POST("api/coworker")
        Observable<Void> login(@Body Coworker coworker);

        @GET("api/coworker/{linkedInId}")
        Observable<Coworker> getProfile(@Path("linkedInId") String linkedInId);
    }

}