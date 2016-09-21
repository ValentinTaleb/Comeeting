package com.les4elefantastiq.comeeting.dataaccess;

import android.support.annotation.NonNull;

import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.les4elefantastiq.comeeting.models.LiveFeedMessage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class LivefeedDataAccess {

    /**
     * @return A List of LiveFeedMessage of the specified Coworkspace
     */
    @NonNull
    public static Observable<List<LiveFeedMessage>> getLivefeedMessages(Coworkspace coworkspace) {
        return CommonDataAccess.getRetrofit().create(LivefeedInterface.class).getLivefeedMessages(coworkspace.id);

    }

    private interface LivefeedInterface {
        @GET("/api/coworkspace/{coworkspaceId}/livefeed/messages")
        Observable<List<LiveFeedMessage>> getLivefeedMessages(@Path("coworkspaceId") String coworkspaceId);
    }

}