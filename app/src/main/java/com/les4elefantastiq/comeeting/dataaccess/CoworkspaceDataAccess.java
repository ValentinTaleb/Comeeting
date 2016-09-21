package com.les4elefantastiq.comeeting.dataaccess;

import android.support.annotation.NonNull;

import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class CoworkspaceDataAccess {

    /**
     * @return A List of all the Coworkspaces
     */
    @NonNull
    public static Observable<List<Coworkspace>> getCoworkspaces() {
        return CommonDataAccess.getRetrofit().create(CoworkspacesInterface.class).coworkspaces();
    }

    /**
     * @return A List of the Coworkers of the specified Coworkspace
     */
    @NonNull
    public static Observable<List<Coworker>> getCoworkers(Coworkspace coworkspace) {
        return CommonDataAccess.getRetrofit().create(CoworkspaceInterface.class).cowokers(coworkspace.id);
    }

    private interface CoworkspacesInterface {
        @GET("/api/coworkspaces")
        Observable<List<Coworkspace>> coworkspaces();
    }

    private interface CoworkspaceInterface {
        @GET("/api/coworkspace/{coworkspaceId}/coworkers")
        Observable<List<Coworker>> cowokers(@Path("coworkspaceId") String coworkspaceId);
    }

}