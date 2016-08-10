package com.les4elefantastiq.comeeting.dataaccess;

import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.les4elefantastiq.comeeting.models.LiveFeedMessage;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Math on 05/08/16.
 */
public class LivefeedDataAccess {
    public static final String API_URL = "http://comeeting-api.azurewebsites.net";

    public static List<LiveFeedMessage> getLivefeedMessages(Coworkspace coworkspace) {
        LivefeedInterface livefeedInterface = CommonDataAccess.getRetrofit().create(LivefeedInterface.class);
        Call<List<LiveFeedMessage>> livefeedMessages = livefeedInterface.getLivefeedMessages(coworkspace.id);

        try {
            return livefeedMessages.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public interface LivefeedInterface {
        @GET("/api/coworkspace/{coworkspaceId}/livefeed/messages")
        Call<List<LiveFeedMessage>> getLivefeedMessages(@Path("coworkspaceId") String coworkspaceId);
    }

}
