package com.les4elefantastiq.comeeting.managers;

import com.les4elefantastiq.comeeting.dataaccess.LivefeedDataAccess;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.les4elefantastiq.comeeting.models.LiveFeedMessage;

import java.util.List;

import rx.Observable;

public class LivefeedManager {

    // ---------------- Public Methods ---------------- //

    public static Observable<List<LiveFeedMessage>> getLiveFeedMessages(Coworkspace coworkspace) {
        return LivefeedDataAccess.getLivefeedMessages(coworkspace);
    }

}